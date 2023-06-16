package team.mosk.api.server.domain.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team.mosk.api.server.ControllerIntegrationSupport;
import team.mosk.api.server.domain.category.dto.CreateCategoryRequest;
import team.mosk.api.server.domain.store.util.WithAuthUser;
import team.mosk.api.server.global.error.exception.ErrorCode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AspectAnnotationTest extends ControllerIntegrationSupport {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("만약 구독기간이 현재 시점보다 과거라면 예외를 발생시킨다.")
    @WithAuthUser(period = -1)
    void AspectAnnotationTest() throws Exception {
        CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name("Aspect Test")
                .build();

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJSON))
                .andExpect(jsonPath("$.status").value(ErrorCode.SUB_INFO_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.code").value(ErrorCode.SUB_INFO_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.SUB_INFO_NOT_FOUND.getMessage()))
                .andDo(print());
    }
}
