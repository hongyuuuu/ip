/** Represents a type of Task with start/end time */
public class Event extends Task {

    /** Stores start time */
    protected String start;

    /** Stores end time */
    protected String end;

    /** Constructor to reuse superclass' constructor with the addition of event timing */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start + " to: " + end + ")";
    }
}
