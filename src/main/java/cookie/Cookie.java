package cookie;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import cookie.command.CookieException;
import cookie.command.Parser;
import cookie.storage.Storage;
import cookie.task.Deadline;
import cookie.task.Event;
import cookie.task.Task;
import cookie.task.TaskList;
import cookie.task.Todo;
import cookie.ui.Ui;

/** The main entry point for the Cookie command-line application. */
public class Cookie {
    /** The default path used to store Cookie's task data. */
    private static final String DEFAULT_FILE_PATH = "./data/cookie.txt";

    /** The tasks currently managed by this Cookie instance. */
    private final TaskList tasks;

    /** Handles user-facing messages for this Cookie instance. */
    private final Ui ui;

    /** Handles persistence for this Cookie instance. */
    private final Storage storage;

    /** Interprets commands entered by the user. */
    private final Parser parser;

    /** Whether the most recently processed command requested that Cookie exit. */
    private boolean isExitRequested;

    /** Creates Cookie for the GUI using the default task file. */
    public Cookie() {
        this(DEFAULT_FILE_PATH, false);
    }

    /**
     * Creates Cookie with a task file at the specified path.
     *
     * @param filePath The path of the task file.
     */
    public Cookie(String filePath) {
        this(filePath, true);
    }

    /** Creates Cookie with the requested storage path and console output mode. */
    Cookie(String filePath, boolean isConsoleOutputEnabled) {
        this.ui = new Ui(isConsoleOutputEnabled);
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        TaskList loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (IOException exception) {
            ui.showLoadError(exception.getMessage());
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
    }

    /** Stores user input and prints the message indicating a successful addition. */
    private void addTask(Task task) {
        tasks.add(task);
        if (!saveTasks()) {
            tasks.delete(tasks.size() - 1);
            return;
        }
        ui.showTaskAdded(task, tasks.size());
    }

    /** Displays the list of items added by users when users enter {@code list}. */
    private void list() {
        ui.showTaskList(tasks);
    }

    /** Marks a task as done and prints a message indicating the successful update. */
    private void markTask(int idx) {
        Task task = tasks.get(idx);
        boolean wasDone = task.isDone();
        tasks.mark(idx);
        if (!saveTasks()) {
            if (wasDone) {
                tasks.mark(idx);
            } else {
                tasks.unmark(idx);
            }
            return;
        }
        ui.showTaskMarked(task);
    }

    /** Marks a task as not done and prints a message indicating the successful update. */
    private void unmarkTask(int idx) {
        Task task = tasks.get(idx);
        boolean wasDone = task.isDone();
        tasks.unmark(idx);
        if (!saveTasks()) {
            if (wasDone) {
                tasks.mark(idx);
            } else {
                tasks.unmark(idx);
            }
            return;
        }
        ui.showTaskUnmarked(task);
    }

    /** Deletes the selected task and reports the removed task and remaining task count. */
    private void deleteTask(int idx) {
        Task task = tasks.get(idx);
        tasks.delete(idx);
        if (!saveTasks()) {
            tasks.add(idx, task);
            return;
        }
        ui.showTaskDeleted(task, tasks.size());
    }

    /** Adds a deadline after the parser validates its description and date or time. */
    private void addDeadline(String description) throws CookieException {
        Parser.ParsedDeadline parsedDeadline = parser.parseDeadline(description);
        addTask(new Deadline(parsedDeadline.description(), parsedDeadline.dateTime()));
    }

    /** Adds an event after the parser validates its description and temporal values. */
    private void addEvent(String description) throws CookieException {
        Parser.ParsedEvent parsedEvent = parser.parseEvent(description);
        addTask(new Event(parsedEvent.description(), parsedEvent.start(), parsedEvent.end()));
    }

    /** Displays deadlines and events that occur on the requested calendar date. */
    private void listOnDate(String value) throws CookieException {
        LocalDate date = parser.parseDate(value);
        ui.showTasksOnDate(date, tasks);
    }

    /** Displays tasks whose descriptions contain the requested keyword. */
    private void findTasks(String keyword) {
        ui.showMatchingTasks(keyword, tasks);
    }

    /** Saves the current task list to the data file and reports whether it succeeded. */
    private boolean saveTasks() {
        try {
            storage.save(tasks);
            return true;
        } catch (IOException exception) {
            ui.showSaveError(exception.getMessage());
            return false;
        }
    }

    /** Reads and responds to commands until the user enters {@code bye}. */
    public void run() {
        ui.greet();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            getResponse(scanner.nextLine());
            if (isExitRequested) {
                return;
            }
        }
    }

    /**
     * Processes one user command and returns Cookie's response.
     *
     * @param input The command entered by the user.
     * @return Cookie's response to the command.
     */
    public String getResponse(String input) {
        isExitRequested = false;
        try {
            execute(parser.parse(input));
        } catch (CookieException exception) {
            ui.showError(exception);
        }
        return ui.getLatestMessage();
    }

    /** Executes a parsed command against Cookie's current task list. */
    private void execute(Parser.ParsedCommand parsedCommand) throws CookieException {
        switch (parsedCommand.command()) {
            case BYE -> {
                parser.requireNoArguments(parsedCommand);
                ui.exit();
                isExitRequested = true;
            }
            case LIST -> {
                parser.requireNoArguments(parsedCommand);
                list();
            }
            case MARK -> markTask(parser.parseTaskIndex(parsedCommand, tasks.size()));
            case UNMARK -> unmarkTask(parser.parseTaskIndex(parsedCommand, tasks.size()));
            case DELETE -> deleteTask(parser.parseTaskIndex(parsedCommand, tasks.size()));
            case ON -> {
                parser.requireSingleArgument(parsedCommand, "on <date>");
                listOnDate(parsedCommand.description());
            }
            case FIND -> {
                parser.requireSingleArgument(parsedCommand, "find <keyword>");
                findTasks(parsedCommand.argument(0));
            }
            case TODO -> addTask(new Todo(
                    parser.requireFileSafe(parser.requireDescription(parsedCommand))));
            case DEADLINE -> addDeadline(parsedCommand.description());
            case EVENT -> addEvent(parsedCommand.description());
            default -> throw new AssertionError("Unhandled command: " + parsedCommand.command());
        }
    }

    /**
     * Starts Cookie with its default task file.
     *
     * @param args Command-line arguments, which are ignored.
     */
    public static void main(String[] args) {
        new Cookie(DEFAULT_FILE_PATH).run();
    }
}
