package io.NextGenAudio.Playlist.Service.controller;

import io.NextGenAudio.Playlist.Service.service.PlaylistService;
import io.NextGenAudio.Playlist.Service.model.Playlist;
import io.NextGenAudio.Playlist.Service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    // Helper to get a placeholder user ID.
    private UUID getCurrentUserId() {
        return UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody CreatePlaylistRequest request) {
        try {
            Playlist newPlaylist = playlistService.createManualPlaylist(
                    request.getName(),
                    getCurrentUserId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(newPlaylist);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Playlist>> getUserPlaylists() {
        List<Playlist> playlists = playlistService.getUserPlaylists(getCurrentUserId());
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable Integer playlistId) {
        try {
            Playlist playlist = playlistService.getPlaylist(playlistId, getCurrentUserId());
            return ResponseEntity.ok(playlist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<Playlist> updatePlaylist(
            @PathVariable Integer playlistId,
            @RequestBody UpdatePlaylistRequest request) {
        try {
            Playlist updatedPlaylist = playlistService.updatePlaylist(
                    playlistId,
                    request.getName(),
                    getCurrentUserId()
            );
            return ResponseEntity.ok(updatedPlaylist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Integer playlistId) {
        try {
            playlistService.deletePlaylist(playlistId, getCurrentUserId());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/{playlistId}/tracks")
    public ResponseEntity<Void> addTracksToPlaylist(
            @PathVariable Integer playlistId,
            @RequestBody AddTracksRequest request) {
        try {
            playlistService.addTracksToPlaylist(
                    playlistId,
                    request.getFileIds(),
                    getCurrentUserId() // Removed the extra null argument
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{playlistId}/tracks")
    public ResponseEntity<Void> removeTracksFromPlaylist(
            @PathVariable Integer playlistId,
            @RequestParam List<Integer> trackIds) {
        try {
            playlistService.removeTracksFromPlaylist(playlistId, trackIds, getCurrentUserId());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}