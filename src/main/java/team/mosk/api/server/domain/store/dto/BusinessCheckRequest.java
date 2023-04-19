package team.mosk.api.server.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessCheckRequest {

    private String crn;
    private String ownerName;
    private LocalDate foundedDate;
}
