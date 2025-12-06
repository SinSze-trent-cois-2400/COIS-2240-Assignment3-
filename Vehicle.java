
public abstract class Vehicle {
    // Vehicle status: either available (can rent) or rented (already taken)
    public enum VehicleStatus {
        AVAILABLE, RENTED
    }

    protected String licensePlate; 
    protected String make;         
    protected String model;        
    protected int year;            
    protected VehicleStatus status;

    // Create a new vehicle (called when making a new car/minibus)
    public Vehicle(String make, String model, int year, String licensePlate) {
        this.make = capitalize(make);   // Make first letter uppercase 
        this.model = capitalize(model); // Same for model 
        this.year = year;
        this.licensePlate = licensePlate.toUpperCase(); // Plate in uppercase 
        this.status = VehicleStatus.AVAILABLE; // New vehicles are available to rent
    }

    // Make first letter of a word uppercase (simple helper)
    protected String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public abstract double calculateRentalCost(int days);

    public abstract String getInfo();

    // Show basic vehicle info (brand, model, year, plate)
    public String getBasicInfo() {
        return String.format("%s %s (%d) | Plate: %s", 
            make, model, year, licensePlate);
    }

    // Getters (allow other classes to read these values)
    public String getLicensePlate() { return licensePlate; }
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
}