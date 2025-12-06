
public class SportCar extends Vehicle implements Rentable {
    private int numSeats;    
    private int horsepower;  
    private boolean hasTurbo; // True = has turbo 

    // Create a new sport car
    public SportCar(String make, String model, int year, String licensePlate, int numSeats, int horsepower, boolean hasTurbo) {
        super(make, model, year, licensePlate); /
        this.numSeats = numSeats;
        this.horsepower = horsepower;
        this.hasTurbo = hasTurbo;
    }

    @Override
    public double calculateRentalCost(int days) {
        return days * 300 + horsepower * 0.5;
    }

    public void rentVehicle() {
        setStatus(VehicleStatus.RENTED);
        System.out.println("SportCar " + getLicensePlate() + " has been rented.");
    }

    @Override
    public void returnVehicle() {
        setStatus(VehicleStatus.AVAILABLE);
        System.out.println("SportCar " + getLicensePlate() + " has been returned.");
    }

    @Override
    public String getInfo() {
        return getBasicInfo() + " | Seats: " + numSeats + " | HP: " + horsepower + " | Turbo: " + (hasTurbo ? "Yes" : "No");
    }

    public int getNumSeats() { return numSeats; }
    public int getHorsepower() { return horsepower; }
    public boolean hasTurbo() { return hasTurbo; }
}