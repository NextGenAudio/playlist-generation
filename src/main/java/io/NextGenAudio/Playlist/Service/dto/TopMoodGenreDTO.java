package io.NextGenAudio.Playlist.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopMoodGenreDTO {
    private String mood;
    private String genre;
    private Double averageFavorScore; // This is the AVG(x_score)
}