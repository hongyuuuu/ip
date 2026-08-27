import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/** Interprets the command structure of user input for Cookie. */
public class Parser {
    /** Parses date and time values that use the ISO date format. */
    private static final DateTimeFormatter ISO_DATE_TIME_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    /** Parses date and time values that use slash-separated dates. */
    private static final DateTimeFormatter SLASH_DATE_TIME_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    /** Parses date values that use the ISO date format. */
    private static final DateTimeFormatter ISO_DATE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    /** Parses date values that use slash-separated dates. */
    private static final DateTimeFormatter SLASH_DATE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    /** Parses time-only values. */
    private static final DateTimeFormatter TIME_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    /** Parses raw user input into a command and its arguments. */
    public ParsedCommand parse(String input) throws CookieException {
        String normalizedInput = input.trim();
        if (normalizedInput.isBlank()) {
            throw new CookieException("I couldn't understand an empty command.");
        }

        String[] parts = normalizedInput.split("\\s+");
        String action = parts[0];
        String description = normalizedInput.substring(action.length()).trim();
        return new ParsedCommand(Command.fromString(action), action, parts, description);
    }

    /** Ensures that a command has no arguments after its command word. */
    public void requireNoArguments(ParsedCommand command) throws CookieException {
        if (command.argumentCount() > 0) {
            throw new CookieException(
                    "The " + command.action() + " command does not take any arguments.");
        }
    }

    /** Ensures that a command has exactly one argument and reports its usage otherwise. */
    public void requireSingleArgument(ParsedCommand command, String usage) throws CookieException {
        if (command.argumentCount() != 1) {
            throw new CookieException("Usage: " + usage + ".");
        }
    }

    /** Returns a task description, rejecting commands with no description. */
    public String requireDescription(ParsedCommand command) throws CookieException {
        if (command.description().isBlank()) {
            throw new CookieException(
                    "A " + command.action() + " task needs a description.");
        }
        return command.description();
    }

    /** Parses a user-provided date, time, or date-time using supported formats. */
    public DateTimeValue parseDateTime(String value) throws CookieException {
        String normalizedValue = value.trim().replaceAll("\\s+", " ");
        DateTimeFormatter[] formats = {
            ISO_DATE_TIME_INPUT_FORMAT,
            SLASH_DATE_TIME_INPUT_FORMAT
        };

        for (DateTimeFormatter format : formats) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(normalizedValue, format);
                return new DateTimeValue(dateTime.toLocalDate(), dateTime.toLocalTime());
            } catch (DateTimeParseException exception) {
                // Try the next supported format.
            }
        }

        try {
            return new DateTimeValue(parseDate(normalizedValue), null);
        } catch (CookieException exception) {
            // Try the supported time-only format.
        }

        try {
            return new DateTimeValue(null, LocalTime.parse(normalizedValue, TIME_INPUT_FORMAT));
        } catch (DateTimeParseException exception) {
            throw new CookieException(
                    "A date, time, or date and time must use yyyy-MM-dd, d/M/yyyy, HHmm, "
                            + "yyyy-MM-dd HHmm, or d/M/yyyy HHmm.");
        }
    }

    /** Parses a user-provided date using one of the supported date formats. */
    public LocalDate parseDate(String value) throws CookieException {
        String normalizedValue = value.trim().replaceAll("\\s+", " ");
        DateTimeFormatter[] formats = {
            ISO_DATE_INPUT_FORMAT,
            SLASH_DATE_INPUT_FORMAT
        };

        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(normalizedValue, format);
            } catch (DateTimeParseException exception) {
                // Try the next supported format.
            }
        }
        throw new CookieException("A date must use yyyy-MM-dd or d/M/yyyy.");
    }

    /** Converts a one-based task number into a zero-based list index. */
    public int parseTaskIndex(ParsedCommand command, int taskCount) throws CookieException {
        if (command.argumentCount() != 1) {
            throw new CookieException("Usage: " + command.action() + " <task number>.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(command.argument(0));
        } catch (NumberFormatException exception) {
            throw new CookieException("The task number must be a positive whole number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new CookieException("There is no task numbered " + taskNumber + ".");
        }
        return taskNumber - 1;
    }

    /** Holds the command, original action word, arguments, and description from user input. */
    public static final class ParsedCommand {
        /** The command resolved from the user's action word. */
        private final Command command;

        /** The action word as entered by the user. */
        private final String action;

        /** The whitespace-separated words following the action word. */
        private final String[] parts;

        /** The text following the action word, preserving its internal spaces. */
        private final String description;

        private ParsedCommand(Command command, String action, String[] parts, String description) {
            this.command = command;
            this.action = action;
            this.parts = parts;
            this.description = description;
        }

        /** Returns the resolved command. */
        public Command command() {
            return command;
        }

        /** Returns the action word as entered by the user. */
        public String action() {
            return action;
        }

        /** Returns the number of whitespace-separated arguments. */
        public int argumentCount() {
            return parts.length - 1;
        }

        /** Returns the argument at the specified zero-based argument position. */
        public String argument(int index) {
            return parts[index + 1];
        }

        /** Returns the text following the action word. */
        public String description() {
            return description;
        }
    }
}
