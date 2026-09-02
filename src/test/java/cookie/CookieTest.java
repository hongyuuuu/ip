package cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

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
}
