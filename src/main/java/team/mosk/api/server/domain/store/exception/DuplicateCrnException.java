package team.mosk.api.server.domain.store.exception;

public class DuplicateCrnException extends RuntimeException{
    public DuplicateCrnException(String message) {
        super(message);
    }
}
