package io.NextGenAudio.Playlist.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    private UUID getCurrentUserId() {
        // This is a placeholder. In a real app, you would get this from the security context (JWT token).
        return UUID.randomUUID();
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody CreatePlaylistRequest request) {
        Playlist newPlaylist = playlistService.createManualPlaylist(request.getName(), request.getDescription(), getCurrentUserId());
        return ResponseEntity.ok(newPlaylist);
    }

    @PostMapping("/generate")
    public ResponseEntity<Playlist> generatePlaylist(@RequestBody GenerateAiPlaylistRequest request) {
        Playlist generatedPlaylist = playlistService.generateAiPlaylist(request.getMood(), request.getGenre(), getCurrentUserId());
        return ResponseEntity.ok(generatedPlaylist);
    }

    @GetMapping
    public ResponseEntity<List<Playlist>> getMyPlaylists() {
        List<Playlist> playlists = playlistService.getUserPlaylists(getCurrentUserId());
        return ResponseEntity.ok(playlists);
    }

    @PostMapping("/{playlistId}/tracks")
    public ResponseEntity<Void> addTrack(@PathVariable Integer playlistId, @RequestBody AddTrackRequest request) {
        playlistService.addTrackToPlaylist(playlistId, request.getFileId());
        return ResponseEntity.ok().build();
    }

    // New endpoints for update and delete functionality
    @PutMapping("/{playlistId}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Integer playlistId, @RequestBody UpdatePlaylistRequest request) {
        Playlist updatedPlaylist = playlistService.updatePlaylist(playlistId, request.getName(), request.getDescription(), getCurrentUserId());
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Integer playlistId) {
        playlistService.deletePlaylist(playlistId, getCurrentUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{playlistId}/tracks/{fileId}")
    public ResponseEntity<Void> removeTrack(@PathVariable Integer playlistId, @PathVariable Integer fileId) {
        playlistService.removeTrackFromPlaylist(playlistId, fileId, getCurrentUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{playlistId}/tracks")
    public ResponseEntity<List<PlaylistMusic>> getPlaylistTracks(@PathVariable Integer playlistId) {
        List<PlaylistMusic> tracks = playlistService.getPlaylistTracks(playlistId);
        return ResponseEntity.ok(tracks);
    }
}