package io.NextGenAudio.Playlist.Service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "playlists")
@JsonIgnoreProperties("tracks")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Integer playlistId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "is_ai_generated")
    private Boolean isAiGenerated = false;

    @Column(name = "is_smart")
    private Boolean isSmart = false;

    @Column
    private String genre;

    @Column
    private String mood;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PlaylistMusic> tracks;

    // Constructors
    public Playlist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public Playlist(String name, UUID userId) {
        this();
        this.name = name;
        this.userId = userId;
    }

    public Playlist(String name, String description, UUID userId, Boolean isPublic) {
        this();
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.isPublic = isPublic != null ? isPublic : false;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    // Getters and Setters
    public Integer getPlaylistId() { return playlistId; }
    public void setPlaylistId(Integer playlistId) { this.playlistId = playlistId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public Boolean getIsAiGenerated() { return isAiGenerated; }
    public void setIsAiGenerated(Boolean isAiGenerated) { this.isAiGenerated = isAiGenerated; }

    public Boolean getIsSmart() { return isSmart; }
    public void setIsSmart(Boolean isSmart) { this.isSmart = isSmart; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<PlaylistMusic> getTracks() { return tracks; }
    public void setTracks(List<PlaylistMusic> tracks) { this.tracks = tracks; }
}