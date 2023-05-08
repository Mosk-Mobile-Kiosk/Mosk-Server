package team.mosk.api.server.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

    @NotBlank(message = "accessToken은 필수 값입니다.")
    private String accessToken;

    public static AccessToken of(String accessToken) {
        return new AccessToken(accessToken);
    }
}
