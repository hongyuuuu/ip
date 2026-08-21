/** Represents the most basic task type, no datetime attached */
public class Todo extends Task {

   /** Constructor to reuse superclass' constructor */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
