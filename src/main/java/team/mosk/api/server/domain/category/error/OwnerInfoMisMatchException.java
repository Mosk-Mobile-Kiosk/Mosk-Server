package team.mosk.api.server.domain.category.error;

public class OwnerInfoMisMatchException extends RuntimeException {
    public OwnerInfoMisMatchException(String message) {
        super(message);
    }
}
