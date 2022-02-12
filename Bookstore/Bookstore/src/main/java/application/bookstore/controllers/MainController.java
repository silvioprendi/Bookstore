package application.bookstore.controllers;
import application.bookstore.models.User;
import application.bookstore.views.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainController {
    private final MainView mainView;
    private final Stage mainStage;

    public MainController(MainView mainView, Stage mainStage) {
        this.mainView = mainView;
        this.mainStage = mainStage;
        setListener();

    }
    private Tab openTab(Tab tab){
        for(Tab t:mainView.getTabPane().getTabs()){
            if(t.getText().equals(tab.getText())){
                return t;
            }
        }
        mainView.getTabPane().getTabs().add(tab);
        mainView.getTabPane().getSelectionModel().selectLast();
        return tab;
    }

    private void setListener() {

        mainView.getViewAuthors().setOnAction((e)-> {
            Tab authorTab = new Tab("Authors");
            authorTab.setContent(new AuthorView().getView());
            Tab tab=openTab(authorTab);
        });

        mainView.getViewBooks().setOnAction((e)-> {
            Tab booksTab = new Tab("Books");
            booksTab.setContent(new BookView().getView());
            Tab tab=openTab(booksTab);
        });
        mainView.getNewBill().setOnAction(e->{
            Tab order = new Tab("New Order");
            order.setContent(new NewOrderView(mainView, order).getView());
            Tab tab=openTab(order);
        });
        mainView.getStatsOfBooks().setOnAction(e->{
            Tab stats = new Tab("Stats of Books");
            stats.setContent(new StatsView().getView());
            Tab tab=openTab(stats);
        });
        mainView.getStatsOfClients().setOnAction(e->{
            Tab stats = new Tab("Stats of Clients");
            stats.setContent(new StatsView().getView2());
            Tab tab=openTab(stats);
        });
        mainView.getStatsOfUsers().setOnAction(e->{
            Tab stats = new Tab("Stats of Users");
            stats.setContent(new StatsView().getView3());
            Tab tab=openTab(stats);
        });
        mainView.getManageUsers().setOnAction(e->{
            Tab users = new Tab("Users");
            users.setContent(new UsersView().getView());
            Tab tab=openTab(users);
        });

        mainView.getChangePasswordMenuLabel().setOnMouseClicked((e)->{


            TextField oldPassField = new TextField();
            TextField newPassField = new TextField();
            TextField newPassField1 = new TextField();
            Label oldPassLabel = new Label("Old Password: ", oldPassField);
            oldPassLabel.setContentDisplay(ContentDisplay.RIGHT);
            Label newPassLabel = new Label("New Password: ", newPassField);
            newPassLabel.setContentDisplay(ContentDisplay.RIGHT);
            Label newPassLabel1 = new Label("New Password: ", newPassField1);
            newPassLabel1.setContentDisplay(ContentDisplay.RIGHT);

            HBox h = new HBox();
            h.setAlignment(Pos.CENTER);
            Button confirmButton = new Button("Confirm");
            confirmButton.setTextFill(Color.WHITE);
            confirmButton.setStyle("-fx-background-color: green");

            Button cancelButton = new Button("Cancel");
            cancelButton.setTextFill(Color.WHITE);
            cancelButton.setStyle("-fx-background-color: red");
            h.getChildren().addAll(confirmButton, cancelButton);
            h.setSpacing(30);

            Label massageLabel = new Label("");

            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            v.getChildren().addAll(oldPassLabel, newPassLabel, newPassLabel1, massageLabel, h);
            v.setSpacing(5);

            ImageView imageView = new ImageView(String.valueOf(LoginView.class.getResource("/images/ChangePassBackground.png")));
            imageView.setFitHeight(700);
            imageView.setFitWidth(1200);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(imageView, v);

            Scene secondScene = new Scene(stackPane, 1200, 700);

            Stage newWindow = new Stage();
            newWindow.setTitle("Change Password");
            newWindow.setScene(secondScene);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.initOwner(mainStage);
            newWindow.getIcons().add(new Image(MainController.class.getResourceAsStream("/images/mechanical-wheel.png")));


            cancelButton.setOnMousePressed(ev->newWindow.close());
            confirmButton.setOnMousePressed(ev->{
                if (oldPassField.getText().equals(mainView.getCurrentUser().getPassword())){
                    if (newPassField.getText().matches(newPassField1.getText())){
                        User u = new User(mainView.getCurrentUser().getUsername(), newPassField.getText(), mainView.getCurrentUser().getRole());
                        if (u.isValid()){
                            mainView.getCurrentUser().deleteFromFile();
                            mainView.setCurrentUser(u);
                            u.saveInFile();
                            newWindow.close();
                        }
                        else{
                            massageLabel.setText("New Password Invalid!");
                            massageLabel.setTextFill(Color.DARKRED);
                        }
                    }
                    else{
                        massageLabel.setText("New Passwords do not match!");
                        massageLabel.setTextFill(Color.DARKRED);
                    }
                }
                else{
                    massageLabel.setText("Old Password Incorrect!");
                    massageLabel.setTextFill(Color.DARKRED);
                }
            });

            newWindow.show();
        });

        mainView.getLogoutMenuLabel().setOnMouseClicked((e)->{
            LoginView loginView = new LoginView();
            new LoginController(loginView, new MainView(mainStage), mainStage);
            Scene scene = new Scene(loginView.getView(), 1200, 700);
            mainStage.setScene(scene);
        });
    }

}
