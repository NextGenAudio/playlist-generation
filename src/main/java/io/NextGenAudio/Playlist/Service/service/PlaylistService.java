package io.NextGenAudio.Playlist.Service.service;

import io.NextGenAudio.Playlist.Service.dto.CollaboratorDTO;
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

import io.NextGenAudio.Playlist.Service.dto.SuggestedPlaylistDTO;
import io.NextGenAudio.Playlist.Service.dto.TopMoodGenreDTO;
import org.springframework.data.domain.PageRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistUserRepository playlistUserRepository;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private PlaylistMusicRepository playlistMusicRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private PlaylistNameMapper playlistNameMapper;

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("Not authenticated");
        }
        // Get the user ID (UUID you set in your filter)
        return auth.getName();
    }

    public Playlist createManualPlaylist(String name, String description, MultipartFile artwork) {
        String bucketName = "sonex2";
        String currentUserId = getCurrentUserId();

        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setIsAiGenerated(false);
        playlist.setCreatedAt(OffsetDateTime.now());
        playlist.setMusicCount(0L);

        if (artwork != null && !artwork.isEmpty()) {
            try {
                String uniqueFileName = System.currentTimeMillis() + "_" + artwork.getOriginalFilename();
                String s3Key = currentUserId + "/images/" + uniqueFileName;

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

        // Save the playlist first
        Playlist savedPlaylist = playlistRepository.save(playlist);

        // Create the playlist-user relationship with owner role (0)
        PlaylistUser playlistUser = new PlaylistUser();
        playlistUser.setPlaylistId(savedPlaylist.getPlaylistId());
        playlistUser.setUserId(currentUserId);
        playlistUser.setRole(0); // 0 for owner
        playlistUser.setAddedAt(OffsetDateTime.now());
        playlistUserRepository.save(playlistUser);

        return savedPlaylist;
    }

    public List<PlaylistDTO> getUserPlaylists() {
        return playlistRepository.findByUserIdOrderByCreatedAtDesc(getCurrentUserId());
    }

    public List<MusicBrief> listFiles(Long playlistId) {
        String currentUserId = getCurrentUserId();

        // Check if user has access to this playlist
        if (!playlistUserRepository.existsByPlaylistIdAndUserId(playlistId, currentUserId)) {
            throw new SecurityException("You don't have access to this playlist");
        }

        // Use efficient database query that selects only the required fields
        return musicRepository.findMusicBriefByPlaylistId(playlistId);
    }

    public void deletePlaylist(Long playlistId) {
        String currentUserId = getCurrentUserId();

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // Check if user is the owner of the playlist
        if (!playlistUserRepository.isPlaylistOwner(playlistId, currentUserId)) {
            throw new SecurityException("Only playlist owner can delete");
        }

        playlistRepository.delete(playlist);
    }

    public void addTracksToPlaylist(Long playlistId, List<Long> fileIds) {
        String currentUserId = getCurrentUserId();

        // Fetch the playlist entity
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // Check if user has access to this playlist (owner or collaborator)
        if (!playlistUserRepository.existsByPlaylistIdAndUserId(playlistId, currentUserId)) {
            throw new SecurityException("You don't have access to this playlist");
        }

        // Find the current max position in the playlist
        Integer currentMaxPosition = playlistMusicRepository.findMaxPositionByPlaylistId(playlistId);
        int position = (currentMaxPosition == null ? 1 : currentMaxPosition + 1);

        for (Long fileId : fileIds) {
            // Avoid duplicates
            boolean exists = playlistMusicRepository
                    .existsByPlaylist_PlaylistIdAndMusicId(playlistId, fileId);

            if (!exists) {
                PlaylistMusic track = new PlaylistMusic(playlist, fileId, position++);
                playlistMusicRepository.save(track);
            }
        }
    }

    public void removeTracksFromPlaylist(Long playlistId, List<Long> musicIds) {
        String currentUserId = getCurrentUserId();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id " + playlistId));

        // Check if user has access to this playlist (owner or collaborator)
        if (!playlistUserRepository.existsByPlaylistIdAndUserId(playlistId, currentUserId)) {
            throw new SecurityException("You don't have access to this playlist");
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

    public List<CollaboratorDTO> getCollaborators(Long playlistId) {
        String currentUserId = getCurrentUserId();

        // Check if user has access to this playlist
        if (!playlistUserRepository.existsByPlaylistIdAndUserId(playlistId, currentUserId)) {
            throw new SecurityException("You don't have access to this playlist");
        }

        return playlistUserRepository.findUserIdsByPlaylistId(playlistId);
    }

    public String addCollaborator(Long playlistId, String collaboratorUserId, Integer role) {
        String currentUserId = getCurrentUserId();
        System.out.println("=== Add Collaborator Debug ===");
        System.out.println("Current User ID: " + currentUserId);
        System.out.println("Playlist ID: " + playlistId);
        System.out.println("Collaborator User ID to add: " + collaboratorUserId);
        System.out.println("Role to assign: " + role);

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // Check owner with detailed logging
        boolean isOwner = playlistUserRepository.isPlaylistOwner(playlistId, currentUserId);
        System.out.println("Is current user owner? " + isOwner);

        // Get the actual owner for debugging
        playlistUserRepository.findOwnerByPlaylistId(playlistId).ifPresentOrElse(
            owner -> System.out.println("Actual owner user ID: '" + owner.getUserId() + "' (length: " + owner.getUserId().length() + ")"),
            () -> System.out.println("No owner found for this playlist!")
        );

        System.out.println("Current user ID: '" + currentUserId + "' (length: " + currentUserId.length() + ")");

        // Check if user is the owner of the playlist
        if (!isOwner) {
            throw new SecurityException("Only playlist owner can add collaborators");
        }

        // Check if collaborator already exists
        boolean exists = playlistUserRepository.existsByPlaylistIdAndUserId(playlistId, collaboratorUserId);
        if (exists) {
            throw new IllegalArgumentException("User is already a collaborator");
        }

        PlaylistUser collaborator = new PlaylistUser();
        collaborator.setPlaylistId(playlistId);
        collaborator.setUserId(collaboratorUserId);
        collaborator.setRole(role); // 1 for collaborator
        collaborator.setAddedAt(OffsetDateTime.now());
        playlistUserRepository.save(collaborator);

        return "Collaborator added successfully";
    }

    public String removeCollaborator(Long playlistId, String collaboratorUserId) {
        String currentUserId = getCurrentUserId();

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // Check if user is the owner of the playlist
        if (!playlistUserRepository.isPlaylistOwner(playlistId, currentUserId)) {
            throw new SecurityException("Only playlist owner can remove collaborators");
        }

        PlaylistUser collaborator = playlistUserRepository
                .findByPlaylistIdAndUserId(playlistId, collaboratorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Collaborator not found"));

        playlistUserRepository.delete(collaborator);
        return "Collaborator removed successfully";
    }

    public List<SuggestedPlaylistDTO> getSuggestedPlaylists() {
        // We use PageRequest to get only the TOP 10 results from the query
        List<TopMoodGenreDTO> topCombinations = musicRepository
                .findTopMoodGenreCombinations(PageRequest.of(0, 10));

        // Map the database results to the final DTO
        return topCombinations.stream()
                .map(combo -> new SuggestedPlaylistDTO(
                        playlistNameMapper.getPlaylistName(combo.getMood(), combo.getGenre()),
                        combo.getMood(),
                        combo.getGenre()
                ))
                .collect(Collectors.toList());
    }

    public List<MusicBrief> getTracksForSuggestedPlaylist(String mood, String genre) {
        if (mood == null || genre == null) {
            throw new IllegalArgumentException("Mood and genre parameters are required");
        }
        return musicRepository.findMusicBriefByMoodAndGenre(mood, genre);
    }
}
