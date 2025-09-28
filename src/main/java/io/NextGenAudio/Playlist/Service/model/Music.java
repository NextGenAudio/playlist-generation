package io.NextGenAudio.Playlist.Service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
//import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity(name="Musics")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false, name="filename")
    private String filename;

    @Column(name="user_id", nullable = false)
    private String userId;

    @Column(name="path")
    private String path;

    @Column(name="uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name="album")
    private String album;

    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "is_liked")
    private boolean isLiked;

    @Column(name="metadata", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    @ManyToMany(mappedBy = "musics")
    @JsonIgnore
    private List<Playlist> playlists;
}

