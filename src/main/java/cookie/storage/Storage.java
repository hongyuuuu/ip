package cookie.storage;

import cookie.command.CookieException;
import cookie.task.DateTimeValue;
import cookie.task.Deadline;
import cookie.task.Event;
import cookie.task.Task;
import cookie.task.TaskList;
import cookie.task.TaskType;
import cookie.task.Todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeParseException;

/** Handles loading tasks from and saving tasks to Cookie's data file. */
public class Storage {
    /** The file used to persist Cookie's tasks. */
    private final Path filePath;

    /** Creates storage backed by the specified file path. */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /** Loads valid task records from the data file, or an empty list if it does not exist. */
    public TaskList load() throws IOException {
        TaskList tasks = new TaskList();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                try {
                    tasks.add(parseTask(line));
                } catch (CookieException exception) {
                    // Ignore malformed records so one bad line does not prevent startup.
                }
            }
        }
        return tasks;
    }

    /** Saves all tasks to the data file using a temporary file and an atomic replacement when supported. */
    public void save(TaskList tasks) throws IOException {
        Path temporaryFile = null;
        try {
            Path parentDir = filePath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            temporaryFile = parentDir == null
                    ? Files.createTempFile("cookie-", ".tmp")
                    : Files.createTempFile(parentDir, ".cookie-", ".tmp");
            try (BufferedWriter writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    writer.write(task.toFileFormat());
                    writer.newLine();
                }
            }
            try {
                Files.move(temporaryFile, filePath, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // Preserve the original save error for the user.
                }
            }
            throw exception;
        }
    }

    /** Converts one saved task record into a task object. */
    private Task parseTask(String line) throws CookieException {
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
            try {
                task = new Deadline(fields[2], DateTimeValue.parseStorageValue(fields[3]));
            } catch (DateTimeParseException exception) {
                throw new CookieException("A saved deadline record is malformed.");
            }
        }
        case EVENT -> {
            if (fields.length != 4 || fields[2].isBlank()) {
                throw new CookieException("A saved event record is malformed.");
            }
            String[] times = fields[3].split("\\s+to\\s+", 2);
            if (times.length != 2 || times[0].isBlank() || times[1].isBlank()) {
                throw new CookieException("A saved event record is malformed.");
            }
            try {
                task = new Event(fields[2], DateTimeValue.parseStorageValue(times[0]),
                        DateTimeValue.parseStorageValue(times[1]));
            } catch (DateTimeParseException exception) {
                throw new CookieException("A saved event record is malformed.");
            }
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
}
