package cookie.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import cookie.task.DateTimeValue;

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

    /** Creates a parser for Cookie commands. */
    public Parser() {
    }

    /**
     * Parses raw user input into a command and its arguments.
     *
     * @param input The raw user input to parse.
     * @return The parsed command and its arguments.
     * @throws CookieException If the input is blank or uses an unknown command.
     */
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

    /**
     * Ensures that a command has no arguments after its command word.
     *
     * @param command The parsed command to validate.
     * @throws CookieException If the command has one or more arguments.
     */
    public void requireNoArguments(ParsedCommand command) throws CookieException {
        if (command.argumentCount() > 0) {
            throw new CookieException(
                    "The " + command.action() + " command does not take any arguments.");
        }
    }

    /**
     * Ensures that a command has exactly one argument and reports its usage otherwise.
     *
     * @param command The parsed command to validate.
     * @param usage The usage message to display when validation fails.
     * @throws CookieException If the command does not have exactly one argument.
     */
    public void requireSingleArgument(ParsedCommand command, String usage) throws CookieException {
        if (command.argumentCount() != 1) {
            throw new CookieException("Usage: " + usage + ".");
        }
    }

    /**
     * Returns a task description, rejecting commands with no description.
     *
     * @param command The parsed command whose description is required.
     * @return The non-blank task description.
     * @throws CookieException If the command has no description.
     */
    public String requireDescription(ParsedCommand command) throws CookieException {
        if (command.description().isBlank()) {
            throw new CookieException(
                    "A " + command.action() + " task needs a description.");
        }
        return command.description();
    }

    /**
     * Rejects a value that would make the task file format ambiguous.
     *
     * @param value The task value to validate.
     * @return The validated value.
     * @throws CookieException If the value contains the task-file delimiter.
     */
    public String requireFileSafe(String value) throws CookieException {
        if (value.contains("|")) {
            throw new CookieException("Task details cannot contain '|'.");
        }
        return value;
    }

    /**
     * Parses a user-provided date, time, or date-time using supported formats.
     *
     * @param value The date, time, or date-time text to parse.
     * @return The parsed date, time, or date-time value.
     * @throws CookieException If the value does not use a supported format.
     */
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

    /**
     * Parses a user-provided date using one of the supported date formats.
     *
     * @param value The date text to parse.
     * @return The parsed date.
     * @throws CookieException If the value does not use a supported date format.
     */
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

    /**
     * Parses the description and date or time supplied for a deadline.
     *
     * @param description The raw description and deadline value to parse.
     * @return The parsed deadline.
     * @throws CookieException If the description or deadline value is malformed.
     */
    public ParsedDeadline parseDeadline(String description) throws CookieException {
        String[] deadlineParts = description.split("\\s+/by\\s+", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new CookieException(
                    "A deadline needs a description and a date and time after /by.");
        }

        String taskDescription = requireFileSafe(deadlineParts[0]);
        String deadlineValue = requireFileSafe(deadlineParts[1]);
        DateTimeValue deadlineDateTime;
        try {
            deadlineDateTime = parseDateTime(deadlineValue);
        } catch (CookieException exception) {
            throw new CookieException(
                    "A deadline date, time, or date and time must use yyyy-MM-dd, d/M/yyyy, "
                            + "HHmm, yyyy-MM-dd HHmm, or d/M/yyyy HHmm.");
        }
        return new ParsedDeadline(taskDescription, deadlineDateTime);
    }

    /**
     * Parses the description and time values supplied for an event.
     *
     * @param description The raw description and event values to parse.
     * @return The parsed event.
     * @throws CookieException If the description or event values are malformed.
     */
    public ParsedEvent parseEvent(String description) throws CookieException {
        String[] eventParts = description.split("\\s+/from\\s+", 2);
        if (eventParts.length < 2 || eventParts[0].isBlank()) {
            throw new CookieException(
                    "An event needs a description, a start time after /from, "
                            + "and an end time after /to.");
        }

        String[] timeParts = eventParts[1].split("\\s+/to\\s+", 2);
        if (timeParts.length < 2 || timeParts[0].isBlank() || timeParts[1].isBlank()) {
            throw new CookieException(
                    "An event needs a description, a start time after /from, "
                            + "and an end time after /to.");
        }

        String eventDescription = requireFileSafe(eventParts[0]);
        String startValue = requireFileSafe(timeParts[0]);
        String endValue = requireFileSafe(timeParts[1]);
        DateTimeValue start;
        DateTimeValue end;
        try {
            start = parseDateTime(startValue);
            end = parseDateTime(endValue);
        } catch (CookieException exception) {
            throw new CookieException(
                    "An event's start and end values must use yyyy-MM-dd, d/M/yyyy, HHmm, "
                            + "yyyy-MM-dd HHmm, or d/M/yyyy HHmm.");
        }
        return new ParsedEvent(eventDescription, start, end);
    }

    /**
     * Converts a one-based task number into a zero-based list index.
     *
     * @param command The parsed command containing the task number.
     * @param taskCount The number of tasks currently in the list.
     * @return The corresponding zero-based list index.
     * @throws CookieException If the task number is missing, invalid, or out of range.
     */
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

        /**
         * Returns the resolved command.
         *
         * @return The resolved command.
         */
        public Command command() {
            return command;
        }

        /**
         * Returns the action word as entered by the user.
         *
         * @return The original action word.
         */
        public String action() {
            return action;
        }

        /**
         * Returns the number of whitespace-separated arguments.
         *
         * @return The number of arguments.
         */
        public int argumentCount() {
            return parts.length - 1;
        }

        /**
         * Returns the argument at the specified zero-based argument position.
         *
         * @param index The zero-based argument position.
         * @return The argument at the specified position.
         */
        public String argument(int index) {
            return parts[index + 1];
        }

        /**
         * Returns the text following the action word.
         *
         * @return The command description.
         */
        public String description() {
            return description;
        }
    }

    /** Holds the parsed description and temporal value of a deadline command. */
    public static final class ParsedDeadline {
        /** The validated task description. */
        private final String description;

        /** The parsed deadline date or time. */
        private final DateTimeValue dateTime;

        private ParsedDeadline(String description, DateTimeValue dateTime) {
            this.description = description;
            this.dateTime = dateTime;
        }

        /**
         * Returns the validated task description.
         *
         * @return The task description.
         */
        public String description() {
            return description;
        }

        /**
         * Returns the parsed deadline date or time.
         *
         * @return The deadline date or time.
         */
        public DateTimeValue dateTime() {
            return dateTime;
        }
    }

    /** Holds the parsed description and temporal values of an event command. */
    public static final class ParsedEvent {
        /** The validated task description. */
        private final String description;

        /** The parsed event start date or time. */
        private final DateTimeValue start;

        /** The parsed event end date or time. */
        private final DateTimeValue end;

        private ParsedEvent(String description, DateTimeValue start, DateTimeValue end) {
            this.description = description;
            this.start = start;
            this.end = end;
        }

        /**
         * Returns the validated task description.
         *
         * @return The task description.
         */
        public String description() {
            return description;
        }

        /**
         * Returns the parsed event start date or time.
         *
         * @return The event start date or time.
         */
        public DateTimeValue start() {
            return start;
        }

        /**
         * Returns the parsed event end date or time.
         *
         * @return The event end date or time.
         */
        public DateTimeValue end() {
            return end;
        }
    }
}
