package team.mosk.api.server.domain.store.dto;

import lombok.*;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

@Getter
@NoArgsConstructor
public class StoreResponse {

    private Long id;
    private String email;
    private String storeName;
    private String ownerName;
    private String call;
    private String address;

    @Builder
    public StoreResponse(Long id, String email, String storeName, String ownerName, String call, String address) {
        this.id = id;
        this.email = email;
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.call = call;
        this.address = address;
    }

    public static StoreResponse of(Store store) {
        return new StoreResponse(
                        store.getId(),
                        store.getEmail(),
                        store.getStoreName(),
                        store.getOwnerName(),
                        store.getCall(),
                        store.getAddress());
    }
}
