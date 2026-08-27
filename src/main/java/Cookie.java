import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Scanner;

import java.io.IOException;

/** The main entry point for the Cookie command-line application. */
public class Cookie {
    private static TaskList LST = new TaskList();
    private static final Ui UI = new Ui();
    private static final Storage STORAGE = new Storage("./data/cookie.txt");
    private static final Parser PARSER = new Parser();
    private static final DateTimeFormatter ISO_DATE_TIME_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter SLASH_DATE_TIME_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter ISO_DATE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter SLASH_DATE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);
    /** Stores user input and prints the message indicating a successful addition */
    private static void addTask(Task task) {
        LST.add(task);
        if (!saveTasks()) {
            LST.remove(LST.size() - 1);
            return;
        }
        UI.showTaskAdded(task, LST.size());
    }

    /** Displays the list of items added by users when users enter {@code list} */
    private static void list() {
        UI.showTaskList(LST);
    }

    /** Marks task as done and prints message indicating a successful mark as done */
    private static void markTask(int idx) {
        Task task = LST.get(idx);
        boolean wasDone = task.isDone;
        task.mark();
        if (!saveTasks()) {
            if (wasDone) {
                task.mark();
            } else {
                task.unmark();
            }
            return;
        }
        UI.showTaskMarked(task);
    }

    /** Unmarks task as done and prints message indicating a successful unmark as done */
    private static void unmarkTask(int idx) {
        Task task = LST.get(idx);
        boolean wasDone = task.isDone;
        task.unmark();
        if (!saveTasks()) {
            if (wasDone) {
                task.mark();
            } else {
                task.unmark();
            }
            return;
        }
        UI.showTaskUnmarked(task);
    }

    /** Deletes the selected task and reports the removed task and remaining task count. */
    private static void deleteTask(int idx) {
        Task task = LST.get(idx);
        LST.remove(idx);
        if (!saveTasks()) {
            LST.add(idx, task);
            return;
        }
        UI.showTaskDeleted(task, LST.size());
    }

    /** Rejects a value that would make the task file format ambiguous. */
    private static String requireFileSafe(String value) throws CookieException {
        if (value.contains("|")) {
            throw new CookieException("Task details cannot contain '|'.");
        }
        return value;
    }

    /** Parses a user-provided date, time, or date-time using supported formats. */
    private static DateTimeValue parseDateTime(String value) throws CookieException {
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
    private static LocalDate parseDate(String value) throws CookieException {
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

    /** Adds a deadline after validating its description, marker, and date or time. */
    private static void addDeadline(String description) throws CookieException {
        String[] deadlineParts = description.split("\\s+/by\\s+", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new CookieException("A deadline needs a description and a date and time after /by.");
        }
        String deadlineValue = requireFileSafe(deadlineParts[1]);
        DateTimeValue deadlineDateTime;
        try {
            deadlineDateTime = parseDateTime(deadlineValue);
        } catch (CookieException exception) {
            throw new CookieException(
                    "A deadline date, time, or date and time must use yyyy-MM-dd, d/M/yyyy, "
                            + "HHmm, yyyy-MM-dd HHmm, or d/M/yyyy HHmm.");
        }
        addTask(new Deadline(requireFileSafe(deadlineParts[0]), deadlineDateTime));
    }

    /** Adds an event after validating its description and both date or time markers. */
    private static void addEvent(String description) throws CookieException {
        String[] eventParts = description.split("\\s+/from\\s+", 2);
        if (eventParts.length < 2 || eventParts[0].isBlank()) {
            throw new CookieException("An event needs a description, a start time after /from, and an end time after /to.");
        }

        String[] timeParts = eventParts[1].split("\\s+/to\\s+", 2);
        if (timeParts.length < 2 || timeParts[0].isBlank() || timeParts[1].isBlank()) {
            throw new CookieException("An event needs a description, a start time after /from, and an end time after /to.");
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
        addTask(new Event(eventDescription, start, end));
    }

    /** Displays deadlines and events that occur on the requested calendar date. */
    private static void listOnDate(String value) throws CookieException {
        LocalDate date = parseDate(value);
        UI.showTasksOnDate(date, LST);
    }

    /** Saves the current task list to the data file and reports whether it succeeded. */
    private static boolean saveTasks() {
        try {
            STORAGE.save(LST);
            return true;
        } catch (IOException exception) {
            UI.showSaveError(exception.getMessage());
            return false;
        }
    }

    /** Loads valid task records from the data file when Cookie starts. */
    private static void loadTasks() {
        try {
            LST = STORAGE.load();
        } catch (IOException exception) {
            UI.showLoadError(exception.getMessage());
        }
    }

    /** Reads and responds to commands until the user enters {@code bye}. */
    public static void main(String[] args) {
        loadTasks();
        UI.greet();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            try {
                Parser.ParsedCommand parsedCommand = PARSER.parse(input);
                switch (parsedCommand.command()) {
                    case BYE -> {
                        PARSER.requireNoArguments(parsedCommand);
                        UI.exit();
                        return;
                    }
                    case LIST -> {
                        PARSER.requireNoArguments(parsedCommand);
                        list();
                    }
                    case MARK -> {
                        markTask(PARSER.parseTaskIndex(parsedCommand, LST.size()));
                    }
                    case UNMARK -> {
                        unmarkTask(PARSER.parseTaskIndex(parsedCommand, LST.size()));
                    }
                    case DELETE -> {
                        deleteTask(PARSER.parseTaskIndex(parsedCommand, LST.size()));
                    }
                    case ON -> {
                        PARSER.requireSingleArgument(parsedCommand, "on <date>");
                        listOnDate(parsedCommand.description());
                    }
                    case TODO -> {
                        addTask(new Todo(requireFileSafe(PARSER.requireDescription(parsedCommand))));
                    }
                    case DEADLINE -> {
                        addDeadline(parsedCommand.description());
                    }
                    case EVENT -> {
                        addEvent(parsedCommand.description());
                    }
                }
            } catch (CookieException exception) {
                UI.showError(exception);
            }
        }
    }
}
