package io.NextGenAudio.Playlist.Service.dto;

public class AddTrackRequest {
    private Integer fileId;
    private Integer position; // Optional: position to insert the track

    // Manual getter and setter methods (fallback for Lombok issues)
    public Integer getFileId() { return fileId; }
    public void setFileId(Integer fileId) { this.fileId = fileId; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}