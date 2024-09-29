public class HealthClaim extends Claim {
    String hospitalName;
    
    public HealthClaim(String description, double amount, String hospitalName)
    {
        super(description, amount);
        this.hospitalName = hospitalName;
    }
    @Override 
    public String getClaimType()
    {
        return "Health";
    }
    @Override 
    public String toString()
    {
        return super.toString() + "Hospital: " + hospitalName;
    }
}
