package application.bookstore.views;

import application.bookstore.ui.LoginButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginView extends View {

    private final BorderPane borderPane = new BorderPane();

    private final TextField usernameField = new TextField();

    private final PasswordField passwordField = new PasswordField();
    private final Button loginBtn = new LoginButton();
    private final Label errorLabel = new Label("");
    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginBtn() {
        return loginBtn;
    }
    public Label getErrorLabel() {
        return errorLabel;
    }
    public LoginView() {
        setView();
    }

    private void setView() {

        Text usernameText = new Text("Username: ");
        Text passwordText = new Text("Password: ");
        Text loginText = new Text("LOGIN");
        loginText.setStyle("-fx-font: 48 times;");

        usernameText.setFill(Color.BLACK);
        passwordText.setFill(Color.BLACK);
        loginText.setFill(Color.BLACK);

        Label usernameLabel = new Label("", usernameField);
        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(usernameText, usernameLabel);
        hBox1.setAlignment(Pos.CENTER);

        Label passwordLabel = new Label("", passwordField);
        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(passwordText, passwordLabel);
        hBox2.setAlignment(Pos.CENTER);

        errorLabel.setTextFill(Color.BLACK);

        VBox vBox = new VBox();

        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(60));
        vBox.setSpacing(80);

        VBox vBox1 = new VBox();
        vBox1.getChildren().addAll(hBox1, hBox2);
        vBox1.setSpacing(20);
        vBox.getChildren().addAll(loginText, vBox1, loginBtn, errorLabel);


        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView(String.valueOf(LoginView.class.getResource("/images/book-store.png")));

        imageView.setFitHeight(700);
        imageView.setFitWidth(1230);
        stackPane.getChildren().addAll(imageView ,vBox);


        borderPane.setCenter(stackPane);

    }

    @Override
    public Parent getView() {
        return borderPane;
    }
}
