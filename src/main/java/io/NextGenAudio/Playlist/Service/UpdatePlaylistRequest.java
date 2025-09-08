package io.NextGenAudio.Playlist.Service;

public class UpdatePlaylistRequest {
    private String name;
    private String description;

    // Manual getter and setter methods (fallback for Lombok issues)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
