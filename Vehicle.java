//add
import java.time.LocalDate;

public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;
//not chang the state
    public enum VehicleStatus { Available, Held, Rented, UnderMaintenance, OutOfService }

//task1-5 capitalize helper method, eliminating redundant logic
    public Vehicle(String make, String model, int year, String licensePlate) {
        //t1-5 make and model, keep null
    	this.make = capitalize(make);
        this.model = capitalize(model);
        this.year = year;
        this.licensePlate = licensePlate;
        this.status = VehicleStatus.Available; // default state is enabled
    }

    //Task1-5 Capitalize the first letter, lowercase the rest (handle null and empty strings)
    private String capitalize(String input) {
        // Retain old: null or empty strings return null directly.
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        // Capitalize first letter , Lowercase all remaining characters
        String trimmedInput = input.trim();
        return trimmedInput.substring(0, 1).toUpperCase() + trimmedInput.substring(1).toLowerCase();
    }
    //keep getter/setter
    public String getLicensePlate() { return licensePlate; }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate == null ? null : licensePlate.toUpperCase();
    }

    public String getMake() { return make; }

    public void setMake(String make) {
        this.make = capitalize(make); // same
    }

    public String getModel() { return model;}

    public void setModel(String model) {
        this.model = capitalize(model); // same
    }

    public int getYear() { return year; }

    public void setYear(int year) {this.year = year;}

    public VehicleStatus getStatus() { return status; }

    public void setStatus(VehicleStatus status) {this.status = status;}

    public abstract double calculateRentalCost(int days);
}


