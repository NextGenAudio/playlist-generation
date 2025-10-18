package io.NextGenAudio.Playlist.Service.controller;


import io.NextGenAudio.Playlist.Service.dto.PlaylistIdsRequest;
import io.NextGenAudio.Playlist.Service.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping("/playlist-ids/{id}")
    public List<Long> getPlaylistIds(@PathVariable Long id) {
        System.out.println("Reqesting playlist ids for music id: " + id);
        return musicService.getPlaylistIds(id);
    }

    @PostMapping("/playlist-ids/{id}")
    public boolean postPlaylistIds(@PathVariable Long id, @RequestBody PlaylistIdsRequest request) {
        System.out.println("Reqesting playlist ids for music id: " + id);
        return musicService.pushPlaylistIds(id, request.getPlaylistIds());
    }

    @PostMapping("/remove-playlist-ids/{id}")
    public boolean removePlaylistIds(@PathVariable Long id, @RequestBody PlaylistIdsRequest request) {
        System.out.println("Reqesting playlist ids for music id: " + id);
        return musicService.removePlaylistIds(id, request.getPlaylistIds());
    }

}
