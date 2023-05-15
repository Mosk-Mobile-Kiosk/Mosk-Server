package team.mosk.api.server.domain.store.exception;

public class QrCodeAlreadyExistsException extends RuntimeException {

    public QrCodeAlreadyExistsException(String message) {
        super(message);
    }
}
