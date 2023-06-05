package team.mosk.api.server.domain.subscribe.error;

public class SubInfoNotFoundException extends RuntimeException {
    public SubInfoNotFoundException(String message) {
        super(message);
    }
}
