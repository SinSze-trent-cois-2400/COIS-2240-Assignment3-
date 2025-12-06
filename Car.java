
public class Car extends Vehicle implements Rentable {
    private int numSeats; // Number of seats 

    // Create a new car
    public Car(String make, String model, int year, String licensePlate, int numSeats) {
        super(make, model, year, licensePlate); // Use parent class (Vehicle) constructor
        this.numSeats = numSeats;
    }

    // Calculate rental cost: $100 per day + $10 per seat
    @Override
    public double calculateRentalCost(int days) {
        return days * 100 + numSeats * 10;
    }
    @Override
    public void rentVehicle() {
        setStatus(VehicleStatus.RENTED);
        System.out.println("Car " + getLicensePlate() + " has been rented.");
    }
    
    @Override
    public void returnVehicle() {
        setStatus(VehicleStatus.AVAILABLE);
        System.out.println("Car " + getLicensePlate() + " has been returned.");
    }

    @Override
    public String getInfo() {
        return getBasicInfo() + " | Seats: " + numSeats;
    }
    
    public int getNumSeats() { return numSeats; }
}
