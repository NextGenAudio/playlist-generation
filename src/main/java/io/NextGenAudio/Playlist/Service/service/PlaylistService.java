package io.NextGenAudio.Playlist.Service.service;

import io.NextGenAudio.Playlist.Service.model.*;
import io.NextGenAudio.Playlist.Service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistMusicRepository playlistMusicRepository;

    public Playlist createManualPlaylist(String name, UUID userId) {
        Playlist playlist = new Playlist(name, userId);
        return playlistRepository.save(playlist);
    }

    public List<Playlist> getUserPlaylists(UUID userId) {
        return playlistRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Playlist getPlaylist(Integer playlistId, UUID userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (!playlist.getUserId().equals(userId)) {
            throw new SecurityException("Access denied to this playlist");
        }
        return playlist;
    }

    public List<PlaylistMusic> getPlaylistTracks(Integer playlistId, UUID userId) {
        // Verify user has access to the playlist
        getPlaylist(playlistId, userId);
        return playlistMusicRepository.findByPlaylist_PlaylistIdOrderByPositionAsc(playlistId);
    }

    public Playlist updatePlaylist(Integer playlistId, String name, UUID userId) {
        Playlist playlist = getPlaylist(playlistId, userId);

        if (!playlist.getUserId().equals(userId)) {
            throw new SecurityException("Only the playlist owner can update");
        }

        if (name != null && !name.isEmpty()) {
            playlist.setName(name);
        }
        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Integer playlistId, UUID userId) {
        Playlist playlist = getPlaylist(playlistId, userId);

        if (!playlist.getUserId().equals(userId)) {
            throw new SecurityException("Only playlist owner can delete");
        }
        playlistRepository.delete(playlist);
    }

    public void addTracksToPlaylist(Integer playlistId, List<Integer> fileIds, UUID userId) {
        Playlist playlist = getPlaylist(playlistId, userId);

        if (!playlist.getUserId().equals(userId)) {
            throw new SecurityException("Only playlist owner can add tracks");
        }

        Integer currentMaxPosition = playlistMusicRepository.findMaxPositionByPlaylistId(playlistId);
        int position = (currentMaxPosition == null ? 1 : currentMaxPosition + 1);

        for (Integer fileId : fileIds) {
            // Updated to call the corrected repository method
            if (!playlistMusicRepository.existsByPlaylist_PlaylistIdAndMusicId(playlistId, fileId.longValue())) {
                PlaylistMusic track = new PlaylistMusic(playlist, fileId.longValue(), position++);
                playlistMusicRepository.save(track);
            }
        }
    }

    public void removeTracksFromPlaylist(Integer playlistId, List<Integer> trackIds, UUID userId) {
        Playlist playlist = getPlaylist(playlistId, userId);

        if (!playlist.getUserId().equals(userId)) {
            throw new SecurityException("Only playlist owner can remove tracks");
        }

        List<PlaylistMusic> tracksToRemove = playlistMusicRepository.findByPlaylistAndIdIn(playlist, trackIds);
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