package io.NextGenAudio.Playlist.Service.service;

import io.NextGenAudio.Playlist.Service.dto.PlaylistDTO;
import io.NextGenAudio.Playlist.Service.dto.MusicBrief;
import io.NextGenAudio.Playlist.Service.model.*;
import io.NextGenAudio.Playlist.Service.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import net.coobird.thumbnailator.Thumbnails;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private S3Client s3Client;
    private final String bucketName = "sonex2";

    @Autowired
    private PlaylistMusicRepository playlistMusicRepository;

    @Autowired
    private MusicRepository musicRepository;


    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("Not authenticated");
        }
        // Get the user ID (UUID you set in your filter)
        return auth.getName();
    }

    public Playlist createManualPlaylist(String name,  String description, MultipartFile artwork) {
        Playlist playlist = new Playlist(name, getCurrentUserId());
        playlist.setDescription(description);
        playlist.setIsAiGenerated(false);
        playlist.setCreatedAt(java.time.OffsetDateTime.now());
        playlist.setMusicCount(0L);


        if (artwork != null && !artwork.isEmpty()) {
            String userId = getCurrentUserId();

            try {
                String uniqueFileName = System.currentTimeMillis() + "_" + artwork.getOriginalFilename();
                String s3Key = userId + "/images/" + uniqueFileName;

                // ✅ Compress image in-memory
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Thumbnails.of(artwork.getInputStream())
                        .size(800, 800)       // resize (max 800px)
                        .outputQuality(0.7)   // reduce quality for smaller file
                        .toOutputStream(outputStream);

                byte[] compressedBytes = outputStream.toByteArray();

                // ✅ Upload compressed file to S3 (make public)
                try (InputStream inputStream = new ByteArrayInputStream(compressedBytes)) {
                    s3Client.putObject(
                            PutObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(s3Key)
                                    .contentType(artwork.getContentType())
                                    .build(),
                            RequestBody.fromInputStream(inputStream, compressedBytes.length)
                    );
                }

                // ✅ Store full public URL in DB
                String publicUrl = "https://" + bucketName + ".s3.amazonaws.com/" + s3Key;
                playlist.setPlaylistArt(publicUrl);

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload artwork to S3", e);
            }
        }

        return playlistRepository.save(playlist);
    }

    public List<PlaylistDTO> getUserPlaylists() {
        return playlistRepository.findByUserIdOrderByCreatedAtDesc(getCurrentUserId())
                .stream()
                .map(p -> new PlaylistDTO(p.getPlaylistId(), p.getName(), p.getDescription(), p.getIsAiGenerated(), p.getMusicCount(), p.getPlaylistArt()))
                .toList();
    }

//    public PlaylistWithMusicsDTO getPlaylistWithMusics(Long playlistId) {
//        Playlist playlist = playlistRepository.findById(playlistId)
//                .orElseThrow(() -> new RuntimeException("Playlist not found"));
//
//        return new PlaylistWithMusicsDTO(
//                playlist.getPlaylistId(),
//                playlist.getName(),
//                playlist.getDescription(),
//                Boolean.TRUE.equals(playlist.getIsAiGenerated()),
//                playlist.getMusics().stream()
//                        .map(m -> new Music())
//                        .toList()
//        );
//    }



    public List<MusicBrief> listFiles(Long playlistId) {
        String currentUserId = getCurrentUserId();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        if (!playlist.getUserId().equals(currentUserId)) {
            throw new SecurityException("Only the playlist owner can view");
        }

        // Use efficient database query that selects only the required fields
        return musicRepository.findMusicBriefByPlaylistId(playlistId);
    }




//        Playlist playlist = listFiles(playlistId);
//
//        if (!playlist.getCurrentUserId().equals(currentUserId)) {
//            throw new SecurityException("Only the playlist owner can update");
//        }
//
//        if (name != null && !name.isEmpty()) {
//            playlist.setName(name);
//        }
//        return playlistRepository.save(playlist);
//    }

    public void deletePlaylist(Long playlistId) {
        String currentUserId = getCurrentUserId();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (!playlist.getUserId().equals(currentUserId)) {
            throw new SecurityException("Only playlist owner can delete");
        }
        playlistRepository.delete(playlist);
    }

    public void addTracksToPlaylist(Long playlistId, List<Long> fileIds) {
        String currentUserId = getCurrentUserId();

        // Fetch the playlist entity
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

//         Check ownership (uncomment if needed)
         if (!playlist.getUserId().equals(currentUserId)) {
             throw new SecurityException("Only playlist owner can add tracks");
         }

        // Find the current max position in the playlist
        Integer currentMaxPosition = playlistMusicRepository.findMaxPositionByPlaylistId(playlistId);
        int position = (currentMaxPosition == null ? 1 : currentMaxPosition + 1);

        for (Long fileId : fileIds) {
            // Avoid duplicates
            boolean exists = playlistMusicRepository
                    .existsByPlaylist_PlaylistIdAndMusicId(playlistId, fileId.longValue());

            if (!exists) {
                PlaylistMusic track = new PlaylistMusic(playlist, fileId.longValue(), position++);
                playlistMusicRepository.save(track);
            }
        }
    }


    public void removeTracksFromPlaylist(Long playlistId, List<Long> musicIds) {
        String currentUserId = getCurrentUserId();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id " + playlistId));;

        if (!playlist.getUserId().equals(currentUserId)) {
            throw new SecurityException("Only playlist owner can remove tracks");
        }

        List<PlaylistMusic> tracksToRemove = playlistMusicRepository.findByPlaylistAndIdIn(playlist, musicIds);
        playlistMusicRepository.deleteAll(tracksToRemove);

        reorderTracksAfterRemoval(playlist);
    }

    private void reorderTracksAfterRemoval(Playlist playlist) {
        List<PlaylistMusic> remainingTracks = playlistMusicRepository.findByPlaylist_PlaylistIdOrderByPositionAsc(playlist.getPlaylistId());
        int position = 1;
        for (PlaylistMusic track : remainingTracks) {
            track.setPosition(position++);
            playlistMusicRepository.save(track);
        }
    }


}
