package io.NextGenAudio.Playlist.Service.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "playlists")
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name= "name" ,nullable = false)
    private String name;

    @Column(name = "descripation")
    private String description;

    @Column(name = "is_ai_generated")
    private Boolean isAiGenerated = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "music_count")
    private Long musicCount;

    @Column(name = "playlist_art")
    private String playlistArt;

    private Boolean isPlaying = false;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_musics",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id")
    )
    @JsonIgnore
    private List<Music> musics;

    @ManyToMany
    @JoinTable(
            name = "playlist_users",
            joinColumns = @JoinColumn(name = "playlist_id")
    )
    private List<PlaylistUser> playlistUsers;


    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

}