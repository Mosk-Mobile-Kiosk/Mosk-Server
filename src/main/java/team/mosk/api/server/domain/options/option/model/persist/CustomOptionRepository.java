package team.mosk.api.server.domain.options.option.model.persist;

import team.mosk.api.server.domain.options.option.dto.OptionResponse;

import java.util.List;

public interface CustomOptionRepository {

    List<OptionResponse> findAllOptionByGroupId(final Long groupId);
}
