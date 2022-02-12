package application.bookstore.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class LoginButton extends Button {
    public LoginButton() {
        super.setText("Login");
        super.setGraphic(getImage());
        super.setTextFill(Color.BLACK);
        super.setStyle("-fx-background-color: #FFFF33");
    }

    private ImageView getImage() {
        ImageView imageView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/login_icon.png")));
        return imageView;
    }
}
