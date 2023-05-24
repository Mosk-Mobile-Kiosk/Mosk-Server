package team.mosk.api.server.domain.order.error;

public class OrdeCancelDeniedException extends RuntimeException {
    public OrdeCancelDeniedException(String message) {
        super(message);
    }
}
