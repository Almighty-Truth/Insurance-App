public class AutoClaim extends Claim 
{
    String licensePlate; 

    public AutoClaim(String description, double amount, String licensePlate)
    {
        super(description, amount);
        this.licensePlate = licensePlate;

    }

    @Override 
    public String getClaimType()
    {
        return "Auto";
    }

    @Override 
    public String toString()
    {
        return super.toString() + "License Plate: " + licensePlate; 
    }

}
