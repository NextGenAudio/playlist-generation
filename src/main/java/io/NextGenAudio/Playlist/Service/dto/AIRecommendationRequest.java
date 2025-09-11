package io.NextGenAudio.Playlist.Service.dto;

import java.util.List;

public class AIRecommendationRequest {
    private String genre;
    private String mood;
    private Integer maxTracks = 20;
    private List<Integer> excludeFileIds;
    private Boolean includeUserHistory = true;

    // Constructors
    public AIRecommendationRequest() {}

    public AIRecommendationRequest(String genre, String mood) {
        this.genre = genre;
        this.mood = mood;
    }

    // Getters and Setters
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public Integer getMaxTracks() { return maxTracks; }
    public void setMaxTracks(Integer maxTracks) { this.maxTracks = maxTracks; }

    public List<Integer> getExcludeFileIds() { return excludeFileIds; }
    public void setExcludeFileIds(List<Integer> excludeFileIds) { this.excludeFileIds = excludeFileIds; }

    public Boolean getIncludeUserHistory() { return includeUserHistory; }
    public void setIncludeUserHistory(Boolean includeUserHistory) { this.includeUserHistory = includeUserHistory; }
}
