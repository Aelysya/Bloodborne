package bloodborne.exceptions;

public class UnknownExitTypeException extends Exception {
    public UnknownExitTypeException(String data) {
        super("Unknown exit type" + data);
    }
}
