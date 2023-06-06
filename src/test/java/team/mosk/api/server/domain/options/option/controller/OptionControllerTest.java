package team.mosk.api.server.domain.options.option.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team.mosk.api.server.ControllerIntegrationSupport;
import team.mosk.api.server.domain.options.option.dto.CreateOptionRequest;
import team.mosk.api.server.domain.options.option.dto.OptionResponse;
import team.mosk.api.server.domain.options.option.dto.UpdateOptionRequest;
import team.mosk.api.server.domain.options.option.util.GivenOption;
import team.mosk.api.server.domain.store.util.WithAuthUser;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class OptionControllerTest extends ControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    static final String NAME_IS_REQUIRED = "이름은 필수입니다.";
    static final String PRICE_IS_REQUIRED = "가격은 필수입니다.";
    static final String GROUP_INFO_IS_REQUIRED = "그룹 정보는 필수입니다.";
    static final String OPTION_ID_IS_REQUIRED = "옵션 아이디는 필수입니다.";

    /**
     * Create Test Methods
     */

    @Test
    @DisplayName("생성 요청에 따라 옵션을 생성한다.")
    @WithAuthUser
    void create() throws Exception {
        when(optionService.create(any(), any(), any())).thenReturn(OptionResponse.of(GivenOption.toEntityWithCount()));
        ObjectMapper mapper = new ObjectMapper();

        CreateOptionRequest request = CreateOptionRequest.builder()
                .name(GivenOption.OPTION_NAME)
                .price(GivenOption.OPTION_PRICE)
                .optionGroupId(1L)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/options")
                .content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(GivenOption.OPTION_NAME))
                .andExpect(jsonPath("$.data.price").value(GivenOption.OPTION_PRICE))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 생성 요청 중 이름이 비어있다면 예외를 발생시킨다.")
    @WithAuthUser
    void createThrowsExceptionWhenNameIsBlank() throws Exception {
        when(optionService.create(any(), any(), any())).thenReturn(OptionResponse.of(GivenOption.toEntityWithCount()));
        ObjectMapper mapper = new ObjectMapper();

        CreateOptionRequest request = CreateOptionRequest.builder()
                .price(GivenOption.OPTION_PRICE)
                .optionGroupId(1L)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/options")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(NAME_IS_REQUIRED))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 생성 요청 중 가격이 비어있다면 예외를 발생시킨다.")
    @WithAuthUser
    void createThrowsExceptionWhenPriceIsNull() throws Exception {
        when(optionService.create(any(), any(), any())).thenReturn(OptionResponse.of(GivenOption.toEntityWithCount()));
        ObjectMapper mapper = new ObjectMapper();

        CreateOptionRequest request = CreateOptionRequest.builder()
                .name(GivenOption.OPTION_NAME)
                .optionGroupId(1L)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/options")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(PRICE_IS_REQUIRED))
                .andDo(print());
    }

    @Test
    @DisplayName("만약 생성 요청 중 옵션 그룹 정보가 비어있다면 예외를 발생시킨다.")
    @WithAuthUser
    void createThrowsExceptionWhenGroupInfoIsEmpty() throws Exception {
        when(optionService.create(any(), any(), any())).thenReturn(OptionResponse.of(GivenOption.toEntityWithCount()));
        ObjectMapper mapper = new ObjectMapper();

        CreateOptionRequest request = CreateOptionRequest.builder()
                .name(GivenOption.OPTION_NAME)
                .price(GivenOption.OPTION_PRICE)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/options")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(GROUP_INFO_IS_REQUIRED))
                .andDo(print());
    }

    /**
     * Create Test Methods
     */

    /**
     * Update Test Methods
     */
    @Test
    @DisplayName("요청 내용에 따라 옵션의 내용을 변경하고 응답 객체를 반환한다.")
    @WithAuthUser
    void update() throws Exception {
        when(optionService.update(any(), any())).thenReturn(OptionResponse.of(GivenOption.toEntityForUpdateTest()));

        ObjectMapper mapper = new ObjectMapper();
        UpdateOptionRequest request = UpdateOptionRequest.builder()
                .optionId(1L)
                .name(GivenOption.MODIFIED_OPTION_NAME)
                .price(GivenOption.MODIFIED_OPTION_PRICE)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/options")
                .content(requestJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(GivenOption.MODIFIED_OPTION_NAME))
                .andExpect(jsonPath("$.data.price").value(GivenOption.MODIFIED_OPTION_PRICE))
                .andDo(print());
    }

    @Test
    @DisplayName("수정 요청 간 옵션 아이디가 비어있다면 예외를 발생시킨다")
    @WithAuthUser
    void updateThrowsExceptionWhenOptionIdIsNull() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        UpdateOptionRequest request = UpdateOptionRequest.builder()
                .name(GivenOption.MODIFIED_OPTION_NAME)
                .price(GivenOption.MODIFIED_OPTION_PRICE)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/options")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(OPTION_ID_IS_REQUIRED))
                .andDo(print());
    }

    @Test
    @DisplayName("수정 요청 간 이름이 비어있다면 예외를 발생시킨다.")
    @WithAuthUser
    void updateThrowsExceptionWhenNameIsBlack() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        UpdateOptionRequest request = UpdateOptionRequest.builder()
                .optionId(1L)
                .price(GivenOption.MODIFIED_OPTION_PRICE)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/options")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(NAME_IS_REQUIRED))
                .andDo(print());
    }

    @Test
    @DisplayName("수정 요청 간 가격이 비어있다면 예외를 발생시킨다.")
    @WithAuthUser
    void updateThrowsExceptionWhenPriceIsNull() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        UpdateOptionRequest request = UpdateOptionRequest.builder()
                .optionId(1L)
                .name(GivenOption.MODIFIED_OPTION_NAME)
                .build();

        String requestJSON = mapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/options")
                        .content(requestJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(PRICE_IS_REQUIRED))
                .andDo(print());
    }

    /**
     * Update Test Methods
     */

    /**
     * Delete Test Methods
     */
    @Test
    @DisplayName("옵션 아이디를 기반으로 해당 옵션을 삭제한다.")
    @WithAuthUser
    void delete() throws Exception {
        doNothing().when(optionService).delete(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/options/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.code").value(HttpStatus.NO_CONTENT.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NO_CONTENT.name()))
                .andDo(print());

    }

    /**
     * Delete Test Methods
     */

    /**
     * Read Test Methods
     */

    @Test
    @DisplayName("옵션 아이디를 기반으로 해당 옵션의 정보를 조회한다.")
    void findOptionByOptionId() throws Exception {
        when(optionReadService.findByOptionId(any())).thenReturn(OptionResponse.of(GivenOption.toEntityWithCount()));

        mockMvc.perform(get("/api/v1/public/options/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(GivenOption.OPTION_NAME))
                .andExpect(jsonPath("$.data.price").value(GivenOption.OPTION_PRICE))
                .andDo(print());
    }
}
