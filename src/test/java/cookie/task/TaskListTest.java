package cookie.task;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests ordered task-list operations and iteration. */
public class TaskListTest {
    @Test
    public void taskList_addInsertDeleteAndGet_maintainsOrder() {
        Todo first = new Todo("first");
        Todo inserted = new Todo("inserted");
        Todo last = new Todo("last");
        TaskList tasks = new TaskList();

        tasks.add(first);
        tasks.add(last);
        tasks.add(1, inserted);

        assertEquals(3, tasks.size());
        assertEquals(first, tasks.get(0));
        assertEquals(inserted, tasks.get(1));
        assertEquals(inserted, tasks.delete(1));
        assertEquals(List.of(first, last), toList(tasks));
    }

    @Test
    public void taskList_markAndUnmark_delegatesToSelectedTask() {
        TaskList tasks = new TaskList(List.of(new Todo("first"), new Todo("second")));

        tasks.mark(1);
        assertTrue(tasks.get(1).isDone());
        assertFalse(tasks.get(0).isDone());
        tasks.unmark(1);
        assertFalse(tasks.get(1).isDone());
    }

    @Test
    public void iterator_returnsTasksInStoredOrder() {
        TaskList tasks = new TaskList(List.of(new Todo("first"), new Todo("second")));

        assertEquals(List.of("first", "second"), toList(tasks).stream()
                .map(Task::getDescription)
                .toList());
    }

    private List<Task> toList(TaskList tasks) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(task);
        }
        return result;
    }
}
