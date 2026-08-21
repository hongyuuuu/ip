/** Represents a type of Task with a deadline */
public class Deadline extends Task {

    /** Stores deadline */
    protected String by;

    /** Constructor to reuse superclass' constructor with the addition of deadline */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}