package io.NextGenAudio.Playlist.Service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "playlist_users")
@IdClass(PlaylistUserId.class)
public class PlaylistUser {
    @Id
    @Column(name = "playlist_id", nullable = false)
    private Long playlistId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "role")
    private Integer role; // 0 for owner, 1 for collaborator, etc.

    @Column(name = "added_at")
    private OffsetDateTime addedAt;

}
