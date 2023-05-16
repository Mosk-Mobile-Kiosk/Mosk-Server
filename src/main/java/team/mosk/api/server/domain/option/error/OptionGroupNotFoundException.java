package team.mosk.api.server.domain.option.error;

public class OptionGroupNotFoundException extends RuntimeException{
    public OptionGroupNotFoundException(String message) {
        super(message);
    }
}
