import java.util.Scanner;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/** The main entry point for the Cookie command-line application. */
public class Cookie {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final ArrayList<Task> LST = new ArrayList<>(100);
    private static final Path FILE_PATH = Paths.get(".", "data", "cookie.txt");

    /** Displays Cookie's greeting and the prompt for the first command. */
    private static void greet() {
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
    private static void exit() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. I'm going to sleep.");
        System.out.println(SEPARATOR);
    }

    /** Stores user input and prints the message indicating a successful addition */
    private static void addTask(Task task) {
        LST.add(task);
        if (!saveTasks()) {
            LST.remove(LST.size() - 1);
            return;
        }
        System.out.println(SEPARATOR);
        System.out.println("Ok. I've added this task:");
        System.out.println("   " + task);
        System.out.println("You have " + LST.size() + " task(s) now. Better start working.");
        System.out.println(SEPARATOR);
    }

    /** Displays the list of items added by users when users enter {@code list} */
    private static void list() {
        System.out.println(SEPARATOR);
        System.out.println("Here are the task(s) in your list:");
        for (int i = 0; i < LST.size(); i++) {
            Task task = LST.get(i);
            System.out.println(i+1 + ". " + task);
        }
        System.out.println(SEPARATOR);
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
        System.out.println(SEPARATOR);
        System.out.println("Wow you actually got work done...");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
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
        System.out.println(SEPARATOR);
        System.out.println("I can't believe you lied to me...");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
    }

    /** Deletes the selected task and reports the removed task and remaining task count. */
    private static void deleteTask(int idx) {
        Task task = LST.get(idx);
        LST.remove(idx);
        if (!saveTasks()) {
            LST.add(idx, task);
            return;
        }
        System.out.println(SEPARATOR);
        System.out.println("You're welcome. I've gotten rid of this task for you:");
        System.out.println("   " + task);
        System.out.println("Now you have " + LST.size() + " task(s) in the list.");
        System.out.println(SEPARATOR);
    }

    /** Displays a friendly error without terminating the application. */
    private static void showError(CookieException exception) {
        System.out.println(SEPARATOR);
        System.out.println("Bruh... " + exception.getMessage());
        System.out.println(SEPARATOR);
    }

    /** Ensures that a command has no arguments after its command word. */
    private static void requireNoArguments(String[] parts, String action) throws CookieException {
        if (parts.length > 1) {
            throw new CookieException("The " + action + " command does not take any arguments.");
        }
    }

    /** Returns a task description, rejecting commands with no description. */
    private static String requireDescription(String description, String action) throws CookieException {
        if (description.isBlank()) {
            throw new CookieException("A " + action + " task needs a description.");
        }
        return description;
    }

    /** Rejects a value that would make the task file format ambiguous. */
    private static String requireFileSafe(String value) throws CookieException {
        if (value.contains("|")) {
            throw new CookieException("Task details cannot contain '|'.");
        }
        return value;
    }

    /** Converts a one-based task number into a zero-based list index. */
    private static int parseTaskIndex(String[] parts, String action) throws CookieException {
        if (parts.length != 2) {
            throw new CookieException("Usage: " + action + " <task number>.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new CookieException("The task number must be a positive whole number.");
        }

        if (taskNumber < 1 || taskNumber > LST.size()) {
            throw new CookieException("There is no task numbered " + taskNumber + ".");
        }
        return taskNumber - 1;
    }

    /** Adds a deadline after validating its description and deadline marker. */
    private static void addDeadline(String description) throws CookieException {
        String[] deadlineParts = description.split("\\s+/by\\s+", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
            throw new CookieException("A deadline needs a description and a date after /by.");
        }
        addTask(new Deadline(requireFileSafe(deadlineParts[0]), requireFileSafe(deadlineParts[1])));
    }

    /** Adds an event after validating its description and both time markers. */
    private static void addEvent(String description) throws CookieException {
        String[] eventParts = description.split("\\s+/from\\s+", 2);
        if (eventParts.length < 2 || eventParts[0].isBlank()) {
            throw new CookieException("An event needs a description, a start time after /from, and an end time after /to.");
        }

        String[] timeParts = eventParts[1].split("\\s+/to\\s+", 2);
        if (timeParts.length < 2 || timeParts[0].isBlank() || timeParts[1].isBlank()) {
            throw new CookieException("An event needs a description, a start time after /from, and an end time after /to.");
        }
        addTask(new Event(requireFileSafe(eventParts[0]), requireFileSafe(timeParts[0]),
                requireFileSafe(timeParts[1])));
    }

    /** Saves the current task list to the hard disk and reports whether it succeeded. */
    private static boolean saveTasks() {
        Path temporaryFile = null;
        try {
            Path parentDir = FILE_PATH.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            temporaryFile = parentDir == null
                    ? Files.createTempFile("cookie-", ".tmp")
                    : Files.createTempFile(parentDir, ".cookie-", ".tmp");
            try (BufferedWriter writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8)) {
                for (Task task : LST) {
                    writer.write(task.toFileFormat());
                    writer.newLine();
                }
            }
            try {
                Files.move(temporaryFile, FILE_PATH, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException e) {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // Preserve the original save error for the user.
                }
            }
            System.out.println(SEPARATOR);
            System.out.println("Oh no! I couldn't save your tasks: " + e.getMessage());
            System.out.println(SEPARATOR);
            return false;
        }
    }

