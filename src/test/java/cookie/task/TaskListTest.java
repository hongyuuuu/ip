package cookie.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

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
        TaskList tasks = new TaskList(new Todo("first"), new Todo("second"));

        tasks.mark(1);
        assertTrue(tasks.get(1).isDone());
        assertFalse(tasks.get(0).isDone());
        tasks.unmark(1);
        assertFalse(tasks.get(1).isDone());
    }

    @Test
    public void iterator_returnsTasksInStoredOrder() {
        TaskList tasks = new TaskList(new Todo("first"), new Todo("second"));

        assertEquals(List.of("first", "second"), toList(tasks).stream()
                .map(Task::getDescription)
                .toList());
    }

    @Test
    public void findAndFindOn_matchingTasks_preserveOriginalTaskNumbers() {
        LocalDate queriedDate = LocalDate.of(2026, 8, 27);
        TaskList tasks = new TaskList(
                new Todo("read book"),
                new Todo("buy groceries"),
                new Deadline("return book", new DateTimeValue(queriedDate, null)),
                new Event("conference",
                        new DateTimeValue(queriedDate.minusDays(1), LocalTime.of(9, 0)),
                        new DateTimeValue(queriedDate.plusDays(1), LocalTime.of(17, 0))));

        assertEquals(List.of(1, 3), tasks.find("book").stream()
                .map(TaskList.IndexedTask::taskNumber)
                .toList());
        assertEquals(List.of(3, 4), tasks.findOn(queriedDate).stream()
                .map(TaskList.IndexedTask::taskNumber)
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
