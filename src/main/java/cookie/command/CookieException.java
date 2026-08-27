package cookie.command;

/** Represents an error caused by invalid input to Cookie. */
public class CookieException extends Exception {

    /** Creates an exception with a message that can be shown to the user. */
    public CookieException(String message) {
        super(message);
    }
}
