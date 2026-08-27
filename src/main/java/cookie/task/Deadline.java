package cookie.task;

/** Represents a type of Task with a deadline. */
public class Deadline extends Task {

    /** Stores the date or time by which the task should be completed. */
    protected DateTimeValue by;

    /** Constructor to reuse superclass' constructor with the addition of a deadline. */
    public Deadline(String description, DateTimeValue by) {
        super(description);
        this.by = by;
    }

    /** Returns the deadline's date or time. */
    public DateTimeValue getBy() {
        return this.by;
    }

    /** Returns the string for {@code Deadline} tasks to be logged */
    @Override
    public String toFileFormat() {
        return "D | " + (this.isDone ? "Done | " : "Not Done | ") + this.description
                + " | " + this.by.toStorageString();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.toDisplayString() + ")";
    }
}
