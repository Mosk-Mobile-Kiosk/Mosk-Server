package team.mosk.api.server.domain.order.error;

public class OrderCompletedException extends RuntimeException {
    public OrderCompletedException(String message) {
        super(message);
    }
}
