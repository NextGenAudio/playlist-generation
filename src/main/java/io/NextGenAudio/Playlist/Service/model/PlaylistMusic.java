package io.NextGenAudio.Playlist.Service.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "playlist_musics")
public class PlaylistMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @Column(name = "music_id", nullable = false)
    private Long musicId;

    @Column
    private Integer position;

    @Column(name = "added_at", nullable = false)
    private OffsetDateTime addedAt;

    // Constructors
    public PlaylistMusic() {
        this.addedAt = OffsetDateTime.now();
    }

    public PlaylistMusic(Playlist playlist, Long musicId, Integer position) {
        this();
        this.playlist = playlist;
        this.musicId = musicId;
        this.position = position;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }

    public Long getMusicId() { return musicId; }
    public void setMusicId(Long musicId) { this.musicId = musicId; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public OffsetDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(OffsetDateTime addedAt) { this.addedAt = addedAt; }
}