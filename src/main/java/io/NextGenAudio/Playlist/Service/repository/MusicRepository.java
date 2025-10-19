package io.NextGenAudio.Playlist.Service.repository;

import io.NextGenAudio.Playlist.Service.model.Music;
import io.NextGenAudio.Playlist.Service.dto.MusicBrief;
import io.NextGenAudio.Playlist.Service.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import io.NextGenAudio.Playlist.Service.dto.TopMoodGenreDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {

    @Query("SELECT new io.NextGenAudio.Playlist.Service.dto.MusicBrief(" +
           "m.id, m.filename, m.path, m.title, m.artist, m.album, m.metadata, " +
           "m.listenCount, m.isLiked) " +
           "FROM PlaylistMusic pm JOIN Musics m ON pm.musicId = m.id " +
           "WHERE pm.playlist.playlistId = :playlistId " +
           "ORDER BY pm.position ASC")
    List<MusicBrief> findMusicBriefByPlaylistId(@Param("playlistId") Long playlistId);

    @Query("SELECT pm.playlist FROM PlaylistMusic pm WHERE pm.musicId = :musicId")
    List<Playlist> findPlaylistsById(@Param("musicId") Long musicId);

    @Query("SELECT new io.NextGenAudio.Playlist.Service.dto.TopMoodGenreDTO(mo.mood, g.genre, AVG(m.xScore)) " +
            "FROM Musics m " +
            "JOIN m.genre g " +
            "JOIN m.mood mo " +
            "WHERE g.genre IS NOT NULL AND mo.mood IS NOT NULL " +
            "GROUP BY mo.mood, g.genre " +
            "ORDER BY AVG(m.xScore) DESC")
    List<TopMoodGenreDTO> findTopMoodGenreCombinations(Pageable pageable);

    @Query("SELECT new io.NextGenAudio.Playlist.Service.dto.MusicBrief(" +
            "m.id, m.filename, m.path, m.title, m.artist, m.album, m.metadata, " +
            "m.listenCount, m.isLiked) " +
            "FROM Musics m " +
            "JOIN m.genre g " +
            "JOIN m.mood mo " +
            "WHERE g.genre = :genre AND mo.mood = :mood " +
            "ORDER BY m.xScore DESC")
    List<MusicBrief> findMusicBriefByMoodAndGenre(@Param("mood") String mood, @Param("genre") String genre);

}
