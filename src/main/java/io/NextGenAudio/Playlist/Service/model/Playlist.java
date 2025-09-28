package io.NextGenAudio.Playlist.Service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name= "name" ,nullable = false)
    private String name;

    @Column(name = "descripation")
    private String description;

    @Column(name = "is_ai_generated")
    private Boolean isAiGenerated = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_musics",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id")
    )
    @JsonIgnore
    private List<Music> musics;

    // Constructors
    public Playlist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public Playlist(String name, String userId) {
        this();
        this.name = name;
        this.userId = userId;
    }

    public Playlist(String name, String description, String userId, Boolean isPublic) {
        this();
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

}