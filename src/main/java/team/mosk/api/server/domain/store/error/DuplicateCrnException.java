package team.mosk.api.server.domain.store.error;

public class DuplicateCrnException extends RuntimeException{
    public DuplicateCrnException(String message) {
        super(message);
    }
}
