import java.util.Scanner;
import java.util.ArrayList;
/**
 * The main entry point for the Cookie command-line application.
 */
public class Cookie {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final ArrayList<Task> LST = new ArrayList<>(100);

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
    private static void addTask(Task task) {
        LST.add(task);
        System.out.println(SEPARATOR);
        System.out.println("Ok. I've added this task:");
        System.out.println("   " + task);
        System.out.println("You have " + LST.size() + " tasks now. Better start working.");
        System.out.println(SEPARATOR);
    }

    /** Displays the list of items added by users when users enter {@code list} */
    private static void list() {
        System.out.println(SEPARATOR);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < LST.size(); i++) {
            Task task = LST.get(i);
            System.out.println(i+1 + ". " + task);
        }
        System.out.println(SEPARATOR);
    }

    /** Marks task as done and prints message indicating a successful mark as done */
    private static void markTask(int idx) {
        Task task = LST.get(idx);
        task.mark();
        System.out.println(SEPARATOR);
        System.out.println("Wow you actually got work done...");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
    }

    /** Unmarks task as done and prints message indicating a successful unmark as done */
    private static void unmarkTask(int idx) {
        Task task = LST.get(idx);
        task.unmark();
        System.out.println(SEPARATOR);
        System.out.println("I can't believe you lied to me...");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
    }

    /** Reads and responds to commands until the user enters {@code bye}. */
    public static void main(String[] args) {
        greet();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            String action = parts[0];
            String description = input.substring(action.length()).trim();

            if (input.equals("bye")) {
                exit();
                break;
            }

            if (input.equals("list")) {
                list();
            }

            if (action.equals("mark")) {
                int idx = Integer.parseInt(parts[1]) - 1;
                markTask(idx);
            }

            if (action.equals("unmark")) {
                int idx = Integer.parseInt(parts[1]) - 1;
                unmarkTask(idx);
            }

            if (action.equals("todo")) {
                addTask(new Todo(description));
            }
        }
    }
}