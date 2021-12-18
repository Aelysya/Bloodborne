package bloodborne.exceptions;

public class UnknownPlaceException extends Exception {
    public UnknownPlaceException(String data) {
        super("Unknown place" + data);
    }
}
