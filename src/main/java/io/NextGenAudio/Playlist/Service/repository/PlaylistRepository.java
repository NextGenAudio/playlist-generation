package io.NextGenAudio.Playlist.Service.repository;

import io.NextGenAudio.Playlist.Service.dto.PlaylistDTO;
import io.NextGenAudio.Playlist.Service.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {


    // Find playlists by user ID, ordered by creation date
    @Query("SELECT new io.NextGenAudio.Playlist.Service.dto.PlaylistDTO(" +
            "p.playlistId, p.name,  p.description ,p.isAiGenerated, p.musicCount, p.playlistArt,  p.createdAt, pu.role)" +
            "FROM Playlist p JOIN PlaylistUser pu ON p.playlistId = pu.playlistId " +
            "WHERE pu.userId = :userId ")
    List<PlaylistDTO> findByUserIdOrderByCreatedAtDesc(String userId);


}