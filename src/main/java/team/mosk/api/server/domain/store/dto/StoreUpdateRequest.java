package team.mosk.api.server.domain.store.dto;

import lombok.Getter;

@Getter
public class StoreUpdateRequest {
    private String storeName;
    private String ownerName;
    private String call;
    private String address;
}
