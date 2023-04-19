package team.mosk.api.server.domain.auth.dto;

import lombok.Getter;

@Getter
public class SignInDto {

    private String email;
    private String password;
}
