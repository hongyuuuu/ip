package cookie.task;

import cookie.command.CookieException;

/** Represents the task types supported by Cookie and their storage codes. */
public enum TaskType {
    /** Represents a basic todo task. */
    TODO("T"),
    /** Represents a task with a deadline. */
    DEADLINE("D"),
    /** Represents a task with a start and end time. */
    EVENT("E");

    /** The single-letter code used when persisting this task type. */
    private final String code;

    /** Creates a task type with the specified storage code. */
    TaskType(String code) {
        this.code = code;
    }

    /**
     * Returns the single-letter storage code for this task type.
     *
     * @return The storage code.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Converts a storage code into its corresponding task type.
     *
     * @param code The storage code to convert.
     * @return The matching task type.
     * @throws CookieException If the code is not recognized.
     */
    public static TaskType fromCode(String code) throws CookieException {
        for (TaskType type : TaskType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new CookieException("I have never seen this task type '" + code + "' before.");
    }
}

