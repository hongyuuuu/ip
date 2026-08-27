import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Owns the ordered collection of tasks managed by Cookie. */
public class TaskList implements Iterable<Task> {
    /** The tasks in their display and persistence order. */
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>(100);
    }

    /** Creates a task list containing a copy of the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of this list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Inserts a task at the specified position. */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /** Marks the task at the specified position as done. */
    public void mark(int index) {
        tasks.get(index).mark();
    }

    /** Marks the task at the specified position as not done. */
    public void unmark(int index) {
        tasks.get(index).unmark();
    }

    /** Returns the task at the specified position. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Deletes and returns the task at the specified position. */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /** Returns the number of tasks in this list. */
    public int size() {
        return tasks.size();
    }

    /** Returns an iterator over tasks in their stored order. */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
