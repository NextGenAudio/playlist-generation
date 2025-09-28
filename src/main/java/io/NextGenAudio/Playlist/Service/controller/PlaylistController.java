package io.NextGenAudio.Playlist.Service.controller;

import io.NextGenAudio.Playlist.Service.model.Music;
import io.NextGenAudio.Playlist.Service.service.PlaylistService;
import io.NextGenAudio.Playlist.Service.model.Playlist;
import io.NextGenAudio.Playlist.Service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/playlists")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "artwork", required = false) MultipartFile artwork
    )  {
        try {
            Playlist newPlaylist = playlistService.createManualPlaylist(name, description, artwork);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPlaylist);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getUserPlaylists() {
        List<PlaylistDTO> playlists = playlistService.getUserPlaylists();
        return ResponseEntity.ok(playlists);
    }


    @GetMapping("/list")
    public ResponseEntity<List<Music>> listFiles(@RequestParam(required = false) Long playlistId) {
        return ResponseEntity.ok(playlistService.listFiles(playlistId));
    }

//    @PutMapping("/{playlistId}")
//    public ResponseEntity<Playlist> updatePlaylist(
//            @PathVariable Integer playlistId,
//            @RequestBody UpdatePlaylistRequest request) {
//        try {
//            Playlist updatedPlaylist = playlistService.updatePlaylist(
//                    playlistId,
//                    request.getName()
//
//            );
//            return ResponseEntity.ok(updatedPlaylist);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build();
//        } catch (SecurityException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//    }
//
//    @DeleteMapping("/{playlistId}")
//    public ResponseEntity<Void> deletePlaylist(@PathVariable Integer playlistId) {
//        try {
//            playlistService.deletePlaylist(playlistId);
//            return ResponseEntity.noContent().build();
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build();
//        } catch (SecurityException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//    }
//
        @PostMapping("/{playlistId}/tracks")
        public ResponseEntity<Void> addTracksToPlaylist(
                @PathVariable Long playlistId,
                @RequestBody AddTracksRequest request) {
            try {
                playlistService.addTracksToPlaylist(
                        playlistId,
                        request.getFileIds()
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
            @PathVariable Long playlistId,
            @RequestParam List<Long> musicIds) {
        try {
            playlistService.removeTracksFromPlaylist(playlistId, musicIds);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}