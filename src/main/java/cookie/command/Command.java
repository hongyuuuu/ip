package cookie.command;

/** Represents the valid commands for the Cookie application. */
public enum Command {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    ON,
    TODO,
    DEADLINE,
    EVENT;

    /** Converts a string action into a Command enum
     * @throws CookieException If the command is unrecognized.
     */
    public static Command fromString(String action) throws CookieException {
        try {
            return Command.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CookieException("What is that command!?");
        }
    }
}
