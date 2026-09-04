package cookie.task;

import java.time.LocalDate;

/** Represents a task with a start and end date or time. */
public class Event extends Task {

    /** Stores the event's starting date or time. */
    protected DateTimeValue start;

    /** Stores the event's ending date or time. */
    protected DateTimeValue end;

    /**
     * Creates an event task with the specified description, start, and end values.
     *
     * @param description The task description.
     * @param start The event's starting date or time.
     * @param end The event's ending date or time.
     */
    public Event(String description, DateTimeValue start, DateTimeValue end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the event's starting date or time.
     *
     * @return The event's start date or time.
     */
    public DateTimeValue getStart() {
        return this.start;
    }

    /**
     * Returns the event's ending date or time.
     *
     * @return The event's end date or time.
     */
    public DateTimeValue getEnd() {
        return this.end;
    }

    /** Returns whether the requested date falls within this event's inclusive date range. */
    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate startDate = start.getDate();
        LocalDate endDate = end.getDate();
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

    /**
     * Returns the string for {@code Event} tasks to be logged.
     *
     * @return The event's storage representation.
     */
    @Override
    public String toFileFormat() {
        return "E | " + (this.isDone ? "Done | " : "Not Done | ") + this.description
                + " | " + this.start.toStorageString() + " to " + this.end.toStorageString();
    }

    /**
     * Returns the event's type marker, checkbox, description, and time range.
     *
     * @return The event's display representation.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start.toDisplayString()
                + " to: " + end.toDisplayString() + ")";
    }
}
