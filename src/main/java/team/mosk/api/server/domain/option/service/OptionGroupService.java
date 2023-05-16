package team.mosk.api.server.domain.option.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.mosk.api.server.domain.category.error.OwnerInfoMisMatchException;
import team.mosk.api.server.domain.option.dto.OptionGroupResponse;
import team.mosk.api.server.domain.option.dto.UpdateOptionGroupRequest;
import team.mosk.api.server.domain.option.error.OptionGroupNotFoundException;
import team.mosk.api.server.domain.option.model.persist.OptionGroup;
import team.mosk.api.server.domain.option.model.persist.OptionGroupRepository;
import team.mosk.api.server.domain.product.error.ProductNotFoundException;
import team.mosk.api.server.domain.product.model.persist.Product;
import team.mosk.api.server.domain.product.model.persist.ProductRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OptionGroupService {

    private final OptionGroupRepository optionGroupRepository;
    private final ProductRepository productRepository;

    private static final String PRODUCT_NOT_FOUND = "상품을 찾을 수 없습니다.";
    private static final String OWNER_INFO_MISMATCH = "상점의 주인이 아닙니다.";
    private static final String GROUP_NOT_FOUND = "그룹을 찾을 수 없습니다.";
    public OptionGroupResponse create(final OptionGroup optionGroup, final Long productId, final Long storeId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        validateOwnerInfo(findProduct.getStore().getId(), storeId);

        optionGroup.initProduct(findProduct);
        OptionGroup savedGroup = optionGroupRepository.save(optionGroup);

        return OptionGroupResponse.of(savedGroup);
    }

    public OptionGroupResponse update(final UpdateOptionGroupRequest request, final Long storeId) {
        OptionGroup findGroup = optionGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new OptionGroupNotFoundException(GROUP_NOT_FOUND));

        validateOwnerInfo(findGroup.getProduct().getStore().getId(), storeId);

        findGroup.update(request);

        return OptionGroupResponse.of(findGroup);
    }

    public void delete(final Long groupId, final Long storeId) {
        OptionGroup findGroup = optionGroupRepository.findById(groupId)
                .orElseThrow(() -> new OptionGroupNotFoundException(GROUP_NOT_FOUND));

        validateOwnerInfo(findGroup.getProduct().getStore().getId(), storeId);

        optionGroupRepository.delete(findGroup);
    }

    public void validateOwnerInfo(final Long storeId, final Long targetId) {
        if(!storeId.equals(targetId)) {
            throw new OwnerInfoMisMatchException(OWNER_INFO_MISMATCH);
        }
    }
}
