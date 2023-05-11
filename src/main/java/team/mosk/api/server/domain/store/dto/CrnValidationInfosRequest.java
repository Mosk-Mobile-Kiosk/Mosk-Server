package team.mosk.api.server.domain.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Getter
@NoArgsConstructor
public class CrnValidationInfosRequest {

    @NotBlank
    private String crn;

    @NotBlank
    private String foundedDate;

    @NotBlank
    private String ownerName;

}
