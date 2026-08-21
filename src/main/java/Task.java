/** Represents a task with a description and completion status */
public class Task {
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

    /** Marks task as done, updates status isDone to true */
    public void mark() {
        this.isDone = true;
    }

    /** Unmarks task as done, updates status isDone to false */
    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return this.getCheckbox() + " " + this.description;
    }
}

