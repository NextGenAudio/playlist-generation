package io.NextGenAudio.Playlist.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, Integer> {
    List<PlaylistMusic> findByPlaylistIdOrderByPositionAsc(Integer playlistId);
}