package io.NextGenAudio.Playlist.Service.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class MusicBrief {
    private final Long id;
    private final String filename;
    private final String path;
    private final String title;
    private final String artist;
    private final String album;
    private final Map<String, Object> metadata;
    private final Integer duration;
    private final Long listenCount;
    private final boolean isLiked;

    // Constructor for repository queries
    public MusicBrief(Long id, String filename, String path, String title, String artist, String album, Map<String, Object> metadata, Long listenCount, boolean isLiked) {
        this.id = id;
        this.filename = filename;
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.metadata = metadata;
        this.listenCount = listenCount;
        this.isLiked = isLiked;

        // Safely extract track_length from metadata
        Object trackLengthObj = metadata != null ? metadata.get("track_length") : null;
        if (trackLengthObj instanceof Integer) {
            this.duration = (Integer) trackLengthObj;
        } else if (trackLengthObj instanceof Number) {
            this.duration = ((Number) trackLengthObj).intValue();
        } else {
            this.duration = null;
        }
    }
}
