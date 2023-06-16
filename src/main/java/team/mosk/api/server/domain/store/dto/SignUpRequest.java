package team.mosk.api.server.domain.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.store.model.persist.Store;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "이메일 형식이여야 합니다.")
    @NotBlank(message = "이메일은 필수 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;

    @NotBlank(message = "가게이름은 필수 값입니다.")
    private String storeName;

    @NotBlank(message = "사업주이름은 필수 값입니다.")
    private String ownerName;

    @NotBlank(message = "휴대폰 번호는 필수 값입니다.")
    private String call;

    @NotBlank(message = "주소는 필수 값입니다.")
    private String address;

    @NotBlank(message = "사업자등록 번호는 필수 값입니다.")
    private String crn; // 사업자 등록 번호

    @Builder
    public SignUpRequest(String email, String password, String storeName, String ownerName, String call, String address, String crn) {
        this.email = email;
        this.password = password;
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.call = call;
        this.address = address;
        this.crn = crn;
    }

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
