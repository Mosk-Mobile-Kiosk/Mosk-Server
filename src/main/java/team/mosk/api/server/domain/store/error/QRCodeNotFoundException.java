package team.mosk.api.server.domain.store.error;

public class QRCodeNotFoundException extends RuntimeException {

    public QRCodeNotFoundException(String message) {
        super(message);
    }
}
