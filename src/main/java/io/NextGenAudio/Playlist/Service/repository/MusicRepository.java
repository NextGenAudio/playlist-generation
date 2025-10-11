package io.NextGenAudio.Playlist.Service.repository;

import io.NextGenAudio.Playlist.Service.model.Music;
import io.NextGenAudio.Playlist.Service.dto.MusicBrief;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long> {

    @Query("SELECT new io.NextGenAudio.Playlist.Service.dto.MusicBrief(" +
           "m.id, m.filename, m.path, m.title, m.artist, m.album, m.metadata, " +
           "m.listenCount, m.isLiked) " +
           "FROM PlaylistMusic pm JOIN Musics m ON pm.musicId = m.id " +
           "WHERE pm.playlist.playlistId = :playlistId " +
           "ORDER BY pm.position ASC")
    List<MusicBrief> findMusicBriefByPlaylistId(@Param("playlistId") Long playlistId);

}
