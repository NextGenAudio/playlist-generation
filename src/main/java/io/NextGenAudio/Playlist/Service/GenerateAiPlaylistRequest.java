package io.NextGenAudio.Playlist.Service;

public class GenerateAiPlaylistRequest {
    private String mood;
    private String genre;

    // Manual getter and setter methods (fallback for Lombok issues)
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}