package cookie.task;

/** Represents a type of Task with a start and end date or time. */
public class Event extends Task {

    /** Stores the event's starting date or time. */
    protected DateTimeValue start;

    /** Stores the event's ending date or time. */
    protected DateTimeValue end;

    /** Constructor to reuse superclass' constructor with the addition of event timing. */
    public Event(String description, DateTimeValue start, DateTimeValue end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /** Returns the event's starting date or time. */
    public DateTimeValue getStart() {
        return this.start;
    }

    /** Returns the event's ending date or time. */
    public DateTimeValue getEnd() {
        return this.end;
    }

    /** Returns the string for {@code Event} tasks to be logged */
    @Override
    public String toFileFormat() {
        return "E | " + (this.isDone ? "Done | " : "Not Done | ") + this.description
                + " | " + this.start.toStorageString() + " to " + this.end.toStorageString();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start.toDisplayString()
                + " to: " + end.toDisplayString() + ")";
    }
}
