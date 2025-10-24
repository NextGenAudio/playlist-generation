package io.NextGenAudio.Playlist.Service.service;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlaylistNameMapper {

    private static final Map<String, String> playlistNameMap = new ConcurrentHashMap<>();

    // Static block to initialize the map with your provided data
    static {
        playlistNameMap.put("happy:blues", "Sunny Day Blues");
        playlistNameMap.put("happy:classical", "Joyful Symphonies");
        playlistNameMap.put("happy:country", "Golden Country Smiles");
        playlistNameMap.put("happy:disco", "Dancing Lights");
        playlistNameMap.put("happy:hiphop", "Feel Good Flow");
        playlistNameMap.put("happy:jazz", "Cheerful Swing");
        playlistNameMap.put("happy:metal", "Bright Metal Energy");
        playlistNameMap.put("happy:pop", "Happy Vibes Pop");
        playlistNameMap.put("happy:reggae", "Sunshine Reggae");
        playlistNameMap.put("happy:rock", "Rock the Smile");

        playlistNameMap.put("sad:blues", "Midnight Blues");
        playlistNameMap.put("sad:classical", "Tearful Symphonies");
        playlistNameMap.put("sad:country", "Heartbreak Highway");
        playlistNameMap.put("sad:disco", "Lost on the Dancefloor");
        playlistNameMap.put("sad:hiphop", "Soul Pain Flow");
        playlistNameMap.put("sad:jazz", "Melancholy Nights");
        playlistNameMap.put("sad:metal", "Fallen Echoes");
        playlistNameMap.put("sad:pop", "Crying in Pop");
        playlistNameMap.put("sad:reggae", "Rainy Reggae");
        playlistNameMap.put("sad:rock", "Broken Strings");

        playlistNameMap.put("relax:blues", "Smooth Blues Chill");
        playlistNameMap.put("relax:classical", "Peaceful Piano Moments");
        playlistNameMap.put("relax:country", "Calm Countryside");
        playlistNameMap.put("relax:disco", "Soft Disco Glow");
        playlistNameMap.put("relax:hiphop", "Lo-Fi Hiphop Dreams");
        playlistNameMap.put("relax:jazz", "Late Night Jazz");
        playlistNameMap.put("relax:metal", "Mellow Metal Waves");
        playlistNameMap.put("relax:pop", "Soft Pop Breeze");
        playlistNameMap.put("relax:reggae", "Smooth Reggae Flow");
        playlistNameMap.put("relax:rock", "Easy Rock Nights");

        playlistNameMap.put("aggressive:blues", "Raging Blues Fire");
        playlistNameMap.put("aggressive:classical", "Epic Symphonic Fury");
        playlistNameMap.put("aggressive:country", "Wild Rodeo Spirit");
        playlistNameMap.put("aggressive:disco", "Frenzy Beats");
        playlistNameMap.put("aggressive:hiphop", "Hardcore Flow");
        playlistNameMap.put("aggressive:jazz", "Chaotic Jazz Energy");
        playlistNameMap.put("aggressive:metal", "Metal Rage");
        playlistNameMap.put("aggressive:pop", "Power Pop Storm");
        playlistNameMap.put("aggressive:reggae", "Rebel Reggae");
        playlistNameMap.put("aggressive:rock", "Rock Storm");

        playlistNameMap.put("dramatic:blues", "Deep Blues Drama");
        playlistNameMap.put("dramatic:classical", "Cinematic Classical");
        playlistNameMap.put("dramatic:country", "Tragic Tales of Country");
        playlistNameMap.put("dramatic:disco", "Retro Drama Lights");
        playlistNameMap.put("dramatic:hiphop", "Intense Hiphop Tales");
        playlistNameMap.put("dramatic:jazz", "Emotive Jazz Stories");
        playlistNameMap.put("dramatic:metal", "Epic Metal Symphony");
        playlistNameMap.put("dramatic:pop", "Soulful Pop Stories");
        playlistNameMap.put("dramatic:reggae", "Heavy Heart Reggae");
        playlistNameMap.put("dramatic:rock", "Emotional Rock Ballads");
    }

    /**
     * Gets the playlist name for a given mood and genre.
     * @param mood The mood (e.g., "happy")
     * @param genre The genre (e.g., "blues")
     * @return The matching playlist name, or a default name if not found.
     */
    public String getPlaylistName(String mood, String genre) {
        String key = mood + ":" + genre;
        // Return the mapped name, or a generated default if it's not in your list
        return playlistNameMap.getOrDefault(key, mood + " " + genre);
    }
}