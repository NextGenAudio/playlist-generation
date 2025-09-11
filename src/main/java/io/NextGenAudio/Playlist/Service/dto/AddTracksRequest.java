package io.NextGenAudio.Playlist.Service.dto;

import java.util.List;

public class AddTracksRequest {
    private List<Integer> fileIds;
    private Integer startPosition;
    private Boolean validateTracks = true;

    // Constructors
    public AddTracksRequest() {}

    public AddTracksRequest(List<Integer> fileIds) {
        this.fileIds = fileIds;
    }

    // Getters and Setters
    public List<Integer> getFileIds() { return fileIds; }
    public void setFileIds(List<Integer> fileIds) { this.fileIds = fileIds; }

    public Integer getStartPosition() { return startPosition; }
    public void setStartPosition(Integer startPosition) { this.startPosition = startPosition; }

    public Boolean getValidateTracks() { return validateTracks; }
    public void setValidateTracks(Boolean validateTracks) { this.validateTracks = validateTracks; }
}
