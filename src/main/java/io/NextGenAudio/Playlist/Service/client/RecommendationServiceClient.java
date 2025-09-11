package io.NextGenAudio.Playlist.Service.client;

import io.NextGenAudio.Playlist.Service.dto.AIRecommendationRequest;
import io.NextGenAudio.Playlist.Service.dto.AIRecommendationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceClient {

    @Value("${recommendation.service.url:http://localhost:8081}")
    private String recommendationServiceUrl;

    private final RestTemplate restTemplate;

    public RecommendationServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Gets AI-powered track recommendations based on user preferences and history
     */
    public List<AIRecommendationResponse> getRecommendations(UUID userId, AIRecommendationRequest request) {
        try {
            String url = recommendationServiceUrl + "/api/recommendations/generate";

            // Create request payload
            RecommendationRequest payload = new RecommendationRequest();
            payload.setUserId(userId);
            payload.setGenre(request.getGenre());
            payload.setMood(request.getMood());
            payload.setMaxTracks(request.getMaxTracks());
            payload.setExcludeFileIds(request.getExcludeFileIds());
            payload.setIncludeUserHistory(request.getIncludeUserHistory());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RecommendationRequest> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<AIRecommendationResponse[]> response =
                restTemplate.postForEntity(url, entity, AIRecommendationResponse[].class);

            return List.of(response.getBody());
        } catch (Exception e) {
            System.err.println("Error getting recommendations from Recommendation Service: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Gets personalized playlist based on user's listening history and preferences
     */
    public List<AIRecommendationResponse> generatePersonalizedPlaylist(UUID userId, String genre, String mood, int trackCount) {
        try {
            String url = String.format("%s/api/recommendations/personalized?userId=%s&genre=%s&mood=%s&count=%d",
                recommendationServiceUrl, userId, genre, mood, trackCount);

            ResponseEntity<AIRecommendationResponse[]> response =
                restTemplate.getForEntity(url, AIRecommendationResponse[].class);

            return List.of(response.getBody());
        } catch (Exception e) {
            System.err.println("Error generating personalized playlist: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Updates user interaction data for better future recommendations
     */
    public void updateUserInteraction(UUID userId, Integer fileId, String interactionType, Double rating) {
        try {
            String url = recommendationServiceUrl + "/api/recommendations/interaction";

            UserInteraction interaction = new UserInteraction();
            interaction.setUserId(userId);
            interaction.setFileId(fileId);
            interaction.setInteractionType(interactionType);
            interaction.setRating(rating);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserInteraction> entity = new HttpEntity<>(interaction, headers);

            restTemplate.postForEntity(url, entity, Void.class);
        } catch (Exception e) {
            System.err.println("Error updating user interaction: " + e.getMessage());
        }
    }

    // Inner classes for request/response
    public static class RecommendationRequest {
        private UUID userId;
        private String genre;
        private String mood;
        private Integer maxTracks;
        private List<Integer> excludeFileIds;
        private Boolean includeUserHistory;

        // Getters and Setters
        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }

        public String getGenre() { return genre; }
        public void setGenre(String genre) { this.genre = genre; }

        public String getMood() { return mood; }
        public void setMood(String mood) { this.mood = mood; }

        public Integer getMaxTracks() { return maxTracks; }
        public void setMaxTracks(Integer maxTracks) { this.maxTracks = maxTracks; }

        public List<Integer> getExcludeFileIds() { return excludeFileIds; }
        public void setExcludeFileIds(List<Integer> excludeFileIds) { this.excludeFileIds = excludeFileIds; }

        public Boolean getIncludeUserHistory() { return includeUserHistory; }
        public void setIncludeUserHistory(Boolean includeUserHistory) { this.includeUserHistory = includeUserHistory; }
    }

    public static class UserInteraction {
        private UUID userId;
        private Integer fileId;
        private String interactionType; // "LIKED", "SKIPPED", "PLAYED", "ADDED_TO_PLAYLIST"
        private Double rating;

        // Getters and Setters
        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }

        public Integer getFileId() { return fileId; }
        public void setFileId(Integer fileId) { this.fileId = fileId; }

        public String getInteractionType() { return interactionType; }
        public void setInteractionType(String interactionType) { this.interactionType = interactionType; }

        public Double getRating() { return rating; }
        public void setRating(Double rating) { this.rating = rating; }
    }
}
