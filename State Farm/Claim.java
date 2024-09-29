import java.io.*;
public abstract class Claim implements Serializable
{
    String description; 
    double amount;

    public Claim(String description, double amount)
    {
        this.description = description;
        this.amount = amount;
    }

    public abstract String getClaimType();
    @Override
    public String toString()
    {
        return getClaimType() + "Claim: " + description + "Amount: " + amount;
    }
}
