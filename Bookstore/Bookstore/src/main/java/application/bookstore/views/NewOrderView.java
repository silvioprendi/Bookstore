package application.bookstore.views;

import application.bookstore.controllers.BookController;
import application.bookstore.controllers.OrderController;
import application.bookstore.models.*;
import application.bookstore.ui.CreateButton;
import application.bookstore.ui.DeleteButton;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;

public class NewOrderView extends View{
    private final BorderPane borderPane = new BorderPane();
    private final TableView<BookOrder> tableView = new TableView<>();
    private final HBox formPane = new HBox();

    private final TextField nameField = new TextField();
    private final Button saveBtn = new CreateButton();
    private final Button deleteBtn = new DeleteButton();
    private final Label totalValueLabel = new Label("0");
    private final Label totalLabel = new Label("Total: ", totalValueLabel);


    private Order order;
    private Tab tab;
    private MainView mainView;

    private final TableColumn<BookOrder, Integer> noCol = new TableColumn<>("Quantity");
    private final TableColumn<BookOrder, String> isbnCol = new TableColumn<>("ISBN");
    private final TableColumn<BookOrder, String> titleCol = new TableColumn<>("Title");
    private final TableColumn<BookOrder, Float> priceCol = new TableColumn<>("Unit Price");
    private final TableColumn<BookOrder, Float> totalPriceCol = new TableColumn<>("Total Price");
    private final TableColumn<BookOrder, String> authorCol = new TableColumn<>("Author");

    private final TableView<Book> books_tableView = new TableView<>();
    private final TableColumn<Book, Integer> books_stockCol = new TableColumn<>("Stock");
    private final TableColumn<Book, String> books_isbnCol = new TableColumn<>("ISBN");
    private final TableColumn<Book, String> books_titleCol = new TableColumn<>("Title");
    private final TableColumn<Book, Float> books_purchasedPriceCol = new TableColumn<>("Purchased Price");
    private final TableColumn<Book, Float> books_sellingPriceCol = new TableColumn<>("Selling Price");
    private final TableColumn<Book, String> books_authorCol = new TableColumn<>("Author");

    private final Label resultLabel = new Label("");


    public NewOrderView(MainView mainView, Tab tab) {
        // inject the controller
        this.mainView=mainView;
        this.tab=tab;
        order = new Order(getCurrentUser().getUsername());
        setForm();
        setTableView();
        setBooks_TableView();
        new OrderController(this);
    }

    private void setForm() {
        formPane.setPadding(new Insets(20));
        formPane.setSpacing(20);
        formPane.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Client Full Name: ", nameField);
        nameLabel.setContentDisplay(ContentDisplay.TOP);
        totalLabel.setContentDisplay(ContentDisplay.RIGHT);
        formPane.getChildren().addAll(nameLabel, saveBtn, deleteBtn,totalLabel);
    }

    private void setTableView() {
        tableView.setStyle("    -fx-background-color: #9A9A9A;"+
                "    -fx-selection-bar: lightgray ;" +
                "    -fx-text-background-color: black ;"+
                "    -fx-focused-text-base-color: black ;" +
                "    -fx-alignment: center ;");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setEditable(true);
        tableView.setItems(FXCollections.observableArrayList(order.getBooksOrdered()));

        noCol.setEditable(true);
        noCol.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );
        noCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        isbnCol.setEditable(false);
        isbnCol.setCellValueFactory(
                new PropertyValueFactory<>("bookISBN")
        );
        // to edit the value inside the table view
        isbnCol.setCellFactory(TextFieldTableCell.forTableColumn());

        titleCol.setEditable(false);
        titleCol.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());

        priceCol.setEditable(false);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<>("unitPrice")
        );
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        totalPriceCol.setEditable(false);
        totalPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("totalPrice")
        );
        totalPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        authorCol.setEditable(false);
        authorCol.setCellValueFactory(
                new PropertyValueFactory<>("author")
        );

        tableView.getColumns().addAll(noCol, isbnCol, titleCol, priceCol, totalPriceCol, authorCol);
    }

    private void setBooks_TableView() {
        books_tableView.setStyle("    -fx-background-color: #9A9A9A;"+
                "    -fx-selection-bar: lightgray ;" +
                "    -fx-text-background-color: black ;"+
                "    -fx-focused-text-base-color: black ;" +
                "    -fx-alignment: center ;");

        books_tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        books_tableView.setEditable(false);
        books_tableView.setItems(FXCollections.observableArrayList(Book.getBooks()));

        books_stockCol.setCellValueFactory(
                new PropertyValueFactory<>("Stock")
        );
        books_stockCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        books_isbnCol.setCellValueFactory(
                new PropertyValueFactory<>("isbn")
        );
        // to edit the value inside the table view
        isbnCol.setCellFactory(TextFieldTableCell.forTableColumn());

        books_titleCol.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );
        books_titleCol.setCellFactory(TextFieldTableCell.forTableColumn());

        books_purchasedPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("purchasedPrice")
        );
        books_purchasedPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        books_sellingPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("sellingPrice")
        );
        books_sellingPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        books_authorCol.setCellValueFactory(
                new PropertyValueFactory<>("author")
        );

        books_tableView.getColumns().addAll(books_stockCol, books_isbnCol, books_titleCol, books_purchasedPriceCol, books_sellingPriceCol, books_authorCol);
    }


    public TextField getNameField() {
        return nameField;
    }

    public TableView<BookOrder> getTableView() {
        return tableView;
    }

    public TableView<Book> getBooks_tableView() {
        return books_tableView;
    }

    @Override
    public Parent getView() {

        HBox tables = new HBox();
        tables.setAlignment(Pos.CENTER);
        HBox.setHgrow(books_tableView, Priority.ALWAYS);
        HBox.setHgrow(tableView, Priority.ALWAYS);
        tables.getChildren().add(books_tableView);
        tables.getChildren().add(tableView);
        borderPane.setCenter(tables);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.getChildren().addAll(formPane, resultLabel);
        borderPane.setBottom(vBox);

        return borderPane;
    }

    public Label getTotalValueLabel() {
        return totalValueLabel;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Order getOrder() {
        return order;
    }

    public TableColumn<BookOrder, Integer> getNoCol() {
        return noCol;
    }

    public TableColumn<BookOrder, String> getIsbnCol() {
        return isbnCol;
    }

    public TableColumn<BookOrder, String> getTitleCol() {
        return titleCol;
    }

    public TableColumn<BookOrder, Float> getPriceCol() {
        return priceCol;
    }

    public TableColumn<BookOrder, Float> getTotalPriceCol() {
        return totalPriceCol;
    }

    public TableColumn<BookOrder, String> getAuthorCol() {
        return authorCol;
    }

    public Label getResultLabel() {
        return resultLabel;
    }

    public Tab getTab() {
        return tab;
    }
    public MainView getMainView() {
        return mainView;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }
}
