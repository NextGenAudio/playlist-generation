package io.NextGenAudio.Playlist.Service.repository;

import io.NextGenAudio.Playlist.Service.dto.CollaboratorDTO;
import io.NextGenAudio.Playlist.Service.model.PlaylistUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistUserRepository extends JpaRepository<PlaylistUser, Long> {

    // Check if a user has access to a playlist
    boolean existsByPlaylistIdAndUserId(Long playlistId, String userId);

    // Check if a user is the owner of a playlist (role = 0)
    @Query("SELECT COUNT(pu) > 0 FROM PlaylistUser pu WHERE pu.playlistId = :playlistId AND pu.userId = :userId AND pu.role = 0")
    boolean isPlaylistOwner(@Param("playlistId") Long playlistId, @Param("userId") String userId);

    // Get user's role in a playlist
    @Query("SELECT pu FROM PlaylistUser pu WHERE pu.playlistId = :playlistId AND pu.userId = :userId")
    Optional<PlaylistUser> findByPlaylistIdAndUserId(@Param("playlistId") Long playlistId, @Param("userId") String userId);

    // Find the owner of a playlist
    @Query("SELECT pu FROM PlaylistUser pu WHERE pu.playlistId = :playlistId AND pu.role = 0")
    Optional<PlaylistUser> findOwnerByPlaylistId(@Param("playlistId") Long playlistId);

    // List all users with access to a playlist
    @Query("SELECT new io.NextGenAudio.Playlist.Service.dto.CollaboratorDTO(pu.userId, pu.role) FROM PlaylistUser pu WHERE pu.playlistId = :playlistId")
    List<CollaboratorDTO> findUserIdsByPlaylistId(@Param("playlistId") Long playlistId);
}
