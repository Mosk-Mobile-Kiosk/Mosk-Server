package team.mosk.api.server.domain.options.optionGroup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.mosk.api.server.domain.category.error.OwnerInfoMisMatchException;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.UpdateOptionGroupRequest;
import team.mosk.api.server.domain.options.optionGroup.error.OptionGroupNotFoundException;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroupRepository;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;
import team.mosk.api.server.global.error.exception.ErrorCode;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OptionGroupService {

    private final OptionGroupRepository optionGroupRepository;
    private final ProductRepository productRepository;
    public OptionGroupResponse create(final OptionGroup optionGroup, final Long productId, final Long storeId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        validateOwnerInfo(findProduct.getStore().getId(), storeId);

        optionGroup.initProduct(findProduct);
        OptionGroup savedGroup = optionGroupRepository.save(optionGroup);

        return OptionGroupResponse.of(savedGroup);
    }

    public OptionGroupResponse update(final UpdateOptionGroupRequest request, final Long storeId) {
        OptionGroup findGroup = optionGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new OptionGroupNotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND));

        validateOwnerInfo(findGroup.getProduct().getStore().getId(), storeId);

        findGroup.update(request);

        return OptionGroupResponse.of(findGroup);
    }

    public void delete(final Long groupId, final Long storeId) {
        OptionGroup findGroup = optionGroupRepository.findById(groupId)
                .orElseThrow(() -> new OptionGroupNotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND));

        validateOwnerInfo(findGroup.getProduct().getStore().getId(), storeId);

        optionGroupRepository.delete(findGroup);
    }

    public void validateOwnerInfo(final Long storeId, final Long targetId) {
        if(!storeId.equals(targetId)) {
            throw new OwnerInfoMisMatchException(ErrorCode.OWNER_INFO_MISMATCHED);
        }
    }
}
