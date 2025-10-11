package io.NextGenAudio.Playlist.Service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.NextGenAudio.Playlist.Service.model.Genre;
import io.NextGenAudio.Playlist.Service.model.Mood;
import io.NextGenAudio.Playlist.Service.model.Playlist;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MusicBrief {
    private final Long id;
    private final String filename;
    private final String path;
    private final String title;
    private final String artist;
    private final String album;
    private final Map<String, Object> metadata;
    private final Integer duration;
//    private final String genre;
//    private final String mood;
    private final Long listenCount;
//    private final LocalDateTime uploadedAt;
    private final boolean isLiked;

    public MusicBrief(Long id, String filename, String path, String title, String artist, String album, Map<String, Object> metadata , Long listenCount, boolean isLiked) {
        this.id = id;
        this.filename = filename;
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.metadata = metadata;
        this.listenCount = listenCount;
//        this.genre = genre.getGenre();
//        this.mood = mood.getMood();
        this.isLiked = isLiked;
//        this.uploadedAt = uploadedAt;

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

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public Integer getDuration() {
        return duration;
    }

//    public String getGenre() {
//        return genre;
//    }
//
//    public String getMood() {
//        return mood;
//    }

    public Long getListenCount() {
        return listenCount;
    }

//    public LocalDateTime getUploadedAt() {
//        return uploadedAt;
//    }

    public boolean isLiked() {
        return isLiked;
    }
}
