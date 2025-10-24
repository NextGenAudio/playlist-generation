package io.NextGenAudio.Playlist.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuggestedPlaylistDTO {
    private String playlistName;
    private String mood;
    private String genre;
    private boolean isAiGenerated;
}