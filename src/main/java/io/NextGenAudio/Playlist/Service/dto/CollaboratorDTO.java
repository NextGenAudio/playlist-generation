package io.NextGenAudio.Playlist.Service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollaboratorDTO {
    private String userId;
    private Integer role;

    public CollaboratorDTO(String userId, Integer role) {
        this.userId = userId;
        this.role = role;
    }
}
