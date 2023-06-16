package team.mosk.api.server.domain.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class StoreUpdateRequest {

    @NotBlank(message = "가게이름은 필수 값입니다.")
    private String storeName;

    @NotBlank(message = "사업주이름은 필수 값입니다.")
    private String ownerName;

    @NotBlank(message = "휴대폰 번호는 필수 값입니다.")
    private String call;

    @NotBlank(message = "주소는 필수 값입니다.")
    private String address;


    @Builder
    public StoreUpdateRequest(String storeName, String ownerName, String call, String address) {
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.call = call;
        this.address = address;
    }
}
