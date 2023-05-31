package team.mosk.api.server;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import team.mosk.api.server.domain.auth.service.AuthService;
import team.mosk.api.server.domain.category.service.CategoryReadService;
import team.mosk.api.server.domain.category.service.CategoryService;
import team.mosk.api.server.domain.options.option.service.OptionReadService;
import team.mosk.api.server.domain.options.option.service.OptionService;
import team.mosk.api.server.domain.options.optionGroup.service.OptionGroupReadService;
import team.mosk.api.server.domain.options.optionGroup.service.OptionGroupService;
import team.mosk.api.server.domain.order.service.OrderService;
import team.mosk.api.server.domain.product.service.ProductReadService;
import team.mosk.api.server.domain.product.service.ProductService;
import team.mosk.api.server.domain.store.service.StoreReadService;
import team.mosk.api.server.domain.store.service.StoreService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class ControllerIntegrationSupport {

    @MockBean
    protected AuthService authService;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected CategoryReadService categoryReadService;

    @MockBean
    protected OptionService optionService;

    @MockBean
    protected OptionReadService optionReadService;

    @MockBean
    protected OptionGroupService optionGroupService;

    @MockBean
    protected OptionGroupReadService optionGroupReadService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected ProductReadService productReadService;

    @MockBean
    protected StoreService storeService;

    @MockBean
    protected StoreReadService storeReadService;

}
