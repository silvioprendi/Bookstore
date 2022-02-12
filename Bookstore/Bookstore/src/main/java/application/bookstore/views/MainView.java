package application.bookstore.views;

import application.bookstore.controllers.MainController;
import application.bookstore.models.Role;
import application.bookstore.ui.CreateButton;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainView extends View {
    private final MenuBar menuBar = new MenuBar();

    ImageView adminView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/admin_icon.png")));
    private final Menu adminMenu = new Menu("Administrator", adminView);

    ImageView usersView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/users_icon.png")));
    private final MenuItem manageUsers = new MenuItem("Manage Users", usersView);

    ImageView menuBookView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/menuBook_icon.png")));
    private final Menu booksMenu= new Menu("Books", menuBookView);
    ImageView booksView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/books_icon.png")));
    private final MenuItem viewBooks = new MenuItem("View Books", booksView);

    ImageView authorsView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/authors_icon.png")));
    private final MenuItem viewAuthors = new MenuItem("View Authors", authorsView);

    ImageView salesView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/sales_icon.png")));
    private final Menu salesMenu= new Menu("Sales", salesView);

    ImageView billView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/bill_icon.png")));
    private final MenuItem newBill = new MenuItem("New Bill", billView);

    ImageView changePassView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/changePass_icon.png")));
    private final Label changePasswordMenuLabel = new Label("Change Password", changePassView);
    private final Menu changePasswordMenu= new Menu("", changePasswordMenuLabel);

    ImageView logoutView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/logout_icon.png")));
    private final Label logoutMenuLabel = new Label("Logout", logoutView);
    private final Menu logoutMenu = new Menu("", logoutMenuLabel);

    ImageView StatsOfBooksView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/booksStats_icon.png")));
    private final MenuItem viewStatsOfBooks = new MenuItem("Stats for Books", StatsOfBooksView);

    ImageView StatsOfUsersView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/usersStats_icon.png")));
    private final MenuItem viewStatsOfUsers = new MenuItem("Stats for Users", StatsOfUsersView);

    ImageView StatsOfClientsView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/clientsStats_icon.png")));
    private final MenuItem viewStatsOfClients = new MenuItem("Stats for Clients", StatsOfClientsView);

    ImageView statsView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/analysis_icon.png")));
    private final Menu statsMenu = new Menu("View Sales Analysis", statsView);

    public MainView(Stage mainStage){
        new MainController(this, mainStage);
    }


    private final TabPane tabPane = new TabPane();

    @Override
    public Parent getView() {
        menuBar.setStyle("    -fx-background-color: #9A9A9A;"+
                "    -fx-selection-bar: lightgray ;" +
                "    -fx-text-background-color: black ;"+
                "    -fx-focused-text-base-color: black ;" +
                "    -fx-alignment: center ;");

        BorderPane borderPane = new BorderPane();

        booksMenu.getItems().addAll(viewBooks, viewAuthors);
        salesMenu.getItems().add(newBill);
        menuBar.getMenus().addAll(booksMenu, salesMenu, changePasswordMenu, logoutMenu);


        Tab defaultTab = new Tab("Books", booksView);
        defaultTab.setContent(new BookView().getView());


        Role currentRole = (getCurrentUser() != null ? getCurrentUser().getRole() : null);
        if (currentRole != null) {
            if (currentRole == Role.ADMIN) {
                menuBar.getMenus().add(adminMenu);
                adminMenu.getItems().addAll(manageUsers);
            }
            if (currentRole == Role.MANAGER || currentRole == Role.ADMIN) {
                salesMenu.getItems().add(statsMenu);
                statsMenu.getItems().addAll(viewStatsOfUsers, viewStatsOfBooks, viewStatsOfClients);
            }
            tabPane.getTabs().add(defaultTab);
        }

        VBox topPane = new VBox();
        topPane.getChildren().addAll(menuBar, tabPane);
        borderPane.setTop(topPane);

        borderPane.setBottom(new StackPane(new Text(getCurrentUser().getUsername() + ", welcome to our bookstore :)")));
        return borderPane;
    }


    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Menu getBooksMenu() {
        return booksMenu;
    }

    public MenuItem getViewBooks() {
        return viewBooks;
    }

    public MenuItem getViewAuthors() {
        return viewAuthors;
    }

    public Menu getSalesMenu() {
        return salesMenu;
    }

    public MenuItem getNewBill() {
        return newBill;
    }

    public Menu getChangePasswordMenu() {
        return changePasswordMenu;
    }

    public Menu getLogoutMenu() {
        return logoutMenu;
    }

    public Menu getAdminMenu() {
        return adminMenu;
    }

    public MenuItem getManageUsers() {
        return manageUsers;
    }

    public MenuItem getStatsMenu() {
        return statsMenu;
    }

    public MenuItem getStatsOfBooks() {
        return viewStatsOfBooks;
    }
    public MenuItem getStatsOfClients() {
        return viewStatsOfClients;
    }
    public MenuItem getStatsOfUsers() {
        return viewStatsOfUsers;
    }

    public Label getChangePasswordMenuLabel() {
        return changePasswordMenuLabel;
    }

    public Label getLogoutMenuLabel() {
        return logoutMenuLabel;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

}
