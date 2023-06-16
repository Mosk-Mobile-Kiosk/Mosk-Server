package team.mosk.api.server.domain.options.option.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.option.error.OptionNotFoundException;
import team.mosk.api.server.domain.options.option.model.persist.Option;
import team.mosk.api.server.domain.options.option.model.persist.OptionRepository;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OptionReadService {

    private final OptionRepository optionRepository;

    public OptionResponse findByOptionId(final Long id) {
        Option findOption = optionRepository.findById(id)
                .orElseThrow(() -> new OptionNotFoundException(ErrorCode.OPTION_NOT_FOUND));
        return OptionResponse.of(findOption);
    }
}
