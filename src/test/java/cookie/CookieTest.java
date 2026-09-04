package cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests Cookie's single-command response API used by the GUI. */
public class CookieTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getResponse_validAndInvalidCommands_returnsMatchingResponses() {
        Cookie cookie = new Cookie(temporaryDirectory.resolve("cookie.txt").toString(), false);

        String addResponse = cookie.getResponse("todo buy milk");
        String listResponse = cookie.getResponse("list");
        String errorResponse = cookie.getResponse("unknown");

        assertTrue(addResponse.contains("[T][ ] buy milk"));
        assertTrue(listResponse.contains("1. [T][ ] buy milk"));
        assertEquals("Bruh... What is that command!?", errorResponse);
    }

    @Test
    public void getStartupResponse_malformedRecords_reportsLineNumbersAndKeepsValidTasks()
            throws IOException {
        Path dataFile = temporaryDirectory.resolve("cookie.txt");
        List<String> savedLines = List.of(
                "T | Not Done | valid task",
                "invalid record",
                "D | Not Done | invalid date | 2026-02-30");
        Files.write(dataFile, savedLines);
        Cookie cookie = new Cookie(dataFile.toString(), false);

        String startupResponse = cookie.getStartupResponse();

        assertTrue(startupResponse.contains("lines 2, 3"));
        assertTrue(cookie.getResponse("list").contains("1. [T][ ] valid task"));
        assertEquals(savedLines, Files.readAllLines(dataFile));
    }
}