    /** Loads valid task records from the hard-disk file when Cookie starts. */
    private static void loadTasks() {
        if (!Files.exists(FILE_PATH)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(FILE_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                try {
                    LST.add(parseTask(line));
                } catch (CookieException exception) {
                    // Ignore malformed records so one bad line does not prevent startup.
                }
            }
        } catch (IOException e) {
            System.out.println(SEPARATOR);
            System.out.println("Oh no! I couldn't load your tasks: " + e.getMessage());
            System.out.println(SEPARATOR);
        }
    }

    /** Converts one saved task record into a task object. */
    private static Task parseTask(String line) throws CookieException {
        String[] fields = line.trim().split("\\s*\\|\\s*", -1);
        if (fields.length < 3) {
            throw new CookieException("A saved task record is incomplete.");
        }

        Task task;
        switch (TaskType.fromCode(fields[0])) {
        case TODO -> {
            if (fields.length != 3 || fields[2].isBlank()) {
                throw new CookieException("A saved todo record is malformed.");
            }
            task = new Todo(fields[2]);
        }
        case DEADLINE -> {
            if (fields.length != 4 || fields[2].isBlank() || fields[3].isBlank()) {
                throw new CookieException("A saved deadline record is malformed.");
            }
            task = new Deadline(fields[2], fields[3]);
        }
        case EVENT -> {
            if (fields.length != 4 || fields[2].isBlank()) {
                throw new CookieException("A saved event record is malformed.");
            }
            String[] times = fields[3].split("\\s+to\\s+", 2);
            if (times.length != 2 || times[0].isBlank() || times[1].isBlank()) {
                throw new CookieException("A saved event record is malformed.");
            }
            task = new Event(fields[2], times[0], times[1]);
        }
        default -> throw new CookieException("A saved task record is malformed.");
        }

        if ("Done".equalsIgnoreCase(fields[1])) {
            task.mark();
        } else if (!"Not Done".equalsIgnoreCase(fields[1])) {
            throw new CookieException("A saved task record has an invalid status.");
        }
        return task;
    }

    /** Reads and responds to commands until the user enters {@code bye}. */
    public static void main(String[] args) {
        loadTasks();
        greet();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            try {
                if (input.isBlank()) {
                    throw new CookieException("I couldn't understand an empty command.");
                }

                String[] parts = input.split("\\s+");
                String action = parts[0];
                String description = input.substring(action.length()).trim();

                Command command = Command.fromString(action);
                switch (command) {
                    case BYE -> {
                        requireNoArguments(parts, action);
                        exit();
                        return;
                    }
                    case LIST -> {
                        requireNoArguments(parts, action);
                        list();
                    }
                    case MARK -> {
                        markTask(parseTaskIndex(parts, action));
                    }
                    case UNMARK -> {
                        unmarkTask(parseTaskIndex(parts, action));
                    }
                    case DELETE -> {
                        deleteTask(parseTaskIndex(parts, action));
                    }
                    case TODO -> {
                        addTask(new Todo(requireFileSafe(requireDescription(description, action))));
                    }
                    case DEADLINE -> {
                        addDeadline(description);
                    }
                    case EVENT -> {
                        addEvent(description);
                    }
                }
            } catch (CookieException exception) {
                showError(exception);
            }
        }
    }
}
