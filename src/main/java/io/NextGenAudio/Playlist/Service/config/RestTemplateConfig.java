package io.NextGenAudio.Playlist.Service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Force RestTemplate to use JDK HttpURLConnection instead of httpclient5
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // Configure connection timeouts
        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(30000);   // 30 seconds

        return new RestTemplate(factory);
    }
}
