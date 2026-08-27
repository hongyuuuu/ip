public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String code;

    TaskType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static TaskType fromCode(String code) throws CookieException {
        for (TaskType type : TaskType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new CookieException("I have never seen this task type '" + code + "' before.");
    }
}

