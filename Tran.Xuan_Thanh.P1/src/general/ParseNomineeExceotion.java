package general;

public class ParseNomineeExceotion extends Exception{

    /**
     * Exception if Nominee has been split by more than 5 objects
     * @param message
     */
    public ParseNomineeExceotion(String message){
        super(message);
    }
}
