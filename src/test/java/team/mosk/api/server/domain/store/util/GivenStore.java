package team.mosk.api.server.domain.store.util;

import team.mosk.api.server.domain.store.dto.SignUpRequest;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.model.persist.Store;

import static team.mosk.api.server.domain.auth.util.GivenAuth.GIVEN_EMAIL;
import static team.mosk.api.server.domain.auth.util.GivenAuth.GIVEN_PASSWORD;

public class GivenStore {

    public static final String GIVEN_STORENAME = "테스트 가게이름";

    public static final String GIVEN_OWNERNAME = "테스트 가게 주인 이름";
    public static final String GIVEN_CALL = "010-0000-0000";
    public static final String GIVEN_ADDRESS = "테스트 주소";

    public static final String GIVEN_CRN = "테스트 사업자등록번호";
    public static final String GIVEN_FOUNDED_DATE = "테스트 사업자등록번호";


    //QRCode
    public static final Long GIVEN_ID = 1L;
    public static final String GIVEN_PATH = "root/Document/test";

    public static StoreResponse givenStoreResponse(){
       return new StoreResponse(
               GIVEN_ID,
                GIVEN_EMAIL,
                GIVEN_STORENAME,
                GIVEN_OWNERNAME,
                GIVEN_CALL,
                GIVEN_ADDRESS);
    }

    public static SignUpRequest givenStoreSignUpRequest() {
        return new SignUpRequest(
                GIVEN_EMAIL,
                GIVEN_PASSWORD,
                GIVEN_STORENAME,
                GIVEN_OWNERNAME,
                GIVEN_CALL,
                GIVEN_ADDRESS,
                GIVEN_CRN);
    }

    public static StoreUpdateRequest givenStoreUpdateRequest() {
        return new StoreUpdateRequest(GIVEN_STORENAME, GIVEN_OWNERNAME, GIVEN_CALL, GIVEN_ADDRESS);
    }

    public static QRCode givenQRCode(String path) {
        Store store = Store.builder()
                .id(GIVEN_ID)
                .build();

        return QRCode.builder()
                .id(GIVEN_ID)
                .path(path)
                .store(store)
                .build();
    }
}
