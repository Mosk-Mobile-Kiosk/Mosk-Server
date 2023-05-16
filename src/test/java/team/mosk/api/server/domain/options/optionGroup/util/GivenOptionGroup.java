package team.mosk.api.server.domain.options.optionGroup.util;

import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;

public class GivenOptionGroup {
    public static final String GROUP_NAME = "테스트 그룹";

    public static final String MODIFIED_GROUP_NAME = "NEW 테스트 그룹";

    public static OptionGroup toEntity() {
        return OptionGroup.builder()
                .name(GROUP_NAME)
                .build();
    }

    public static OptionGroup toEntityWithCount() {
        return OptionGroup.builder()
                .id(1L)
                .name(GROUP_NAME)
                .build();
    }

    public static OptionGroup toEntityForUpdateTest() {
        return OptionGroup.builder()
                .id(1L)
                .name(MODIFIED_GROUP_NAME)
                .build();
    }
}
