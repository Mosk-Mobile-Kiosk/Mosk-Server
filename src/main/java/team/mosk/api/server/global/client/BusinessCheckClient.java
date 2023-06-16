package team.mosk.api.server.global.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import team.mosk.api.server.domain.store.dto.BusinessCheckRequest;
import team.mosk.api.server.domain.store.dto.BusinessCheckResponse;
import team.mosk.api.server.domain.store.error.DuplicateCrnException;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.util.ArrayList;

@Service
public class BusinessCheckClient {

    private final WebClient webClient;

    public BusinessCheckClient(@Qualifier("gongGongDataClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public void callBusinessRegistrationCheck(ArrayList<BusinessCheckRequest> bcrList) {
        ResponseEntity<BusinessCheckResponse> response = webClient.post()
                .bodyValue(bcrList)
                .retrieve()
                .toEntity(BusinessCheckResponse.class)
                .block();

        if (response.getStatusCode() != HttpStatus.OK || !isBusinessCheck(response.getBody().getValid_msg())) {
            throw new DuplicateCrnException(ErrorCode.DUPLICATE_CRN_NUMBER);
        }
    }

    private boolean isBusinessCheck(String resultMsg) {
        return resultMsg == "" ? true : false;
    }
}
