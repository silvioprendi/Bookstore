package application.bookstore.models;

import application.bookstore.auxiliaries.FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class User extends BaseModel implements Serializable {
    private static final ArrayList<User> users = new ArrayList<>();
    private String username;
    private String password;
    private Role role;
    public static final String FILE_PATH = "data/users.ser";
    private static final File DATA_FILE = new File(FILE_PATH);
    @Serial
    private static final long serialVersionUID = 1234567L;
    public User() {}

    public User(String username, String password, Role role) {
        this(username, password);
        this.role = role;
    }

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "username=" + getUsername() +
                ", password=" + getPassword() +
                ", role=" + getRole() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            return other.getUsername().equals(getUsername()) && other.getPassword().equals(getPassword());
        }
        return false;
    }

    public boolean usernameExists(){
        for(User u: users){
            if(u.getUsername().equals(this.getUsername()))
                return true;
        }
        return false;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static User getIfExists(User potentialUser) {
        for(User user: getUsers())
            if (user.equals(potentialUser))
                return user;
        return null;
    }

    public static ArrayList<User> getSearchResults(String searchText) {
        // don't do it this way, build a regex
        ArrayList<User> searchResults = new ArrayList<>();
        for(User User: getUsers())
            if (User.getUsername().matches(".*"+searchText+".*"))
                searchResults.add(User);
        return searchResults;
    }

    public static ArrayList<User> getUsers () {
        if (users.size() == 0){
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));
                while (true) {
                    User temp = (User) inputStream.readObject();
                    if (temp == null)
                        break;
                    users.add(temp);
                }
                inputStream.close();
            } catch (EOFException eofException) {
                System.out.println("End of file reached!");
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public boolean saveInFile() {
        boolean saved = super.save(User.DATA_FILE);
        if (saved)
            users.add(this);
        return saved;
    }

    @Override
    public boolean updateFile() {
        try {
            FileHandler.overwriteCurrentListToFile(DATA_FILE, users);
        }
        catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid() {
        if (!username.matches("\\w+"))
            return false;
        if (!password.matches("\\w+"))
            return false;
        return true;

    }

    @Override
    public boolean deleteFromFile() {
        users.remove(this);
        try {
            FileHandler.overwriteCurrentListToFile(DATA_FILE, users);
        }
        catch (Exception e){
            users.add(this);
            System.out.println(Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }
}
