package general;

public class ParseActorException extends Exception{

    /**
     * Exception if Actor has been split by more than 3 objects
     * @param message
     */
    public ParseActorException(String message){
        super(message);
    }
}
