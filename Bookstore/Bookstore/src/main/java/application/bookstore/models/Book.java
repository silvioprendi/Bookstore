package application.bookstore.models;

import application.bookstore.auxiliaries.FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Book extends BaseModel implements Serializable{
    private String isbn;
    private String title;
    private int stock;
    private float purchasedPrice;
    private float sellingPrice;
    private Author author;
    private static final String FILE_PATH = "data/books.ser";
    private static final File DATA_FILE = new File(FILE_PATH);
    @Serial
    private static final long serialVersionUID = 1234567L;
    private static final ArrayList<Book> books = new ArrayList<>();
    private String Message;
    public Book(){}

    public Book(String isbn, String title, int stock, float purchasedPrice, float sellingPrice, Author author) {
        this.isbn = isbn;
        this.title = title;
        this.stock = stock;
        this.purchasedPrice = purchasedPrice;
        this.sellingPrice = sellingPrice;
        this.author = author;
    }

    public float getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(float purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
            this.sellingPrice = sellingPrice;
    }

    public int getStock() { return stock;  }

    public void setStock(int stock) { this.stock = stock; }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
            this.title = title;
    }


    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getMessage() { return Message; }

    public void setMessage(String message) { Message = message; }

    @Override
    public boolean saveInFile() {
        boolean saved = super.save(DATA_FILE);
        if (saved)
            books.add(this);
        return saved;
    }

    public boolean isValid() {
        if (stock<0) {
            setMessage("Stock is invalid!");
            System.out.println(getMessage());
            return false;
        }
        if (!isbn.matches("^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?" +
                "$)[\\d-]+$") ) {
            setMessage("ISBN is invalid!");
            System.out.println(getMessage());
            return false;
        }
        if (!title.matches("[\\w\\s].+")) {
            setMessage("Title is invalid!");
            System.out.println(getMessage());
            return false;
        }
        if (purchasedPrice<0) {
            setMessage("Purchased Price is invalid!");
            System.out.println(getMessage());
            return false;
        }
        if (sellingPrice<0) {
            setMessage("Selling Price is invalid!");
            System.out.println(getMessage());
            return false;
        }
        return true;
    }


    @Override
    public boolean deleteFromFile(){
        books.remove(this);
        try {
            FileHandler.overwriteCurrentListToFile(DATA_FILE, books);
        }
        catch (Exception e){
            books.add(this);
            System.out.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }

    @Override
    public boolean updateFile(){
        try {
            FileHandler.overwriteCurrentListToFile(DATA_FILE, books);
        }
        catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }

    public static ArrayList<Book> getBooks() {
        if (books.size() == 0) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
                while (true) {
                    Book temp = (Book) inputStream.readObject();
                    if (temp != null)
                        books.add(temp);
                    else
                        break;
                }
                inputStream.close();
            } catch (EOFException eofException) {
                System.out.println("End of book file reached!");
            }
            catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return books;
    }

    public boolean exists(){
        for (Book b: books) {
            if (b.getIsbn().equals(this.getIsbn()))
                return true;
        }
        return false;
    }

    public static ArrayList<Book> getSearchResults(String searchText) {
        ArrayList<Book> searchResults = new ArrayList<>();
        for(Book book: getBooks())
            if (book.getTitle().matches(".*"+searchText+".*") || book.getIsbn().matches(".*"+searchText+".*"))
                searchResults.add(book);
        return searchResults;
    }

}
