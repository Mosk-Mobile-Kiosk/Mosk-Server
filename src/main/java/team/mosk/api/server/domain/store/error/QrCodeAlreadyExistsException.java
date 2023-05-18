package team.mosk.api.server.domain.store.error;

public class QrCodeAlreadyExistsException extends RuntimeException {

    public QrCodeAlreadyExistsException(String message) {
        super(message);
    }
}
