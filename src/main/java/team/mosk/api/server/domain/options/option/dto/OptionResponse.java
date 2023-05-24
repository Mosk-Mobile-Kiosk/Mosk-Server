package team.mosk.api.server.domain.options.option.dto;

import lombok.*;
import team.mosk.api.server.domain.options.option.model.persist.Option;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionResponse {

    private Long id;
    private String name;
    private long price;

    public static OptionResponse of(final Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getPrice());
    }
}
