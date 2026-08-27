import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

/** The main entry point for the Cookie command-line application. */
public class Cookie {
    /** The tasks currently managed by this Cookie instance. */
    private final TaskList tasks;

    /** Handles user-facing messages for this Cookie instance. */
    private final Ui ui;

    /** Handles persistence for this Cookie instance. */
    private final Storage storage;

    /** Interprets commands entered by the user. */
    private final Parser parser;

    /** Creates Cookie with a task file at the specified path. */
    public Cookie(String filePath) {
        this.ui = new Ui();
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

    /** Stores user input and prints the message indicating a successful addition */
    private void addTask(Task task) {
        tasks.add(task);
        if (!saveTasks()) {
            tasks.remove(tasks.size() - 1);
            return;
        }
        ui.showTaskAdded(task, tasks.size());
    }

    /** Displays the list of items added by users when users enter {@code list} */
    private void list() {
        ui.showTaskList(tasks);
    }

    /** Marks task as done and prints message indicating a successful mark as done */
    private void markTask(int idx) {
        Task task = tasks.get(idx);
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
        ui.showTaskMarked(task);
    }

    /** Unmarks task as done and prints message indicating a successful unmark as done */
    private void unmarkTask(int idx) {
        Task task = tasks.get(idx);
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
        ui.showTaskUnmarked(task);
    }

    /** Deletes the selected task and reports the removed task and remaining task count. */
    private void deleteTask(int idx) {
        Task task = tasks.get(idx);
        tasks.remove(idx);
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
            String input = scanner.nextLine();
            try {
                Parser.ParsedCommand parsedCommand = parser.parse(input);
                switch (parsedCommand.command()) {
                    case BYE -> {
                        parser.requireNoArguments(parsedCommand);
                        ui.exit();
                        return;
                    }
                    case LIST -> {
                        parser.requireNoArguments(parsedCommand);
                        list();
                    }
                    case MARK -> {
                        markTask(parser.parseTaskIndex(parsedCommand, tasks.size()));
                    }
                    case UNMARK -> {
                        unmarkTask(parser.parseTaskIndex(parsedCommand, tasks.size()));
                    }
                    case DELETE -> {
                        deleteTask(parser.parseTaskIndex(parsedCommand, tasks.size()));
                    }
                    case ON -> {
                        parser.requireSingleArgument(parsedCommand, "on <date>");
                        listOnDate(parsedCommand.description());
                    }
                    case TODO -> {
                        addTask(new Todo(
                                parser.requireFileSafe(parser.requireDescription(parsedCommand))));
                    }
                    case DEADLINE -> {
                        addDeadline(parsedCommand.description());
                    }
                    case EVENT -> {
                        addEvent(parsedCommand.description());
                    }
                }
            } catch (CookieException exception) {
                ui.showError(exception);
            }
        }
    }

    /** Starts Cookie with its default task file. */
    public static void main(String[] args) {
        new Cookie("./data/cookie.txt").run();
    }
}
