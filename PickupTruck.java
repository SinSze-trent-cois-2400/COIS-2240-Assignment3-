
public class PickupTruck extends Vehicle implements Rentable {
    private double cargoSize; // Size of the truck bed 
    private boolean hasTrailer; // True = has a trailer

    // Create a new pickup truck
    public PickupTruck(String make, String model, int year, String licensePlate, double cargoSize, boolean hasTrailer) {
        super(make, model, year, licensePlate); 
        if (cargoSize <= 0) throw new IllegalArgumentException("Cargo size must be more than 0");
        this.cargoSize = cargoSize;
        this.hasTrailer = hasTrailer;
    }

    // Calculate rental cost: $150 per day + $50 per cubic meter of cargo
    @Override
    public double calculateRentalCost(int days) {
        return days * 150 + cargoSize * 50;
    }

    @Override
    public void rentVehicle() {
        setStatus(VehicleStatus.RENTED);
        System.out.println("Pickup Truck " + getLicensePlate() + " has been rented.");
    }

    @Override
    public void returnVehicle() {
        setStatus(VehicleStatus.AVAILABLE);
        System.out.println("Pickup Truck " + getLicensePlate() + " has been returned.");
    }

    @Override
    public String getInfo() {
        return getBasicInfo() + " | Cargo Size: " + cargoSize + "m | Trailer: " + (hasTrailer ? "Yes" : "No");
    }

    public double getCargoSize() { return cargoSize; }
    public boolean hasTrailer() { return hasTrailer; }
}