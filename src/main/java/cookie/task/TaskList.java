package cookie.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Owns the ordered collection of tasks managed by Cookie. */
public class TaskList implements Iterable<Task> {
    /** The tasks in their display and persistence order. */
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list containing the supplied tasks in argument order.
     *
     * @param tasks The tasks to add to this list.
     */
    public TaskList(Task... tasks) {
        this(List.of(tasks));
    }

    /**
     * Creates a task list containing a copy of the supplied tasks.
     *
     * @param tasks The tasks to copy into this list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of this list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Inserts a task at the specified position.
     *
     * @param index The zero-based insertion position.
     * @param task The task to insert.
     */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Marks the task at the specified position as done.
     *
     * @param index The zero-based task position.
     */
    public void mark(int index) {
        tasks.get(index).mark();
    }

    /**
     * Marks the task at the specified position as not done.
     *
     * @param index The zero-based task position.
     */
    public void unmark(int index) {
        tasks.get(index).unmark();
    }

    /**
     * Returns the task at the specified position.
     *
     * @param index The zero-based task position.
     * @return The task at the specified position.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Deletes and returns the task at the specified position.
     *
     * @param index The zero-based task position.
     * @return The deleted task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an iterator over tasks in their stored order.
     *
     * @return An iterator over the tasks.
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
