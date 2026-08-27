import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

/** The main entry point for the Cookie command-line application. */
public class Cookie {
    private static TaskList LST = new TaskList();
    private static final Ui UI = new Ui();
    private static final Storage STORAGE = new Storage("./data/cookie.txt");
    private static final Parser PARSER = new Parser();
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

    /** Adds a deadline after the parser validates its description and date or time. */
    private static void addDeadline(String description) throws CookieException {
        Parser.ParsedDeadline parsedDeadline = PARSER.parseDeadline(description);
        addTask(new Deadline(parsedDeadline.description(), parsedDeadline.dateTime()));
    }

    /** Adds an event after the parser validates its description and temporal values. */
    private static void addEvent(String description) throws CookieException {
        Parser.ParsedEvent parsedEvent = PARSER.parseEvent(description);
        addTask(new Event(parsedEvent.description(), parsedEvent.start(), parsedEvent.end()));
    }

    /** Displays deadlines and events that occur on the requested calendar date. */
    private static void listOnDate(String value) throws CookieException {
        LocalDate date = PARSER.parseDate(value);
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
                        addTask(new Todo(
                                PARSER.requireFileSafe(PARSER.requireDescription(parsedCommand))));
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
