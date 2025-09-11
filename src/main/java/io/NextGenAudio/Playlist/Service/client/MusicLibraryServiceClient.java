package io.NextGenAudio.Playlist.Service.client;

import io.NextGenAudio.Playlist.Service.dto.AIRecommendationRequest;
import io.NextGenAudio.Playlist.Service.dto.AIRecommendationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Service
public class MusicLibraryServiceClient {

    @Value("${music.library.service.url:http://localhost:8080}")
    private String musicLibraryServiceUrl;

    private final RestTemplate restTemplate;

    public MusicLibraryServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Validates if a track exists in the Music Library Service
     */
    public boolean validateTrackExists(Integer fileId) {
        try {
            String url = musicLibraryServiceUrl + "/files/" + fileId + "/validate";
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            // Log the error and return false for safety
            System.err.println("Error validating track " + fileId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets track metadata from the Music Library Service
     */
    public TrackMetadata getTrackMetadata(Integer fileId) {
        try {
            String url = musicLibraryServiceUrl + "/files/" + fileId + "/metadata";
            ResponseEntity<TrackMetadata> response = restTemplate.getForEntity(url, TrackMetadata.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error fetching metadata for track " + fileId + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets tracks by genre and mood for AI recommendations
     */
    public TrackMetadata[] getTracksByGenreAndMood(String genre, String mood, int limit) {
        try {
            String url = String.format("%s/files/search?genre=%s&mood=%s&limit=%d",
                musicLibraryServiceUrl, genre, mood, limit);
            ResponseEntity<TrackMetadata[]> response = restTemplate.getForEntity(url, TrackMetadata[].class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error fetching tracks by genre/mood: " + e.getMessage());
            return new TrackMetadata[0];
        }
    }

    /**
     * Inner class for track metadata
     */
    public static class TrackMetadata {
        private Integer fileId;
        private String title;
        private String artist;
        private String album;
        private Integer duration;
        private String genre;
        private String mood;

        // Constructors
        public TrackMetadata() {}

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
    }
}
