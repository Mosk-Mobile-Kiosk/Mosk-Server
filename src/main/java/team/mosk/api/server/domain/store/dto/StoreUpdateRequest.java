package team.mosk.api.server.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateRequest {
    private String storeName;
    private String ownerName;
    private String call;
    private String address;


}
