package team.mosk.api.server.domain.store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import team.mosk.api.server.IntegrationTestSupport;
import team.mosk.api.server.domain.store.dto.StoreResponse;
import team.mosk.api.server.domain.store.dto.StoreUpdateRequest;
import team.mosk.api.server.domain.store.error.DuplicateCrnException;
import team.mosk.api.server.domain.store.error.DuplicateEmailException;
import team.mosk.api.server.domain.store.model.persist.QRCode;
import team.mosk.api.server.domain.store.model.persist.QRCodeRepository;
import team.mosk.api.server.domain.store.model.persist.Store;
import team.mosk.api.server.domain.store.model.persist.StoreRepository;
import team.mosk.api.server.global.error.exception.ErrorCode;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static team.mosk.api.server.domain.auth.util.GivenAuth.*;
import static team.mosk.api.server.domain.store.util.GivenStore.*;


class StoreServiceTest extends IntegrationTestSupport {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreReadService storeReadService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private QRCodeRepository qrCodeRepository;

    @Value("${filePath}")
    String filePath;

    @DisplayName("회원가입을 할 수 있다.")
    @Test
    void create() {
        //given
        Store store = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();

        //when
        StoreResponse storeResponse = storeService.create(store);

        //then
        assertThat(storeResponse)
                .extracting("email", "storeName", "ownerName", "call", "address")
                .contains(GIVEN_EMAIL, GIVEN_STORENAME, GIVEN_OWNERNAME, GIVEN_CALL, GIVEN_ADDRESS);
    }

    @DisplayName("회원가입 시 이미 등록된 이메일을 사용할 수 없다.")
    @Test
    void createWithDuplicateEmail() {
        //given
        Store store1 = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn("1111111")
                .build();

        storeRepository.save(store1);

        Store store2 = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn("2222222")
                .build();



        //when //then
        assertThatThrownBy(() -> storeService.create(store2))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage(ErrorCode.DUPLICATE_EMAIL.getMessage());
    }

    @DisplayName("회원가입 시 이미 등록된 사업자등록번호를 사용할 수 없다.")
    @Test
    void createWithDuplicateCrn() {
        //given
        Store store1 = Store.builder()
                .email("test111@mosk.com")
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();

        storeRepository.save(store1);

        Store store2 = Store.builder()
                .email("test222@mosk.com")
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();



        //when //then
        assertThatThrownBy(() -> storeService.create(store2))
                .isInstanceOf(DuplicateCrnException.class)
                .hasMessage(ErrorCode.DUPLICATE_CRN_NUMBER.getMessage());
    }

    @DisplayName("가게정보를 변경할 수 있다.")
    @Test
    void update() {
        //given
        Store store = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();

        Long storeId = storeRepository.save(store).getId();

        StoreUpdateRequest request = StoreUpdateRequest.builder()
                .storeName("updateStoreName")
                .ownerName("updateOwnerName")
                .call("010-1234-5678")
                .address("updateAddress")
                .build();

        //when
        storeService.update(storeId, request);

        //then
        Store savedStore = storeRepository.findById(storeId).get();
        assertThat(savedStore)
                .extracting("storeName", "ownerName", "call", "address")
                .contains("updateStoreName", "updateOwnerName", "010-1234-5678", "updateAddress");
    }

    @DisplayName("회원탈퇴를 할 수 있다.")
    @Test
    void delete() {
        //given
        Store store = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();

        Long storeId = storeRepository.save(store).getId();

        //when
        storeService.delete(storeId);

        //then
        Optional<Store> findStore = storeRepository.findById(storeId);
        assertThat(findStore).isEmpty();
    }

    @DisplayName("자신의 가게의 페이지로 이동하는 QRCode를 생성할 수 있다.")
    @Test
    void createQRCode() {
        //given
        Store store = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();

        Store savedStore = storeRepository.save(store);

        //when
        storeService.createQRCode(savedStore.getId());

        //then
        QRCode qrCode = qrCodeRepository.findAll().get(0);
        assertThat(qrCode.getStore()).isEqualTo(savedStore);
    }

    @DisplayName("가게 ID를 통해 가게 정보를 조회할 수 있다.")
    @Test
    void findById() {
        //given
        Store store = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();

        Store savedStore = storeRepository.save(store);

        //when
        StoreResponse findStore = storeReadService.findById(savedStore.getId());

        //then
        assertThat(findStore)
                .extracting("id", "email", "storeName", "ownerName", "call", "address")
                .contains(savedStore.getId(), GIVEN_EMAIL, GIVEN_STORENAME, GIVEN_OWNERNAME, GIVEN_CALL, GIVEN_ADDRESS);
    }

    @DisplayName("이메일 중복확인 시 가입되지 않은 이메일은 사용할 수 있다.")
    @Test
    void emailDuplicateCheck() {
        //given
        Store store = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();

        storeRepository.save(store);

        //when //then
        storeReadService.emailDuplicateCheck("test@mosk.com");
    }

    @DisplayName("이메일 중복확인 시 이미 사용중인 이메일은 사용할 수 없다.")
    @Test
    void emailDuplicateCheckWithDuplicateEmail() {
        //given
        Store store = Store.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .storeName(GIVEN_STORENAME)
                .ownerName(GIVEN_OWNERNAME)
                .call(GIVEN_CALL)
                .address(GIVEN_ADDRESS)
                .crn(GIVEN_CRN)
                .build();

        storeRepository.save(store);

        //when //then
        assertThatThrownBy(() -> storeReadService.emailDuplicateCheck(GIVEN_EMAIL))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage(ErrorCode.DUPLICATE_EMAIL.getMessage());
    }




}