
public abstract class Vehicle {
    // Vehicle status available (can rent) or rented (already taken)
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
        // Use setLicensePlate to apply validation
        tsetLicensePlate(licensePlate);
        this.status = VehicleStatus.AVAILABLE; // New vehicles are available to rent
    }

    // Validate plate format (3 letters + 3 number)
    protected boolean isValidPlate(String plate) {
        if (plate == null || plate.trim().isEmpty()) return false;
        return plate.matches("[A-Z]{3}[0-9]{3}"); // Regex for valid format
    }

    //Set plate with validation (throw error if invalid)
    private boolean isValidPlate(String plate) {
        if (plate == null || plate.trim().isEmpty()) return false;
        return plate.matches("[A-Z]{3}[0-9]{3}"); // Regex for valid format
    }

    // et plate with validation (throw error if invalid)
   public void setLicensePlate(String licensePlate) {
        String upperPlate = licensePlate.toUpperCase().trim();
        if (!isValidPlate(upperPlate)) {
            // 2.1: Throw exception for invalid plate (caught in VehicleRentalApp)
            throw new IllegalArgumentException("Invalid license plate! Must be 3 letters + 3 numbers (e.g., AAA111)");
        }
        this.licensePlate = upperPlate;
    }

    // Abstract methods (unchanged)
    public abstract double calculateRentalCost(int days);
    public abstract String getInfo();

    // Getters (allow other classes to read these values)
    public String getLicensePlate() { return licensePlate; }
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
}