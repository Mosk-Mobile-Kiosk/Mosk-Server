package team.mosk.api.server;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import team.mosk.api.server.domain.order.service.PaymentClient;
import team.mosk.api.server.domain.store.service.BusinessCheckClient;
import team.mosk.api.server.domain.store.service.QRCodeClient;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTestSupport {

    @MockBean
    protected PaymentClient paymentClient;

    @MockBean
    protected QRCodeClient qrCodeClient;

    @MockBean
    protected BusinessCheckClient businessCheckClient;


}
