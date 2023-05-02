package team.mosk.api.server.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.store.model.persist.Store;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String storeName;

    @NotBlank
    private String ownerName;

    @NotBlank
    private String call;

    @NotBlank
    private String address;

    @NotBlank
    private String crn; // 사업자 등록 번호

    public Store toEntity() {
        return Store.builder()
                .email(this.email)
                .password(this.password)
                .storeName(this.storeName)
                .ownerName(this.ownerName)
                .call(this.call)
                .address(this.address)
                .crn(this.crn)
                .build();
    }
}
