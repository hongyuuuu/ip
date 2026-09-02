package cookie.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** Represents one message and its speaker's avatar in the chat dialog. */
public class DialogBox extends HBox {
    private final Label text;
    private final ImageView displayPicture;

    /**
     * Creates a right-aligned dialog box.
     *
     * @param message The message to display.
     * @param image The speaker's avatar.
     */
    public DialogBox(String message, Image image) {
        text = new Label(message);
        displayPicture = new ImageView(image);

        text.setWrapText(true);
        displayPicture.setFitWidth(100.0);
        displayPicture.setFitHeight(100.0);
        setAlignment(Pos.TOP_RIGHT);

        getChildren().addAll(text, displayPicture);
    }
}
