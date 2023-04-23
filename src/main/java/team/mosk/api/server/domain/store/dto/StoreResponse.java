package team.mosk.api.server.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {
    private Long id;
    private String email;
    private String storeName;
    private String ownerName;
    private String call;
    private String address;

    public static StoreResponse of(Store store) {
        return new StoreResponse(store.getId(),
                            store.getEmail(),
                            store.getStoreName(),
                            store.getOwnerName(),
                            store.getCall(),
                            store.getAddress());
    }
}
