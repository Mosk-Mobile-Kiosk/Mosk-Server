package team.mosk.api.server.domain.options.optionGroup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.options.optionGroup.error.OptionGroupNotFoundException;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroupRepository;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OptionGroupReadService {

    private final OptionGroupRepository optionGroupRepository;
    public OptionGroupResponse findByGroupId(final Long id) {
        OptionGroup findGroup = optionGroupRepository.findById(id)
                .orElseThrow(() -> new OptionGroupNotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND));

        return OptionGroupResponse.of(findGroup);
    }

    public List<OptionGroupResponse> findAllOptionGroupByProductId(final Long productId) {
        return optionGroupRepository.findAllOptionGroupByProductId(productId);
    }
}
