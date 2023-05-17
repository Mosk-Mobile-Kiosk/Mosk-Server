package team.mosk.api.server.domain.options.optionGroup.model.persist;

import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;

import java.util.List;

public interface CustomOptionGroupRepository {
    List<OptionGroupResponse> findAllOptionGroup();
}
