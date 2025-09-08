package io.NextGenAudio.Playlist.Service;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "playlist_music")
public class PlaylistMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "playlist_id", nullable = false)
    private Integer playlistId;

    @Column(name = "file_id", nullable = false)
    private Integer fileId;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    // Manual getter and setter methods (fallback for Lombok issues)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPlaylistId() { return playlistId; }
    public void setPlaylistId(Integer playlistId) { this.playlistId = playlistId; }

    public Integer getFileId() { return fileId; }
    public void setFileId(Integer fileId) { this.fileId = fileId; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}