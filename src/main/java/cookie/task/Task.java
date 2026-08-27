package cookie.task;

/** Serves as a template for more specific task types */
public abstract class Task {
    /** The task description */
    protected String description;

    /** Whether or not the task has been completed */
    protected boolean isDone;

    /** Constructor method for initialising a task object */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns task status as a checkbox */
    public String getCheckbox() {
        return (isDone ? "[X]" : "[ ]");
    }

    /** Returns task description */
    public String getDescription() {
        return this.description;
    }

    /** Returns whether the task has been completed. */
    public boolean isDone() {
        return this.isDone;
    }

    /** Marks task as done, updates status isDone to true */
    public void mark() {
        this.isDone = true;
    }

    /** Unmarks task as done, updates status isDone to false */
    public void unmark() {
        this.isDone = false;
    }

    /** Returns the formatted string representation for file storage. */
    public abstract String toFileFormat();

    @Override
    public String toString() {
        return this.getCheckbox() + " " + this.description;
    }
}

