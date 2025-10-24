package io.NextGenAudio.Playlist.Service.service;

import io.NextGenAudio.Playlist.Service.model.primary.Playlist;
import io.NextGenAudio.Playlist.Service.repository.primary.MusicRepository;
import io.NextGenAudio.Playlist.Service.repository.primary.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MusicService {
    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    public List<Long> getPlaylistIds(Long id) {
        List<Playlist> playlists =  musicRepository.findPlaylistsById(id);
        System.out.println(playlists);
        return playlists.stream().map(Playlist::getPlaylistId).toList();
    }

    @Transactional
    public boolean pushPlaylistIds(Long musicId, List<Long> playlistIds) {
        return musicRepository.findById(musicId).map(music -> {
            playlistIds.forEach(playlistId -> {
                playlistRepository.findById(playlistId).ifPresent(playlist -> {
                    if (!music.getPlaylists().contains(playlist)) {
                        music.getPlaylists().add(playlist);
                        playlist.getMusics().add(music);
                    }
                });
            });
            musicRepository.save(music);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean removePlaylistIds(Long musicId, List<Long> playlistIds) {
        return musicRepository.findById(musicId).map(music -> {
            playlistIds.forEach(playlistId -> {
                playlistRepository.findById(playlistId).ifPresent(playlist -> {
                    music.getPlaylists().remove(playlist);
                    playlist.getMusics().remove(music);
                });
            });
            musicRepository.save(music);
            return true;
        }).orElse(false);
    }
}
