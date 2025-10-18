    package io.NextGenAudio.Playlist.Service.model;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.OffsetDateTime;

    @Entity
    @Getter
    @Setter
    @Table(name = "playlist_musics")
    public class PlaylistMusic {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "playlist_id", nullable = false)
        @JsonBackReference
        private Playlist playlist;

        @Column(name = "music_id", nullable = false)
        private Long musicId;

        @Column
        private Integer position;

        @Column(name = "added_at")
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

    }