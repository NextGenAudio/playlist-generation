package io.NextGenAudio.Playlist.Service.repository;

import io.NextGenAudio.Playlist.Service.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    // Find playlists by user ID
    List<Playlist> findByUserId(String userId);

    // Find playlists by user ID, ordered by creation date
    List<Playlist> findByUserIdOrderByCreatedAtDesc(String userId);


}