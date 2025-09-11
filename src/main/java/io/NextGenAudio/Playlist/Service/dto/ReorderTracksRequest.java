package io.NextGenAudio.Playlist.Service.dto;

import java.util.List;

public class ReorderTracksRequest {
    private List<TrackPosition> trackPositions;

    public static class TrackPosition {
        private Integer trackId;
        private Integer newPosition;

        public TrackPosition() {}

        public TrackPosition(Integer trackId, Integer newPosition) {
            this.trackId = trackId;
            this.newPosition = newPosition;
        }

        public Integer getTrackId() { return trackId; }
        public void setTrackId(Integer trackId) { this.trackId = trackId; }

        public Integer getNewPosition() { return newPosition; }
        public void setNewPosition(Integer newPosition) { this.newPosition = newPosition; }
    }

    // Constructors
    public ReorderTracksRequest() {}

    public ReorderTracksRequest(List<TrackPosition> trackPositions) {
        this.trackPositions = trackPositions;
    }

    // Getters and Setters
    public List<TrackPosition> getTrackPositions() { return trackPositions; }
    public void setTrackPositions(List<TrackPosition> trackPositions) { this.trackPositions = trackPositions; }
}
