import java.util.Scanner;
import java.util.ArrayList;
/**
 * The main entry point for the Cookie command-line application.
 */
public class Cookie {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final ArrayList<String> LST = new ArrayList<>(100);

    /** Displays Cookie's greeting and the prompt for the first command. */
    private static void greet() {
        String banner =
                  " ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗███████╗\n"
                + "██╔════╝██╔═══██╗██╔═══██╗██║ ██╔╝██║██╔════╝\n"
                + "██║     ██║   ██║██║   ██║█████╔╝ ██║█████╗  \n"
                + "██║     ██║   ██║██║   ██║██╔═██╗ ██║██╔══╝  \n"
                + "╚██████╗╚██████╔╝╚██████╔╝██║  ██╗██║███████╗\n"
                + " ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝";

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello! I'm your favourite chatbot Cookie.");
        System.out.println("What do you need today?");
        System.out.println(SEPARATOR);
    }

    /** Prints the message shown when the user ends the conversation. */
    private static void exit() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. I'm going to sleep.");
        System.out.println(SEPARATOR);
    }

    /** Stores user input and prints the message indicating a successful addition */
    private static void add(String item) {
        LST.add(item);
        System.out.println(SEPARATOR);
        System.out.println("Added: " + item);
        System.out.println(SEPARATOR);
    }

    /** Displays the list of items added by users when users enter {@code list} */
    private static void list() {
        System.out.println(SEPARATOR);
        for (int i = 0; i < LST.size(); i++) {
            String item = LST.get(i);
            System.out.println(i+1 + ". " + item);
        }
        System.out.println(SEPARATOR);
    }

    /** Reads and responds to commands until the user enters {@code bye}. */
    public static void main(String[] args) {
        greet();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                exit();
                break;
            }

            if (command.equals("list")) {
                list();
                continue;
            }

            add(command);
        }
    }
}
