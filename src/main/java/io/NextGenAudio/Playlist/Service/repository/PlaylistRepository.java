package io.NextGenAudio.Playlist.Service.repository;

import io.NextGenAudio.Playlist.Service.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    // Find playlists by user ID
    List<Playlist> findByUserId(UUID userId);

    // Find playlists by user ID, ordered by creation date
    List<Playlist> findByUserIdOrderByCreatedAtDesc(UUID userId);

}