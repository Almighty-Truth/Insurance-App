public class HomeClaim extends Claim {
    String address;
    
    public HomeClaim(String description, double amount, String address)
    {
        super(description, amount);
        this.address = address;
    }
    @Override 
    public String getClaimType()
    {
        return "Home";
    }
    @Override
    public String toString()
    {
        return super.toString() + "Address: " + address;
    }
}
