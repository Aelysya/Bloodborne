package bloodborne.exceptions;

public class MalFormedJsonException extends Exception{

    public MalFormedJsonException(String data){
        super(data);
    }
}
