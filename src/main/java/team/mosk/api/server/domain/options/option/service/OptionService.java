package team.mosk.api.server.domain.options.option.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.mosk.api.server.domain.category.error.OwnerInfoMisMatchException;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.option.dto.UpdateOptionRequest;
import team.mosk.api.server.domain.options.optionGroup.error.OptionGroupNotFoundException;
import team.mosk.api.server.domain.options.option.error.OptionNotFoundException;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroupRepository;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OptionService {

    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;

    private static final String OWNER_INFO_MISMATCH = "상점의 주인이 아닙니다.";
    private static final String GROUP_NOT_FOUND = "그룹을 찾을 수 없습니다.";
    private static final String OPTION_NOT_FOUND = "옵션을 찾을 수 없습니다.";

    public OptionResponse create(final Option option, final Long groupId, final Long storeId) {
        OptionGroup findGroup = optionGroupRepository.findById(groupId)
                .orElseThrow(() -> new OptionGroupNotFoundException(GROUP_NOT_FOUND));

        validateOwnerInfo(findGroup.getProduct().getStore().getId(), storeId);

        option.initGroup(findGroup);
        Option savedOption = optionRepository.save(option);
        findGroup.addOption(savedOption);

        return OptionResponse.of(savedOption);
    }

    public OptionResponse update(final UpdateOptionRequest request, final Long storeId) {
        Option findOption = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new OptionNotFoundException(OPTION_NOT_FOUND));

        validateOwnerInfo(findOption.getOptionGroup().getProduct().getStore().getId(), storeId);

        findOption.update(request);
        return OptionResponse.of(findOption);
    }

    public void delete(final Long optionId, final Long storeId) {
        Option findOption = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException(OPTION_NOT_FOUND));

        validateOwnerInfo(findOption.getOptionGroup().getProduct().getStore().getId(), storeId);

        optionRepository.delete(findOption);
    }

    public void validateOwnerInfo(final Long storeId, final Long targetId) {
        if(!storeId.equals(targetId)) {
            throw new OwnerInfoMisMatchException(OWNER_INFO_MISMATCH);
        }
    }
}
