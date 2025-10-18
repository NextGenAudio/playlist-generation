package io.NextGenAudio.Playlist.Service.dto;

import java.time.OffsetDateTime;

public record PlaylistDTO(
        Long id,
        String name,
        String description,
        boolean isAiGenerated,
        Long musicCount,
        String playlistArt,
        OffsetDateTime createdAt,
        Integer role
) {}
