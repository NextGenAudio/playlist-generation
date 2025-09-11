package io.NextGenAudio.Playlist.Service.dto;

public class GenerateAiPlaylistRequest {
    private String name;
    private String mood;
    private String genre;
    private Integer trackCount;

    // Constructors
    public GenerateAiPlaylistRequest() {}

    public GenerateAiPlaylistRequest(String name, String mood, String genre, Integer trackCount) {
        this.name = name;
        this.mood = mood;
        this.genre = genre;
        this.trackCount = trackCount;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getTrackCount() { return trackCount; }
    public void setTrackCount(Integer trackCount) { this.trackCount = trackCount; }
}