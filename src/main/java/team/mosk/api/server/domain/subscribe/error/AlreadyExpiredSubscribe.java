package team.mosk.api.server.domain.subscribe.error;

public class AlreadyExpiredSubscribe extends RuntimeException {
    public AlreadyExpiredSubscribe(String message) {
        super(message);
    }
}
