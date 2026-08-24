/** Represents the most basic task type, no datetime attached */
public class Todo extends Task {

   /** Constructor to reuse superclass' constructor */
    public Todo(String description) {
        super(description);
    }

    /** Returns the string for {@code Todo} tasks to be logged */
    @Override
    public String toFileFormat() {
        return "T | " + (this.isDone ? "Done | " : "Not Done | ") + this.description;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
