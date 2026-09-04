package cookie.ui;

import java.util.Objects;

/** Receives messages produced by Cookie's user-interface layer. */
@FunctionalInterface
public interface Output {
    /**
     * Accepts one complete message for presentation.
     *
     * @param message The message to present.
     */
    void show(String message);

    /**
     * Creates an output that forwards every message to each supplied output in order.
     *
     * @param outputs The outputs that should receive each message.
     * @return An output that forwards messages to all supplied outputs.
     */
    static Output combine(Output... outputs) {
        Output[] outputCopy = outputs.clone();
        for (Output output : outputCopy) {
            Objects.requireNonNull(output);
        }
        return message -> {
            for (Output output : outputCopy) {
                output.show(message);
            }
        };
    }
}
