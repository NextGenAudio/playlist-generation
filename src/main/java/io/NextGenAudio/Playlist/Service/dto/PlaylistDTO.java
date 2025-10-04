package io.NextGenAudio.Playlist.Service.dto;

public record PlaylistDTO(
        Long id,
        String name,
        String description,
        boolean isAiGenerated,
        Long musicCount,
        String playlistArt
) {}
