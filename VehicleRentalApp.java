import java.util.Scanner;
import java.time.LocalDate;


public class VehicleRentalApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RentalSystem rentalSystem = RentalSystem.getInstance(); // Get the system

        while (true) { // Loop forever until user exits
            // main menu
            System.out.println("\n1: Add Vehicle\n" + 
                              "2: Add Customer\n" + 
                              "3: Rent Vehicle\n" + 
                              "4: Return Vehicle\n" + 
                              "5: Display Available Vehicles\n" + 
                              "6: Show Rental History\n" + 
                              "0: Exit\n");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the Enter key from input

            switch (choice) {
                // Add a new vehicle
                case 1:
                    System.out.println("  1: Car\n" + 
                                       "  2: Minibus\n" + 
                                       "  3: Pickup Truck");
                    int type = scanner.nextInt();
                    scanner.nextLine();

                    // Get vehicle details from user
                    System.out.print("Enter license plate: ");
                    String plate = scanner.nextLine().toUpperCase();
                    System.out.print("Enter make (brand): ");
                    String make = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    Vehicle vehicle = null;


                    // Make the right type of vehicle
                    try {
                if (type == 1) {
                    System.out.print("Enter number of seats: ");
                    int seats = scanner.nextInt();
                    vehicle = new Car(make, model, year, plate, seats);
                } else if (type == 2) {
                    System.out.print("Is accessible for disabilities? (true/false): ");
                    boolean isAccessible = scanner.nextBoolean();
                    vehicle = new Minibus(make, model, year, plate, isAccessible);
                } else if (type == 3) {
                        System.out.print("Enter cargo size (in meters): ");
                        double cargoSize = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Has trailer? (true/false): ");
                        boolean hasTrailer = scanner.nextBoolean();
                        vehicle = new PickupTruck(make, model, year, plate, cargoSize, hasTrailer);
                    } else {
                        System.out.println("Invalid vehicle type!");
                    }
                    
                    if (vehicle != null) {
                        rentalSystem.addVehicle(vehicle);
                    }
                    catch (IllegalArgumentException e) {
                // Catch invalid plate error and show user-friendly message
                System.out.println("Error adding vehicle: " + e.getMessage());
            }
                    break;

                // Add a new customer
                case 2:
                    System.out.print("Enter customer ID (number): ");
                    int cid = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter customer name: ");
                    String cname = scanner.nextLine();

                    rentalSystem.addCustomer(new Customer(cid, cname));
                    break;
                    
                // Rent a vehicle
                case 3:
                    rentalSystem.displayVehicles(Vehicle.VehicleStatus.AVAILABLE); // Show available cars

                    System.out.print("Enter vehicle license plate: ");
                    String rentPlate = scanner.nextLine().toUpperCase();

                    System.out.println("Registered Customers:");
                    rentalSystem.displayAllCustomers(); // Show all customers

                    // Get customer ID (make sure it's a number)
                    int cidRent = -1;
                    while (true) {
                        System.out.print("Enter customer ID (only numbers): ");
                        if (scanner.hasNextInt()) {
                            cidRent = scanner.nextInt();
                            break;
                        } else {
                            System.out.println("Error: Please enter a number!");
                            scanner.next(); // Clear bad input
                        }
                    }

                    System.out.print("Enter rental amount (money): ");
                    double rentAmount = scanner.nextDouble();
                    scanner.nextLine();

                    // Find vehicle and customer
                    Vehicle vehicleToRent = rentalSystem.findVehicleByPlate(rentPlate);
                    Customer customerToRent = rentalSystem.findCustomerById(cidRent);

                    if (vehicleToRent == null || customerToRent == null) {
                        System.out.println("Vehicle or customer not found.");
                    } else {
                        rentalSystem.rentVehicle(vehicleToRent, customerToRent, LocalDate.now(), rentAmount);
                    }
                    break;

                //Return a vehicle
                case 4:
                    rentalSystem.displayVehicles(Vehicle.VehicleStatus.RENTED); // Show rented cars

                    System.out.print("Enter vehicle license plate: ");
                    String returnPlate = scanner.nextLine().toUpperCase();
                    
                    System.out.println("Registered Customers:");
                    rentalSystem.displayAllCustomers(); // Show all customers

                    // Get customer ID (make sure it's a number)
                    int cidReturn = -1;
                    while (true) {
                        System.out.print("Enter customer ID (only numbers): ");
                        if (scanner.hasNextInt()) {
                            cidReturn = scanner.nextInt();
                            break;
                        } else {
                            System.out.println("Error: Please enter a number!");
                            scanner.next(); // Clear bad input
                        }
                    }

                    System.out.print("Enter extra return fees (if any): ");
                    double returnFees = scanner.nextDouble();
                    scanner.nextLine();

                    //Find vehicle and customer
                    Vehicle vehicleToReturn = rentalSystem.findVehicleByPlate(returnPlate);
                    Customer customerToReturn = rentalSystem.findCustomerById(cidReturn);

                    if (vehicleToReturn == null || customerToReturn == null) {
                        System.out.println("Vehicle or customer not found.");
                    } else {
                        rentalSystem.returnVehicle(vehicleToReturn, customerToReturn, LocalDate.now(), returnFees);
                    }
                    break;
                    
                // Show all available vehicles
                case 5:
                    rentalSystem.displayVehicles(Vehicle.VehicleStatus.AVAILABLE);
                    break;
                
                // Show all rental history
                case 6:
                    rentalSystem.displayRentalHistory();
                    break;
                    
                // Exit the program
                case 0:
                    scanner.close();
                    System.out.println("Exiting program... Goodbye!");
                    System.exit(0);
                
                // Invalid menu choice
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
