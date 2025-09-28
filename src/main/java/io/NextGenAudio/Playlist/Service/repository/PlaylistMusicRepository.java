package io.NextGenAudio.Playlist.Service.repository;

import io.NextGenAudio.Playlist.Service.model.Playlist;
import io.NextGenAudio.Playlist.Service.model.PlaylistMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, Integer> {

    List<PlaylistMusic> findByPlaylist_PlaylistIdOrderByPositionAsc(Long playlistId);

    @Query("SELECT MAX(pm.position) FROM PlaylistMusic pm WHERE pm.playlist.playlistId = :playlistId")
    Integer findMaxPositionByPlaylistId(@Param("playlistId") Long playlistId);

    // Corrected method name from fileId to musicId
    boolean existsByPlaylist_PlaylistIdAndMusicId(Long playlistId, Long musicId);

    List<PlaylistMusic> findByPlaylistAndIdIn(Playlist playlist, List<Long> trackIds);
}