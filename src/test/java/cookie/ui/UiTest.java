package cookie.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cookie.command.CookieException;
import cookie.task.DateTimeValue;
import cookie.task.Deadline;
import cookie.task.Event;
import cookie.task.TaskList;
import cookie.task.Todo;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests the UI's messages and date-based task filtering. */
public class UiTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private PrintStream originalOutput;

    @BeforeEach
    public void redirectStandardOutput() {
        originalOutput = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void restoreStandardOutput() {
        System.setOut(originalOutput);
    }

    @Test
    public void messageMethods_printExpectedTaskInformation() {
        Ui ui = new Ui();
        Todo todo = new Todo("buy milk");
        TaskList tasks = new TaskList();
        tasks.add(todo);

        ui.greet();
        ui.showTaskAdded(todo, 1);
        ui.showTaskList(tasks);
        ui.showTaskMarked(todo);
        ui.showTaskUnmarked(todo);
        ui.showTaskDeleted(todo, 0);
        ui.showError(new CookieException("bad command"));
        ui.showSaveError("save details");
        ui.showLoadError("load details");
        ui.exit();

        String printed = output.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Hello! I'm your favourite chatbot Cookie."));
        assertTrue(printed.contains("Ok. I've added this task:"));
        assertTrue(printed.contains("1. [T][ ] buy milk"));
        assertTrue(printed.contains("Wow you actually got work done..."));
        assertTrue(printed.contains("I can't believe you lied to me..."));
        assertTrue(printed.contains("Now you have 0 task(s) in the list."));
        assertTrue(printed.contains("Bruh... bad command"));
        assertTrue(printed.contains("couldn't save your tasks: save details"));
        assertTrue(printed.contains("couldn't load your tasks: load details"));
        assertTrue(printed.contains("Bye. I'm going to sleep."));
    }

    @Test
    public void showTasksOnDate_listsOnlyMatchingDeadlinesAndOverlappingEvents() {
        Ui ui = new Ui();
        LocalDate queriedDate = LocalDate.of(2026, 8, 27);
        TaskList tasks = new TaskList();
        tasks.add(new Todo("ordinary todo"));
        tasks.add(new Deadline("matching deadline",
                new DateTimeValue(queriedDate, LocalTime.of(17, 0))));
        tasks.add(new Event("overlapping event",
                new DateTimeValue(LocalDate.of(2026, 8, 26), LocalTime.of(23, 0)),
                new DateTimeValue(queriedDate, LocalTime.of(1, 0))));
        tasks.add(new Event("non-matching event",
                new DateTimeValue(LocalDate.of(2026, 8, 28), LocalTime.of(9, 0)),
                new DateTimeValue(LocalDate.of(2026, 8, 28), LocalTime.of(10, 0))));

        ui.showTasksOnDate(queriedDate, tasks);

        String printed = output.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Here are the task(s) on Aug 27 2026:"));
        assertTrue(printed.contains("2. [D][ ] matching deadline"));
        assertTrue(printed.contains("3. [E][ ] overlapping event"));
        assertTrue(!printed.contains("ordinary todo"));
        assertTrue(!printed.contains("non-matching event"));
    }

    @Test
    public void showMatchingTasks_listsOnlyTasksContainingKeyword() {
        Ui ui = new Ui();
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Deadline("return book", new DateTimeValue(
                LocalDate.of(2026, 8, 27), LocalTime.of(17, 0))));
        tasks.add(new Todo("buy groceries"));

        ui.showMatchingTasks("book", tasks);

        String printed = output.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Here are the matching tasks in your list:"));
        assertTrue(printed.contains("1. [T][ ] read book"));
        assertTrue(printed.contains("2. [D][ ] return book"));
        assertTrue(!printed.contains("buy groceries"));
    }
}
