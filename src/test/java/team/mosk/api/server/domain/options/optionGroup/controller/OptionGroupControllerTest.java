package team.mosk.api.server.domain.options.optionGroup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team.mosk.api.server.domain.options.optionGroup.dto.CreateOptionGroupRequest;
import team.mosk.api.server.domain.options.optionGroup.dto.OptionGroupResponse;
import team.mosk.api.server.domain.options.optionGroup.dto.UpdateOptionGroupRequest;
import team.mosk.api.server.domain.options.optionGroup.model.persist.OptionGroup;
import team.mosk.api.server.domain.options.optionGroup.service.OptionGroupReadService;
import team.mosk.api.server.domain.options.optionGroup.service.OptionGroupService;
import team.mosk.api.server.domain.options.optionGroup.util.GivenOptionGroup;
import team.mosk.api.server.domain.store.util.WithAuthUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("windows")
public class OptionGroupControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    OptionGroupService optionGroupService;
    @MockBean
    OptionGroupReadService optionGroupReadService;

    static final String NAME_IS_REQUIRED = "이름은 필수입니다.";
    static final String PRODUCT_INFO_IS_REQUIRED = "상품 정보는 필수입니다.";
    static final String GROUP_INFO_IS_REQUIRED = "그룹 아이디는 필수입니다.";


    /**
     * Create Test Methods
     */
    @Test
    @DisplayName("요청 내용에 따라 옵션 그룹을 생성하고 응답 객체를 반환한다.")
    @WithAuthUser
    void create() throws Exception {
        when(optionGroupService.create(any(), any(), any())).thenReturn(OptionGroupResponse.of(GivenOptionGroup.toEntityWithCount()));

        ObjectMapper mapper = new ObjectMapper();
        CreateOptionGroupRequest request = CreateOptionGroupRequest.builder().
                name(GivenOptionGroup.GROUP_NAME)
                .productId(1L)
                .build();

        String requestJSON = mapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/optiongroups")
                .content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(GivenOptionGroup.GROUP_NAME));
    }

    @Test
    @DisplayName("만약 생성 요청 중 이름이 비어있다면 예외를 발생시킨다.")
    @WithAuthUser
    void createThrowsExceptionWhenNameIsBlank() throws Exception {
        when(optionGroupService.create(any(), any(), any())).thenReturn(OptionGroupResponse.of(GivenOptionGroup.toEntityWithCount()));

        ObjectMapper mapper = new ObjectMapper();
        CreateOptionGroupRequest request = CreateOptionGroupRequest.builder()
                .productId(1L)
                .build();

        String requestJSON = mapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/optiongroups")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(NAME_IS_REQUIRED))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 생성 요청 중 상품 정보가 비어있다면 예외를 발생시킨다.")
    @WithAuthUser
    void createThrowsExceptionWhenProductIdIsNull() throws Exception {
        when(optionGroupService.create(any(), any(), any())).thenReturn(OptionGroupResponse.of(GivenOptionGroup.toEntityWithCount()));

        ObjectMapper mapper = new ObjectMapper();
        CreateOptionGroupRequest request = CreateOptionGroupRequest.builder()
                .name(GivenOptionGroup.GROUP_NAME)
                .build();

        String requestJSON = mapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/optiongroups")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(PRODUCT_INFO_IS_REQUIRED))
                .andDo(print());
    }

    /**
     * Create Test Methods
     */

    /**
     * Update Test Methods
     */
    @Test
    @DisplayName("요청 내용에 따라 옵션 그룹의 내용을 변경하고 응답 객체를 반환한다.")
    @WithAuthUser
    void update() throws Exception {
        when(optionGroupService.update(any(), any())).thenReturn(OptionGroupResponse.of(GivenOptionGroup.toEntityForUpdateTest()));

        ObjectMapper mapper = new ObjectMapper();
        UpdateOptionGroupRequest request = UpdateOptionGroupRequest.builder()
                .groupId(1L)
                .name(GivenOptionGroup.MODIFIED_GROUP_NAME)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/optiongroups")
                .content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(GivenOptionGroup.MODIFIED_GROUP_NAME))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 수정 요청 중 이름이 비어있다면 예외를 발생시킨다.")
    @WithAuthUser
    void updateThrowsExceptionWhenNameIsBlank() throws Exception {
        when(optionGroupService.update(any(), any())).thenReturn(OptionGroupResponse.of(GivenOptionGroup.toEntityForUpdateTest()));

        ObjectMapper mapper = new ObjectMapper();
        UpdateOptionGroupRequest request = UpdateOptionGroupRequest.builder()
                .groupId(1L)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/optiongroups")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(NAME_IS_REQUIRED))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 수정 요청 중 옵션 그룹의 정보가 없다면 예외를 발생시킨다.")
    @WithAuthUser
    void updateHasThrowExceptionWhenGroupInfoIsNull() throws Exception {
        when(optionGroupService.update(any(), any())).thenReturn(OptionGroupResponse.of(GivenOptionGroup.toEntityForUpdateTest()));

        ObjectMapper mapper = new ObjectMapper();
        UpdateOptionGroupRequest request = UpdateOptionGroupRequest.builder()
                .name(GivenOptionGroup.MODIFIED_GROUP_NAME)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/optiongroups")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(GROUP_INFO_IS_REQUIRED))
                .andDo(print());
    }

    /**
     * Update Test Methods
     */

    /**
     * Delete Test Methods
     */
    @Test
    @DisplayName("옵션 그룹 아이디를 기반으로 해당 옵션 그룹을 삭제한다.")
    @WithAuthUser
    void delete() throws Exception {
        doNothing().when(optionGroupService).delete(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/optiongroups/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.code").value(HttpStatus.NO_CONTENT.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NO_CONTENT.name()))
                .andDo(print());
    }

    /**
     * Delete Test Methods
     */

    /**
     * ReadService Methods
     */

    @Test
    @DisplayName("옵션 그룹 아이디를 기반으로 해당 옵션 그룹과 모든 옵션을 불러온다.")
    void findByGroupId() throws Exception {
        OptionGroup givenGroup = GivenOptionGroup.toEntityWithCount();

        when(optionGroupReadService.findByGroupId(any())).thenReturn(OptionGroupResponse.of(givenGroup));

        mockMvc.perform(get("/api/v1/public/optiongroups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.id").value(givenGroup.getId()))
                .andExpect(jsonPath("$.data.name").value(givenGroup.getName()))
                .andExpect(jsonPath("$.data.options[0].id").value(givenGroup.getOptions().get(0).getId()))
                .andExpect(jsonPath("$.data.options[0].name").value(givenGroup.getOptions().get(0).getName()))
                .andExpect(jsonPath("$.data.options[0].price").value(givenGroup.getOptions().get(0).getPrice()))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 아이디로 모든 옵션 그룹과 각 그룹별 해당하는 옵션 전부를 불러온다.")
    void findAllGroupAndOptionByProductId() throws Exception {
        List<OptionGroupResponse> response = new ArrayList<>();
        OptionGroup givenGroup1 = GivenOptionGroup.toEntityWithCount();
        response.add(OptionGroupResponse.of(givenGroup1));
        OptionGroup givenGroup2 = OptionGroup.builder()
                .id(2L)
                .name("Dummy")
                .options(givenGroup1.getOptions())
                .product(givenGroup1.getProduct())
                .build();
        response.add(OptionGroupResponse.of(givenGroup2));

        when(optionGroupReadService.findAllOptionGroupByProductId(any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/public/optiongroups/all/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data[0].id").value(givenGroup1.getId()))
                .andExpect(jsonPath("$.data[0].name").value(givenGroup1.getName()))
                .andExpect(jsonPath("$.data[0].options[0].id").value(givenGroup1.getOptions().get(0).getId()))
                .andExpect(jsonPath("$.data[0].options[0].name").value(givenGroup1.getOptions().get(0).getName()))
                .andExpect(jsonPath("$.data[0].options[0].price").value(givenGroup1.getOptions().get(0).getPrice()))
                .andExpect(jsonPath("$.data[1].id").value(givenGroup2.getId()))
                .andExpect(jsonPath("$.data[1].name").value(givenGroup2.getName()))
                .andExpect(jsonPath("$.data[1].options[0].id").value(givenGroup2.getOptions().get(0).getId()))
                .andExpect(jsonPath("$.data[1].options[0].name").value(givenGroup2.getOptions().get(0).getName()))
                .andExpect(jsonPath("$.data[1].options[0].price").value(givenGroup2.getOptions().get(0).getPrice()))
                .andDo(print());
    }
 }
