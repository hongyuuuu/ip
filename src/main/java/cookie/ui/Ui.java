package cookie.ui;

import cookie.command.CookieException;
import cookie.task.Deadline;
import cookie.task.Event;
import cookie.task.Task;
import cookie.task.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Handles Cookie's messages and other interactions with the user. */
public class Ui {
    /** Separates Cookie's messages in the console. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** Formats dates shown in date-based task queries. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Displays Cookie's greeting and the prompt for the first command. */
    public void greet() {
        String banner =
                  " ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗\n"
                + "██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝\n"
                + "██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  \n"
                + "██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  \n"
                + "╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗\n"
                + " ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝";

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello! I'm your favourite chatbot Cookie.");
        System.out.println("What do you need today?");
        System.out.println(SEPARATOR);
    }

    /** Prints the message shown when the user ends the conversation. */
    public void exit() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. I'm going to sleep.");
        System.out.println(SEPARATOR);
    }

    /** Displays confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println("Ok. I've added this task:");
        System.out.println("   " + task);
        System.out.println("You have " + taskCount + " task(s) now. Better start working.");
        System.out.println(SEPARATOR);
    }

    /** Displays all tasks with their one-based positions. */
    public void showTaskList(TaskList tasks) {
        System.out.println(SEPARATOR);
        System.out.println("Here are the task(s) in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(i + 1 + ". " + task);
        }
        System.out.println(SEPARATOR);
    }

    /** Displays confirmation that a task was marked as done. */
    public void showTaskMarked(Task task) {
        System.out.println(SEPARATOR);
        System.out.println("Wow you actually got work done...");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
    }

    /** Displays confirmation that a task was unmarked as done. */
    public void showTaskUnmarked(Task task) {
        System.out.println(SEPARATOR);
        System.out.println("I can't believe you lied to me...");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
    }

    /** Displays confirmation that a task was deleted. */
    public void showTaskDeleted(Task task, int remainingTaskCount) {
        System.out.println(SEPARATOR);
        System.out.println("You're welcome. I've gotten rid of this task for you:");
        System.out.println("   " + task);
        System.out.println("Now you have " + remainingTaskCount + " task(s) in the list.");
        System.out.println(SEPARATOR);
    }

    /** Displays a friendly error without terminating the application. */
    public void showError(CookieException exception) {
        System.out.println(SEPARATOR);
        System.out.println("Bruh... " + exception.getMessage());
        System.out.println(SEPARATOR);
    }

    /** Displays an error encountered while saving tasks. */
    public void showSaveError(String details) {
        System.out.println(SEPARATOR);
        System.out.println("Oh no! I couldn't save your tasks: " + details);
        System.out.println(SEPARATOR);
    }

    /** Displays an error encountered while loading tasks. */
    public void showLoadError(String details) {
        System.out.println(SEPARATOR);
        System.out.println("Oh no! I couldn't load your tasks: " + details);
        System.out.println(SEPARATOR);
    }

    /** Displays deadlines and events that occur on the requested calendar date. */
    public void showTasksOnDate(LocalDate date, TaskList tasks) {
        System.out.println(SEPARATOR);
        System.out.println("Here are the task(s) on " + date.format(DISPLAY_DATE_FORMAT) + ":");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            boolean occursOnDate = false;
            if (task instanceof Deadline deadline) {
                occursOnDate = date.equals(deadline.getBy().getDate());
            } else if (task instanceof Event event) {
                occursOnDate = occursOnDate(event, date);
            }

            if (occursOnDate) {
                System.out.println(i + 1 + ". " + task);
            }
        }
        System.out.println(SEPARATOR);
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
