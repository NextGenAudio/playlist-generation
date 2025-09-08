package io.NextGenAudio.Playlist.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistMusicRepository playlistMusicRepository;

    private final String MUSIC_LIBRARY_URL = "http://music-library-service/api/tracks/";

    public Playlist createManualPlaylist(String name, String description, UUID userId) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setUserId(userId);
        playlist.setCreatedAt(OffsetDateTime.now());
        return playlistRepository.save(playlist);
    }

    public void addTrackToPlaylist(Integer playlistId, Integer fileId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(MUSIC_LIBRARY_URL + fileId, String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Track with ID " + fileId + " does not exist.");
        }
        List<PlaylistMusic> tracks = playlistMusicRepository.findByPlaylistIdOrderByPositionAsc(playlistId);
        PlaylistMusic newTrack = new PlaylistMusic();
        newTrack.setPlaylistId(playlistId);
        newTrack.setFileId(fileId);
        newTrack.setPosition(tracks.size() + 1);
        newTrack.setCreatedAt(OffsetDateTime.now());
        playlistMusicRepository.save(newTrack);
    }

    public Playlist generateAiPlaylist(String mood, String genre, UUID userId) {
        List<Integer> recommendedTrackIds = getRecommendations(mood, genre);
        if (recommendedTrackIds.isEmpty()) {
            throw new RuntimeException("Could not generate playlist. No tracks found for the criteria.");
        }
        String playlistName = "My " + mood + " " + genre + " Mix";
        Playlist playlist = createManualPlaylist(playlistName, "AI Generated Playlist", userId);
        for (int i = 0; i < recommendedTrackIds.size(); i++) {
            PlaylistMusic track = new PlaylistMusic();
            track.setPlaylistId(playlist.getPlaylistId());
            track.setFileId(recommendedTrackIds.get(i));
            track.setPosition(i + 1);
            track.setCreatedAt(OffsetDateTime.now());
            playlistMusicRepository.save(track);
        }
        return playlist;
    }

    private List<Integer> getRecommendations(String mood, String genre) {
        System.out.println("Fetching recommendations for mood: " + mood + " and genre: " + genre);
        return List.of(101, 205, 30, 42, 55);
    }

    public List<Playlist> getUserPlaylists(UUID userId) {
        return playlistRepository.findByUserId(userId);
    }

    // New methods for update and delete functionality
    public Playlist updatePlaylist(Integer playlistId, String name, String description, UUID userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // Check if the user owns this playlist
        if (!playlist.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to update this playlist");
        }

        playlist.setName(name);
        playlist.setDescription(description);
        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Integer playlistId, UUID userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // Check if the user owns this playlist
        if (!playlist.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to delete this playlist");
        }

        // Delete all tracks from the playlist first
        List<PlaylistMusic> tracks = playlistMusicRepository.findByPlaylistIdOrderByPositionAsc(playlistId);
        playlistMusicRepository.deleteAll(tracks);

        // Delete the playlist
        playlistRepository.delete(playlist);
    }

    public void removeTrackFromPlaylist(Integer playlistId, Integer fileId, UUID userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // Check if the user owns this playlist
        if (!playlist.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to modify this playlist");
        }

        List<PlaylistMusic> tracks = playlistMusicRepository.findByPlaylistIdOrderByPositionAsc(playlistId);
        PlaylistMusic trackToRemove = tracks.stream()
            .filter(track -> track.getFileId().equals(fileId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Track not found in playlist"));

        playlistMusicRepository.delete(trackToRemove);

        // Update positions of remaining tracks
        tracks.stream()
            .filter(track -> track.getPosition() > trackToRemove.getPosition())
            .forEach(track -> {
                track.setPosition(track.getPosition() - 1);
                playlistMusicRepository.save(track);
            });
    }

    public List<PlaylistMusic> getPlaylistTracks(Integer playlistId) {
        return playlistMusicRepository.findByPlaylistIdOrderByPositionAsc(playlistId);
    }
}