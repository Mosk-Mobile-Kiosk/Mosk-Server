package team.mosk.api.server.domain.store.exception;

public class QRCodeNotFoundException extends RuntimeException {

    public QRCodeNotFoundException(String message) {
        super(message);
    }
}
