package application.bookstore.controllers;

import application.bookstore.auxiliaries.FileHandler;
import application.bookstore.models.Author;
import application.bookstore.models.Book;
import application.bookstore.views.BookView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookController {
    private final BookView bookView;

    public BookController(BookView bookView) {
        this.bookView = bookView;
        setSaveListener();
        setDeleteListener();
        setEditListener();
        setComboBoxListener();
        setSearchListener();
    }

    private void setSearchListener() {
        bookView.getSearchView().getClearBtn().setOnAction(e -> {
            bookView.getSearchView().getSearchField().setText("");
            bookView.getTableView().setItems(FXCollections.observableArrayList(Book.getBooks()));
        });
        bookView.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = bookView.getSearchView().getSearchField().getText();
            ArrayList<Book> searchResults = Book.getSearchResults(searchText);
            bookView.getTableView().setItems(FXCollections.observableArrayList(searchResults));
        });
    }

    private void setComboBoxListener(){
        bookView.getAuthorsComboBox().setOnMouseClicked(e->{
            bookView.getAuthorsComboBox().getItems().setAll(Author.getAuthors());
            // set default selected the first author
            if (!Author.getAuthors().isEmpty())
                bookView.getAuthorsComboBox().setValue(Author.getAuthors().get(0));
        });

    }
    private void setSaveListener() {
        bookView.getSaveBtn().setOnAction(e -> {
            String isbn = bookView.getIsbnField().getText();
            String title = bookView.getTitleField().getText();
            int stock = Integer.parseInt(bookView.getStockField().getText());
            float purchasedPrice = Float.parseFloat(bookView.getPurchasedPriceField().getText());
            float sellingPrice = Float.parseFloat(bookView.getSellingPriceField().getText());
            Author author = bookView.getAuthorsComboBox().getValue();
            Book book = new Book(isbn, title, stock, purchasedPrice, sellingPrice, author);

            if (!book.exists()){
                if (book.saveInFile()) {
                    bookView.getTableView().getItems().add(book);
                    bookView.getResultLabel().setText("Book created successfully");
                    bookView.getResultLabel().setTextFill(Color.DARKGREEN);
                    resetFields();
                } else {
                    bookView.getResultLabel().setText("Book creation failed " + book.getMessage());
                    bookView.getResultLabel().setTextFill(Color.DARKRED);
                }
            } else {
                bookView.getResultLabel().setText("Book with this ISBN exists.");
                bookView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });
    }

    private void setDeleteListener(){
        bookView.getDeleteBtn().setOnAction(e->{
            List<Book> itemsToDelete = List.copyOf(bookView.getTableView().getSelectionModel().getSelectedItems());
            for (Book b: itemsToDelete) {
                if (b.deleteFromFile()) {
                    bookView.getTableView().getItems().remove(b);
                    System.out.println("Book removed successfully ISBN: "+b.getIsbn());
                    bookView.getResultLabel().setText("Book removed successfully");
                    bookView.getResultLabel().setTextFill(Color.DARKGREEN);
                } else {
                    System.out.println("Book deletion failed ISBN: "+b.getIsbn());
                    bookView.getResultLabel().setText("Book deletion failed");
                    bookView.getResultLabel().setTextFill(Color.DARKRED);
                    break;
                }
            }
        });
    }

    private void setEditListener() {
        bookView.getStockCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            int oldVal=bookToEdit.getStock();
            bookToEdit.setStock(e.getNewValue());
            if (bookToEdit.isValid()){
                bookToEdit.updateFile();
            }
            else {
                System.out.println("Stock value invalid! "+e.getNewValue());
                bookToEdit.setPurchasedPrice(oldVal);
                bookView.getTableView().getItems().set(bookView.getTableView().getItems().indexOf(bookToEdit), bookToEdit);
                bookView.getResultLabel().setText("Stock value invalid!");
                bookView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });

        bookView.getIsbnCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            String oldVal=bookToEdit.getIsbn();
            bookToEdit.setIsbn(e.getNewValue());
            if (bookToEdit.isValid()){
                bookToEdit.updateFile();
            }
            else {
                System.out.println("ISBN value invalid! "+e.getNewValue());
                bookToEdit.setIsbn(oldVal);
                bookView.getTableView().getItems().set(bookView.getTableView().getItems().indexOf(bookToEdit), bookToEdit);
                bookView.getResultLabel().setText("ISBN value invalid!");
                bookView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });

        bookView.getTitleCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            String oldVal=bookToEdit.getTitle();
            bookToEdit.setTitle(e.getNewValue());
            if (bookToEdit.isValid()){
                bookToEdit.updateFile();
            }
            else {
                System.out.println("Title value invalid! "+e.getNewValue());
                bookToEdit.setTitle(oldVal);
                bookView.getTableView().getItems().set(bookView.getTableView().getItems().indexOf(bookToEdit), bookToEdit);
                bookView.getResultLabel().setText("Title value invalid!");
                bookView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });

        bookView.getPurchasedPriceCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            float oldVal=bookToEdit.getPurchasedPrice();
            bookToEdit.setPurchasedPrice(e.getNewValue());
            if (bookToEdit.isValid()){
                bookToEdit.updateFile();
            }
            else {
                System.out.println("Purchased Price value invalid! "+e.getNewValue());
                bookToEdit.setPurchasedPrice(oldVal);
                bookView.getTableView().getItems().set(bookView.getTableView().getItems().indexOf(bookToEdit), bookToEdit);
                bookView.getResultLabel().setText("Purchased Price value invalid!");
                bookView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });

        bookView.getSellingPriceCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            float oldVal=bookToEdit.getSellingPrice();
            bookToEdit.setSellingPrice(e.getNewValue());
            if (bookToEdit.isValid()){
                bookToEdit.updateFile();
            }
            else {
                System.out.println("Selling Price value invalid! "+e.getNewValue());
                bookToEdit.setSellingPrice(oldVal);
                bookView.getTableView().getItems().set(bookView.getTableView().getItems().indexOf(bookToEdit), bookToEdit);
                bookView.getResultLabel().setText("Selling Price value invalid!");
                bookView.getResultLabel().setTextFill(Color.DARKRED);
            }
        });


    }

    private void resetFields() {
        bookView.getStockField().setText("");
        bookView.getIsbnField().setText("");
        bookView.getTitleField().setText("");
        bookView.getPurchasedPriceField().setText("");
        bookView.getSellingPriceField().setText("");
    }
}
