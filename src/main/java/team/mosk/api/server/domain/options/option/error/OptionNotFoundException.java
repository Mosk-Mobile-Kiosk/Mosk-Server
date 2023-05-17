package team.mosk.api.server.domain.options.option.error;

public class OptionNotFoundException extends RuntimeException{
    public OptionNotFoundException(String message) {
        super(message);
    }
}
