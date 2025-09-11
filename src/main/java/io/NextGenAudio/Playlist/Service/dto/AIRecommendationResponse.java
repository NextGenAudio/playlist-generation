package io.NextGenAudio.Playlist.Service.dto;

public class AIRecommendationResponse {
    private Integer fileId;
    private String title;
    private String artist;
    private String album;
    private Integer duration;
    private String genre;
    private String mood;
    private Double confidenceScore;
    private String recommendationReason;

    // Constructors
    public AIRecommendationResponse() {}

    public AIRecommendationResponse(Integer fileId, String title, String artist, Double confidenceScore, String recommendationReason) {
        this.fileId = fileId;
        this.title = title;
        this.artist = artist;
        this.confidenceScore = confidenceScore;
        this.recommendationReason = recommendationReason;
    }

    // Getters and Setters
    public Integer getFileId() { return fileId; }
    public void setFileId(Integer fileId) { this.fileId = fileId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }
}
