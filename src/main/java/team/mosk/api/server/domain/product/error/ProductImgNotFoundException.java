package team.mosk.api.server.domain.product.error;

public class ProductImgNotFoundException extends RuntimeException{
    public ProductImgNotFoundException(String message) {
        super(message);
    }
}
