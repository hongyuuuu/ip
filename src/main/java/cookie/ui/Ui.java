package cookie.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import cookie.command.CookieException;
import cookie.task.Deadline;
import cookie.task.Event;
import cookie.task.Task;
import cookie.task.TaskList;

/** Handles Cookie's messages and other interactions with the user. */
public class Ui {
    /** Separates Cookie's messages in the console. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** Formats dates shown in date-based task queries. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Whether messages should be written to the console. */
    private final boolean isConsoleOutputEnabled;

    /** The most recent message prepared for the user. */
    private String latestMessage = "";

    /** Creates a UI handler for Cookie. */
    public Ui() {
        this(true);
    }

    /**
     * Creates a UI handler with optional console output.
     *
     * @param isConsoleOutputEnabled Whether messages should be written to the console.
     */
    public Ui(boolean isConsoleOutputEnabled) {
        this.isConsoleOutputEnabled = isConsoleOutputEnabled;
    }

    /** Displays Cookie's greeting and the prompt for the first command. */
    public void greet() {
        String lineSeparator = System.lineSeparator();
        String banner =
                  " ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗" + lineSeparator
                + "██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝" + lineSeparator
                + "██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  " + lineSeparator
                + "██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  " + lineSeparator
                + "╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗" + lineSeparator
                + " ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝";

        show(banner + lineSeparator
                + "Hello! I'm your favourite chatbot Cookie." + lineSeparator
                + "What do you need today?");
    }

    /** Prints the message shown when the user ends the conversation. */
    public void exit() {
        show("Bye. I'm going to sleep.");
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task The added task.
     * @param taskCount The number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int taskCount) {
        String lineSeparator = System.lineSeparator();
        show("Ok. I've added this task:" + lineSeparator
                + "   " + task + lineSeparator
                + "You have " + taskCount + " task(s) now. Better start working.");
    }

    /**
     * Displays all tasks with their one-based positions.
     *
     * @param tasks The tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        StringBuilder message = new StringBuilder("Here are the task(s) in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            message.append(System.lineSeparator()).append(i + 1).append(". ").append(task);
        }
        show(message.toString());
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task The marked task.
     */
    public void showTaskMarked(Task task) {
        show("Wow you actually got work done..." + System.lineSeparator() + "   " + task);
    }

    /**
     * Displays confirmation that a task was unmarked as done.
     *
     * @param task The unmarked task.
     */
    public void showTaskUnmarked(Task task) {
        show("I can't believe you lied to me..." + System.lineSeparator() + "   " + task);
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task The deleted task.
     * @param remainingTaskCount The number of tasks after deletion.
     */
    public void showTaskDeleted(Task task, int remainingTaskCount) {
        String lineSeparator = System.lineSeparator();
        show("You're welcome. I've gotten rid of this task for you:" + lineSeparator
                + "   " + task + lineSeparator
                + "Now you have " + remainingTaskCount + " task(s) in the list.");
    }

    /**
     * Displays a friendly error without terminating the application.
     *
     * @param exception The error to display.
     */
    public void showError(CookieException exception) {
        show("Bruh... " + exception.getMessage());
    }

    /**
     * Displays an error encountered while saving tasks.
     *
     * @param details The details of the saving error.
     */
    public void showSaveError(String details) {
        show("Oh no! I couldn't save your tasks: " + details);
    }

    /**
     * Displays an error encountered while loading tasks.
     *
     * @param details The details of the loading error.
     */
    public void showLoadError(String details) {
        show("Oh no! I couldn't load your tasks: " + details);
    }

    /**
     * Displays deadlines and events that occur on the requested calendar date.
     *
     * @param date The date to search.
     * @param tasks The tasks to search.
     */
    public void showTasksOnDate(LocalDate date, TaskList tasks) {
        StringBuilder message = new StringBuilder("Here are the task(s) on ")
                .append(date.format(DISPLAY_DATE_FORMAT)).append(":");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            boolean occursOnDate = false;
            if (task instanceof Deadline deadline) {
                occursOnDate = date.equals(deadline.getBy().getDate());
            } else if (task instanceof Event event) {
                occursOnDate = occursOnDate(event, date);
            }

            if (occursOnDate) {
                message.append(System.lineSeparator()).append(i + 1).append(". ").append(task);
            }
        }
        show(message.toString());
    }

    /** Displays tasks whose descriptions contain the specified keyword. */
    public void showMatchingTasks(String keyword, TaskList tasks) {
        StringBuilder message = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getDescription().contains(keyword)) {
                message.append(System.lineSeparator()).append(i + 1).append(". ").append(task);
            }
        }
        show(message.toString());
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    /** Records a message and writes it to the console when console output is enabled. */
    private void show(String message) {
        latestMessage = message;
        if (isConsoleOutputEnabled) {
            System.out.println(SEPARATOR);
            System.out.println(message);
            System.out.println(SEPARATOR);
        }
    }

    /** Returns whether an event has a date range that includes the requested date. */
    private boolean occursOnDate(Event event, LocalDate date) {
        LocalDate startDate = event.getStart().getDate();
        LocalDate endDate = event.getEnd().getDate();
        if (startDate == null && endDate == null) {
            return false;
        }
        if (startDate == null) {
            startDate = endDate;
        }
        if (endDate == null) {
            endDate = startDate;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
