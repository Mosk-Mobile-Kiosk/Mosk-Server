package team.mosk.api.server.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class AccessToken {

    @NotBlank
    private String accessToken;

    public static AccessToken of(String accessToken) {
        return new AccessToken(accessToken);
    }
}
