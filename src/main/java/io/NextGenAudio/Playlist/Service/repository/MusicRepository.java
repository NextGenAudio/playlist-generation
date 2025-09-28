package io.NextGenAudio.Playlist.Service.repository;

import io.NextGenAudio.Playlist.Service.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
