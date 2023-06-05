package team.mosk.api.server.domain.subscribe.error;

public class AlreadyExpiredSubscribeException extends RuntimeException {
    public AlreadyExpiredSubscribeException(String message) {
        super(message);
    }
}
