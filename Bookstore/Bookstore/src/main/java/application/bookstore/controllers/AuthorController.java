package application.bookstore.controllers;

import application.bookstore.auxiliaries.FileHandler;
import application.bookstore.models.Author;
import application.bookstore.models.Book;
import application.bookstore.ui.CreateButton;
import application.bookstore.views.AuthorView;
import application.bookstore.views.LoginView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorController {
    private AuthorView authorView;
    public AuthorController(AuthorView authorView) {
        this.authorView = authorView;
        setSaveListener();
        setDeleteListener();
        setEditListener();
        setSearchListener();
    }

    private void setEditListener() {
        authorView.getFirstNameCol().setOnEditCommit(e -> {
            Author authorToEdit = e.getRowValue();
            String oldVal=authorToEdit.getFirstName();
            authorToEdit.setFirstName(e.getNewValue());
            if (authorToEdit.isValid()){
                authorToEdit.updateFile();
            }
            else {
                System.out.println("Edit value invalid! "+e.getNewValue());
                authorToEdit.setFirstName(oldVal);
                authorView.getTableView().getItems().set(authorView.getTableView().getItems().indexOf(authorToEdit), authorToEdit);
                authorView.getResultLabel().setText("Edit value invalid!");
                authorView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });

        authorView.getLastNameCol().setOnEditCommit(e -> {
            Author authorToEdit = e.getRowValue();
            String oldVal=authorToEdit.getLastName();
            authorToEdit.setLastName(e.getNewValue());
            if (authorToEdit.isValid()){
                authorToEdit.updateFile();
            }
            else {
                System.out.println("Edit value invalid! "+e.getNewValue());
                authorToEdit.setLastName(oldVal);
                authorView.getTableView().getItems().set(authorView.getTableView().getItems().indexOf(authorToEdit), authorToEdit);
                authorView.getResultLabel().setText("Edit value invalid!");
                authorView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });
    }

    private void setSearchListener() {
        authorView.getSearchView().getClearBtn().setOnAction(e -> {
            authorView.getSearchView().getSearchField().setText("");
            authorView.getTableView().setItems(FXCollections.observableArrayList(Author.getAuthors()));
        });
        authorView.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = authorView.getSearchView().getSearchField().getText();
            ArrayList<Author> searchResults = Author.getSearchResults(searchText);
            authorView.getTableView().setItems(FXCollections.observableArrayList(searchResults));
        });
    }

    private void setDeleteListener() {
        authorView.getDeleteBtn().setOnAction(e->{
            ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "The author that you are trying to delete may be connected to one or more books.",
                    confirmBtn,
                    cancelBtn);


            alert.setTitle("Delete Warning!");
            alert.setHeaderText("Are you sure you want to delete this author?");

            //Adding css to alert buttons
            alert.getDialogPane().getChildren().forEach(node -> {
                if (node instanceof ButtonBar) {
                    ButtonBar buttonBar = (ButtonBar) node;
                    buttonBar.getButtons().forEach(possibleButtons -> {
                        if (possibleButtons instanceof Button) {
                            Button b = (Button) possibleButtons;
                            if (b.getText().equals("Cancel")) {
                                ImageView imageView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/xMark_icon.png")));
                                b.setGraphic(imageView);
                                b.setStyle("-fx-background-color: #c41010;  -fx-text-fill: #ffffff;");
                            } else {
                                ImageView imageView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/checkMark_icon.png")));
                                b.setGraphic(imageView);
                                b.setStyle("-fx-background-color: #58F800;  -fx-text-fill: #ffffff;");
                            }
                        }
                    });
                }
            });



            Image imageView = new Image(String.valueOf(LoginView.class.getResource("/images/warning_icon.png")));
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(imageView);
            Optional<ButtonType> result = alert.showAndWait();


            if (result.orElse(cancelBtn) == confirmBtn) {
                List<Author> itemsToDelete = List.copyOf(authorView.getTableView().getSelectionModel().getSelectedItems());
                for (Author a: itemsToDelete) {
                    if (a.deleteFromFile()) {
                        authorView.getTableView().getItems().remove(a);
                        System.out.println("Author removed successfully FullName: "+a.getFullName());
                        authorView.getResultLabel().setText("Author removed successfully");
                        authorView.getResultLabel().setTextFill(Color.DARKGREEN);
                    } else {
                        System.out.println("Author deletion failed FullName: "+a.getFullName());
                        authorView.getResultLabel().setText("Author deletion failed");
                        authorView.getResultLabel().setTextFill(Color.DARKRED);
                        break;
                    }
                }
            }
        });
    }

    private void setSaveListener() {
        authorView.getSaveBtn().setOnAction(e -> {
            String firstName = authorView.getFirstNameField().getText();
            String lastName = authorView.getLastNameField().getText();
            Author author = new Author(firstName, lastName);
            if (!author.exists()) {
                if (author.saveInFile()) {
                    authorView.getTableView().getItems().add(author);
                    authorView.getResultLabel().setText("Author created successfully!");
                    authorView.getResultLabel().setTextFill(Color.DARKGREEN);
                    authorView.getFirstNameField().setText("");
                    authorView.getLastNameField().setText("");
                } else {
                    authorView.getResultLabel().setText("Author creation failed!");
                    authorView.getResultLabel().setTextFill(Color.DARKRED);
                }
            }
            else {
                authorView.getResultLabel().setText("Author with this Full Name exists.");
                authorView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });
    }

}
