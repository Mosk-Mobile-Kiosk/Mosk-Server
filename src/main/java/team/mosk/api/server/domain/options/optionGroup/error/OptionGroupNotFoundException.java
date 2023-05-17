package team.mosk.api.server.domain.options.optionGroup.error;

public class OptionGroupNotFoundException extends RuntimeException{
    public OptionGroupNotFoundException(String message) {
        super(message);
    }
}
