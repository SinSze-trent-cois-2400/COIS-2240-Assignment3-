
public class Minibus extends Vehicle implements Rentable {
    private boolean isAccessible; // True = for people with disabilities

 
    public Minibus(String make, String model, int year, String licensePlate, boolean isAccessible) {
        super(make, model, year, licensePlate); 
        this.isAccessible = isAccessible;
    }
    
    @Override
    public double calculateRentalCost(int days) {
        return days * 200;
    }

    @Override
    public void rentVehicle() {
        setStatus(VehicleStatus.RENTED);
        System.out.println("Minibus " + getLicensePlate() + " has been rented.");
    }

  
    @Override
    public void returnVehicle() {
        setStatus(VehicleStatus.AVAILABLE);
        System.out.println("Minibus " + getLicensePlate() + " has been returned.");
    }

    @Override
    public String getInfo() {
        return getBasicInfo() + " | Accessible: " + (isAccessible ? "Yes" : "No");
    }


    public boolean isAccessible() { return isAccessible; }
}