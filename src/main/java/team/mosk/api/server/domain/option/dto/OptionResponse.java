package team.mosk.api.server.domain.option.dto;

import lombok.*;
import team.mosk.api.server.domain.option.model.persist.Option;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionResponse {

    private Long optionId;
    private String name;
    private int price;

    public static OptionResponse of(final Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getPrice());
    }
}
