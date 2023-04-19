package team.mosk.api.server.domain.store.dto;

import lombok.Getter;
import team.mosk.api.server.domain.store.model.persist.Store;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

@Getter
public class SignUpRequest {

    private String email;
    private String password;
    private String storeName;
    private String call;
    private String address;

    @AssertTrue
    private Boolean brs; // 사업자 등록 진위 여부 (Business Registration Status)

    public Store toEntity() {
        return Store.builder()
                .email(this.email)
                .password(this.password)
                .storeName(this.storeName)
                .call(this.call)
                .address(this.address)
                .build();
    }
}
