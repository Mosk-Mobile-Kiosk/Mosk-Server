package team.mosk.api.server.domain.subscribe.dto;

import lombok.*;
import team.mosk.api.server.domain.subscribe.model.persist.Subscribe;
import team.mosk.api.server.domain.subscribe.model.persist.SubscribeHistory;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscribeResponse {

    private Long id;
    private Long storeId;
    private LocalDate startDate;
    private LocalDate endDate;

    public static SubscribeResponse of(final Subscribe subscribe) {
        return new SubscribeResponse(subscribe.getId(),
                subscribe.getStore().getId(),
                subscribe.getStartDate(),
                subscribe.getEndDate());
    };
}

