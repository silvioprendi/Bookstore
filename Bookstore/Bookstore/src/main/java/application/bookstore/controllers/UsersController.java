package application.bookstore.controllers;

import application.bookstore.models.Role;
import application.bookstore.models.User;
import application.bookstore.views.UsersView;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class UsersController {
    private UsersView usersView;
    public UsersController(UsersView usersView) {
        this.usersView = usersView;
        setSaveListener();
        setDeleteListener();
        setEditListener();
        setSearchListener();
    }

    private void setEditListener() {
        usersView.getUsernameCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            String oldVal=userToEdit.getUsername();
            userToEdit.setUsername(e.getNewValue());
            if (userToEdit.isValid()){
                userToEdit.updateFile();
            }
            else {
                System.out.println("Edit value invalid! "+e.getNewValue());
                userToEdit.setUsername(oldVal);
                usersView.getTableView().getItems().set(usersView.getTableView().getItems().indexOf(userToEdit), userToEdit);
                usersView.getResultLabel().setText("Edit value invalid!");
                usersView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });

        usersView.getPasswordCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            String oldVal=userToEdit.getPassword();
            userToEdit.setPassword(e.getNewValue());
            if (userToEdit.isValid()){
                userToEdit.updateFile();
            }
            else {
                System.out.println("Edit value invalid! "+e.getNewValue());
                userToEdit.setPassword(oldVal);
                usersView.getTableView().getItems().set(usersView.getTableView().getItems().indexOf(userToEdit), userToEdit);
                usersView.getResultLabel().setText("Edit value invalid!");
                usersView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });

        usersView.getRoleCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            Role oldVal=userToEdit.getRole();
            userToEdit.setRole(e.getNewValue());
            if (userToEdit.isValid()){
                userToEdit.updateFile();
            }
            else {
                System.out.println("Edit value invalid! "+e.getNewValue());
                userToEdit.setRole(oldVal);
                usersView.getTableView().getItems().set(usersView.getTableView().getItems().indexOf(userToEdit), userToEdit);
                usersView.getResultLabel().setText("Edit value invalid!");
                usersView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });
    }

    private void setSearchListener() {
        usersView.getSearchView().getClearBtn().setOnAction(e -> {
            usersView.getSearchView().getSearchField().setText("");
            usersView.getTableView().setItems(FXCollections.observableArrayList(User.getUsers()));
        });
        usersView.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = usersView.getSearchView().getSearchField().getText();
            ArrayList<User> searchResults = User.getSearchResults(searchText);
            usersView.getTableView().setItems(FXCollections.observableArrayList(searchResults));
        });
    }

    private void setDeleteListener() {
        usersView.getDeleteBtn().setOnAction(e->{
            List<User> itemsToDelete = List.copyOf(usersView.getTableView().getSelectionModel().getSelectedItems());
            for (User a: itemsToDelete) {
                if (a.deleteFromFile()) {
                    usersView.getTableView().getItems().remove(a);
                    System.out.println("User removed successfully username: "+a.getUsername());
                    usersView.getResultLabel().setText("User removed successfully");
                    usersView.getResultLabel().setTextFill(Color.DARKGREEN);
                } else {
                    System.out.println("User deletion failed username: "+a.getUsername());
                    usersView.getResultLabel().setText("User deletion failed");
                    usersView.getResultLabel().setTextFill(Color.DARKRED);
                    break;
                }
            }
        });
    }

    private void setSaveListener() {
        usersView.getSaveBtn().setOnAction(e -> {
            String username = usersView.getUserNameField().getText();
            String password = usersView.getPasswordField().getText();
            Role role = usersView.getRoleComboBox().getValue();
            User user = new User(username, password, role);
            if (!user.usernameExists()) {
                if (user.saveInFile()) {
                    usersView.getTableView().getItems().add(user);
                    usersView.getResultLabel().setText("User created successfully!");
                    usersView.getResultLabel().setTextFill(Color.DARKGREEN);
                    usersView.getUsernameCol().setText("");
                    usersView.getPasswordField().setText("");
                } else {
                    usersView.getResultLabel().setText("User creation failed!");
                    usersView.getResultLabel().setTextFill(Color.DARKRED);
                }
            }
            else {
                usersView.getResultLabel().setText("User with this username exists.");
                usersView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });
    }

}
