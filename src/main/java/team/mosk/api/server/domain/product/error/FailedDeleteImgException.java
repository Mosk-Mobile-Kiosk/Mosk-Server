package team.mosk.api.server.domain.product.error;

public class FailedDeleteImgException extends RuntimeException{
    public FailedDeleteImgException(String message) {
        super(message);
    }
}
