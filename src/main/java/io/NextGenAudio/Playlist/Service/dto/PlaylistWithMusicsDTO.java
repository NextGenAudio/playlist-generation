package io.NextGenAudio.Playlist.Service.dto;

import io.NextGenAudio.Playlist.Service.model.Music;

import java.util.List;

public record PlaylistWithMusicsDTO(
        Long id,
        String name,
        String description,
        boolean isAiGenerated,
        List<Music> musics
) {}