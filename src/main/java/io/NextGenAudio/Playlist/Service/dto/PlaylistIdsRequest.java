package io.NextGenAudio.Playlist.Service.dto;

import java.util.List;

public class PlaylistIdsRequest {
    private List<Long> playlistIds;

    public PlaylistIdsRequest() {
    }

    public PlaylistIdsRequest(List<Long> playlistIds) {
        this.playlistIds = playlistIds;
    }

    public List<Long> getPlaylistIds() {
        return playlistIds;
    }

    public void setPlaylistIds(List<Long> playlistIds) {
        this.playlistIds = playlistIds;
    }
}

