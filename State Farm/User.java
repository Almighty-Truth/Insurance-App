import java.io.*;
import java.util.*;
public class User implements Serializable {
    String username; 
    String hashedPasword; 
    ArrayList<Claim> claims;

    public User(String username, String hashedPassword)
    {
        this.username = username;
        this.hashedPasword = hashedPassword;
        this.claims = new ArrayList<>();
    }
}
