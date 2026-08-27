package cookie.task;

/** Serves as a template for more specific task types. */
public abstract class Task {
    /** The task description. */
    protected String description;

    /** Whether or not the task has been completed. */
    protected boolean isDone;

    /** Creates an incomplete task with the specified description.
     *
     * @param description The task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns the task status as a checkbox.
     *
     * @return The completed or incomplete checkbox.
     */
    public String getCheckbox() {
        return (isDone ? "[X]" : "[ ]");
    }

    /** Returns the task description.
     *
     * @return The task description.
     */
    public String getDescription() {
        return this.description;
    }

    /** Returns whether the task has been completed.
     *
     * @return {@code true} if the task is done, otherwise {@code false}.
     */
    public boolean isDone() {
        return this.isDone;
    }

    /** Marks the task as done. */
    public void mark() {
        this.isDone = true;
    }

    /** Marks the task as not done. */
    public void unmark() {
        this.isDone = false;
    }

    /** Returns the formatted string representation for file storage.
     *
     * @return The task's storage representation.
     */
    public abstract String toFileFormat();

    /** Returns the task's checkbox and description. */
    @Override
    public String toString() {
        return this.getCheckbox() + " " + this.description;
    }
}

