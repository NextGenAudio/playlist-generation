package io.NextGenAudio.Playlist.Service.controller;

import io.NextGenAudio.Playlist.Service.dto.MusicBrief;
import io.NextGenAudio.Playlist.Service.service.PlaylistService;
import io.NextGenAudio.Playlist.Service.model.primary.Playlist;
import io.NextGenAudio.Playlist.Service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Playlist> createPlaylist(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "artwork", required = false) MultipartFile artwork
    ) {

        Playlist newPlaylist = playlistService.createManualPlaylist(name, description, artwork);
        return ResponseEntity.ok(newPlaylist);

    }

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getUserPlaylists() {
        List<PlaylistDTO> playlists = playlistService.getUserPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MusicBrief>> listFiles(@RequestParam(required = false) Long playlistId) {
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
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long playlistId) {
        try {
            playlistService.deletePlaylist(playlistId);
            return ResponseEntity.ok("Playlist deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //
    @PostMapping("/{playlistId}/tracks")
    public ResponseEntity<Void> addTracksToPlaylist(
            @PathVariable Long playlistId,
            @RequestBody AddTracksRequest request) {
        try {
            if (request.getFileIds() == null || request.getFileIds().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            playlistService.addTracksToPlaylist(
                    playlistId,
                    request.getFileIds()
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    @PostMapping("/{playlistId}/add-collaborator")
    public ResponseEntity<String> addContributor(@RequestParam String profileId, @RequestParam Integer role, @PathVariable Long playlistId) {
        try {
            String response = playlistService.addCollaborator(playlistId, profileId, role);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{playlistId}/collaborators")
    public ResponseEntity<List<CollaboratorDTO>> getCollaborators(@PathVariable Long playlistId) {
        try {
            List<CollaboratorDTO> collaborators = playlistService.getCollaborators(playlistId);
            return ResponseEntity.ok(collaborators);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("remove-collaborator/{playlistId}")
    public ResponseEntity<String> removeCollaborator(@PathVariable Long playlistId, @RequestParam String userId) {
        try {
             String response = playlistService.removeCollaborator(playlistId, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log for debugging
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}