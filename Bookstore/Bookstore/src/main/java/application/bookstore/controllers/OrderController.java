package application.bookstore.controllers;

import application.bookstore.models.Author;
import application.bookstore.models.Book;
import application.bookstore.models.BookOrder;
import application.bookstore.models.Order;
import application.bookstore.ui.CreateButton;
import application.bookstore.views.BookView;
import application.bookstore.views.LoginView;
import application.bookstore.views.NewOrderView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class OrderController {
    private final NewOrderView orderView;

    public OrderController(NewOrderView bookView) {
        this.orderView = bookView;
        Order.getOrders();// get data from file
        setEditListener();
        setChooseBookListener();
        setRemoveBookListener();
        setCreateListener();
    }

    private void setChooseBookListener(){
        orderView.getBooks_tableView().setOnMousePressed(e->{
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                    BookOrder b = new BookOrder(1, orderView.getBooks_tableView().getSelectionModel().getSelectedItem());
                    if(b.getBook().getStock()>0) {
                        orderView.getResultLabel().setText("");
                        orderView.getOrder().getBooksOrdered().add(b);
                        orderView.getBooks_tableView().getItems().remove(orderView.getBooks_tableView().getSelectionModel().getSelectedItem());
                        orderView.getTableView().getItems().add(b);
                        orderView.getTotalValueLabel().setText(((Float)orderView.getOrder().getTotal()).toString());
                    } else {
                        orderView.getResultLabel().setText("We don't have stock!");
                        orderView.getResultLabel().setTextFill(Color.DARKRED);
                    }

                }
        });
    }

    private void setRemoveBookListener(){
        orderView.getDeleteBtn().setOnMousePressed(e->{
                BookOrder b = orderView.getTableView().getSelectionModel().getSelectedItem();
                orderView.getOrder().getBooksOrdered().remove(b);
                orderView.getBooks_tableView().getItems().add(b.getBook());
                orderView.getTableView().getItems().remove(b);
                orderView.getTotalValueLabel().setText(((Float)orderView.getOrder().getTotal()).toString());

        });
    }

    private void setEditListener() {
        orderView.getNoCol().setOnEditCommit(e -> {
            BookOrder orderToEdit = e.getRowValue();
            int oldVal=orderToEdit.getQuantity();
            orderToEdit.setQuantity(e.getNewValue());
            if (orderToEdit.getQuantity()>0){
                if (orderToEdit.getQuantity()<=orderToEdit.getBook().getStock()) {
                    System.out.println(orderView.getOrder().getBooksOrdered());
                    int index=orderView.getTableView().getItems().indexOf(orderToEdit);
                    orderView.getTableView().getItems().set(index, orderToEdit);
                    orderView.getTotalValueLabel().setText(((Float) orderView.getOrder().getTotal()).toString());
                }
                else {
                    System.out.println("We don't have enough stock!"+e.getNewValue());
                    orderToEdit.setQuantity(oldVal);
                    orderView.getTableView().getItems().set(orderView.getTableView().getItems().indexOf(orderToEdit), orderToEdit);
                    orderView.getResultLabel().setText("We don't have enough stock! Our current stock is " + orderToEdit.getBook().getStock()
                    + " for the book: " + "\"" + orderToEdit.getTitle() + "\"");
                    orderView.getResultLabel().setTextFill(Color.DARKRED);
                }
            }else {
                System.out.println("Edit value invalid! "+e.getNewValue());
                orderToEdit.setQuantity(oldVal);
                orderView.getTableView().getItems().set(orderView.getTableView().getItems().indexOf(orderToEdit), orderToEdit);
                orderView.getResultLabel().setText("Edit value invalid!");
                orderView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });

    }

    private void setCreateListener() {
        orderView.getSaveBtn().setOnMousePressed(e -> {
            orderView.getOrder().completeOrder(orderView.getNameField().getText());
            if (orderView.getOrder().saveInFile()) {
                for (BookOrder b : orderView.getOrder().getBooksOrdered()) {
                    b.getBook().setStock(b.getBook().getStock() - b.getQuantity());
                }

                ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "To view your order please go to Bookstore\\Bookstore\\bills",
                        okBtn
                        );


                alert.setTitle("Information");
                alert.setHeaderText("Order was created successfully.");

                //Adding css to alert buttons
                alert.getDialogPane().getChildren().forEach(node -> {
                    if (node instanceof ButtonBar) {
                        ButtonBar buttonBar = (ButtonBar) node;
                        buttonBar.getButtons().forEach(possibleButtons -> {
                            if (possibleButtons instanceof Button) {
                                Button b = (Button) possibleButtons;
                                if (b.getText().equals("OK")) {
                                    ImageView imageView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/checkMark_icon.png")));
                                    b.setGraphic(imageView);
                                    b.setStyle("-fx-background-color: #58F800;  -fx-text-fill: #ffffff;");
                                }
                            }
                        });
                    }
                });


                Image imageView = new Image(String.valueOf(LoginView.class.getResource("/images/info_icon.png")));
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(imageView);
                Optional<ButtonType> result = alert.showAndWait();

                System.out.println(orderView.getOrder());
                orderView.getOrder().print();
                orderView.getResultLabel().setText("Order created successfully");
                Book book = Book.getBooks().get(0);
                book.updateFile();
                orderView.getResultLabel().setTextFill(Color.DARKGREEN);
                resetFields();
//                orderView.getMainView().getTabPane().getTabs().remove(orderView.getTab());
            } else {
                orderView.getResultLabel().setText("Client name cannot be empty!");
                orderView.getResultLabel().setTextFill(Color.DARKRED);
            }

        });
    }
    private void resetFields() {
        orderView.getNameField().setText("");
    }
}
