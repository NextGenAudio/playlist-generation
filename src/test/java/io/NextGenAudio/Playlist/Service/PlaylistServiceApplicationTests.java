package io.NextGenAudio.Playlist.Service; // This package MUST match the one above

import io.NextGenAudio.Playlist.Service.repository.PlaylistMusicRepository;
import io.NextGenAudio.Playlist.Service.repository.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class PlaylistServiceApplicationTests {

    // Mock the repositories to prevent the test from needing a live database connection
    @MockBean
    private PlaylistRepository playlistRepository;

    @MockBean
    private PlaylistMusicRepository playlistMusicRepository;

    // Mock Kafka to prevent the test from needing a running Kafka broker
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void contextLoads() {
        // This test will now pass because it can find the @SpringBootApplication
        // and all external connections are mocked.
    }
}