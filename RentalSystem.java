import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

// Core system 
public class RentalSystem {
    private static RentalSystem instance; // Only one copy 
    private List<Vehicle> vehicles;       
    private List<Customer> customers;     
    private RentalHistory rentalHistory;  

    // no one can make a new system 
    private RentalSystem() {
        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentalHistory = new RentalHistory();
        loadData(); // Load saved data (vehicles, customers, records)
    }

    // Get the only system instance (create if it doesn't exist)
    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }

        //Updated method - Return boolean (true = success, false = failure)
    public boolean rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
           if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            return true; // Success - return true
            } else {
            System.out.println("Vehicle is not available to rent.");
            return false; // Failure - return false
            }
    }

//Updated method - Return boolean (true = success, false = failure)
    public boolean returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            return true; // Success - return true
        } else {
            System.out.println("This vehicle is not rented.");
            return false; // Failure - return false
        }
    }
    // Load all saved data 
    private void loadData() {
        loadVehicles();
        loadCustomers();
        loadRentalRecords();
        System.out.println("Data loading completed!");
    }

    // Load vehicles from "vehicles.txt" 
    private void loadVehicles() {
        File file = new File("vehicles.txt");
        if (!file.exists()) return; // Do nothing if file doesn't exist

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) { // Read each line
                String[] parts = line.split("\\|"); // Split line by "|"
                if (parts.length != 5) continue; // Skip bad format lines

                String licensePlate = parts[0];
                String make = parts[1];
                String model = parts[2];
                int year = Integer.parseInt(parts[3]);
                String vehicleType = parts[4];

                Vehicle vehicle = null;
                // Make the right type of vehicle (car, minibus, etc.)
                switch (vehicleType) {
                    case "Car":
                        vehicle = new Car(make, model, year, licensePlate, 4);
                        break;
                    case "Minibus":
                        vehicle = new Minibus(make, model, year, licensePlate, false);
                        break;
                    case "PickupTruck":
                        vehicle = new PickupTruck(make, model, year, licensePlate, 1.5, false);
                        break;
                    case "SportCar":
                        vehicle = new SportCar(make, model, year, licensePlate, 2, 300, true);
                        break;
                    default:
                        System.err.println("Unknown vehicle type: " + vehicleType);
                        continue;
                }
                vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
                vehicles.add(vehicle);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load vehicles: " + e.getMessage());
        }
    }

    // Load customers from "customers.txt" 
    private void loadCustomers() {
        File file = new File("customers.txt");
        if (!file.exists()) return; // Do nothing if file doesn't exist

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) { // Read each line
                String[] parts = line.split("\\|"); // Split line by "|"
                if (parts.length != 2) continue; // Skip bad format lines

                int customerId = Integer.parseInt(parts[0]);
                String customerName = parts[1];
                customers.add(new Customer(customerId, customerName));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load customers: " + e.getMessage());
        }
    }

    // Load rental records from "rental_records.txt" 
    private void loadRentalRecords() {
        File file = new File("rental_records.txt");
        if (!file.exists()) return; // Do nothing if file doesn't exist

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) { // Read each line
                String[] parts = line.split("\\|"); // Split line by "|"
                if (parts.length != 5) continue; // Skip bad format lines

                String recordType = parts[0];
                String licensePlate = parts[1];
                int customerId = Integer.parseInt(parts[2]);
                LocalDate date = LocalDate.parse(parts[3]);
                double amount = Double.parseDouble(parts[4]);

                // Find the vehicle and customer for this record
                Vehicle vehicle = findVehicleByPlate(licensePlate);
                Customer customer = findCustomerById(customerId);
                if (vehicle != null && customer != null) {
                    // Update vehicle status (rented/available)
                    if (recordType.equals("RENT")) {
                        vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
                    } else if (recordType.equals("RETURN")) {
                        vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
                    }
                    RentalRecord record = new RentalRecord(vehicle, customer, date, amount, recordType);
                    rentalHistory.addRecord(record);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load rental records: " + e.getMessage());
        }
    }

    // Save a vehicle to "vehicles.txt" file
    private void saveVehicle(Vehicle vehicle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true))) {
            // Write vehicle info (plate|make|model|year|type)
            writer.write(String.format("%s|%s|%s|%d|%s%n",
                    vehicle.getLicensePlate(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getClass().getSimpleName()));
        } catch (IOException e) {
            System.err.println("Failed to save vehicle: " + e.getMessage());
        }
    }

    // Save a customer to "customers.txt" file
    private void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            // Write customer info (ID|name)
            writer.write(String.format("%d|%s%n",
                    customer.getCustomerId(),
                    customer.getCustomerName()));
        } catch (IOException e) {
            System.err.println("Failed to save customer: " + e.getMessage());
        }
    }

    // Save a rental record to "rental_records.txt" file
    private void saveRecord(RentalRecord record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true))) {
            // Write record info (type|plate|customerID|date|amount)
            writer.write(String.format("%s|%s|%d|%s|%.2f%n",
                    record.getRecordType(),
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerId(),
                    record.getDate().toString(),
                    record.getTotalAmount()));
        } catch (IOException e) {
            System.err.println("Failed to save record: " + e.getMessage());
        }
    }

    // Add a new vehicle to the system (check for duplicate plate first)
    public boolean addVehicle(Vehicle vehicle) {
        // Check if plate already exists
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
            System.out.println("Fail: License plate " + vehicle.getLicensePlate() + " already exists!");
            return false;
        }
        // Check if plate is empty
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            System.out.println("Error: License plate cannot be empty!");
            return false;
        }
        vehicles.add(vehicle);
        saveVehicle(vehicle); // Save to file
        System.out.println("Vehicle added successfully! Plate: " + vehicle.getLicensePlate());
        return true;
    }

    // Add a new customer to the system (check for duplicate ID first)
    public boolean addCustomer(Customer customer) {
        // Check if customer ID already exists
        if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("Fail: Customer ID " + customer.getCustomerId() + " already exists!");
            return false;
        }
        // Check if ID is positive
        if (customer.getCustomerId() <= 0) {
            System.out.println("Error: Customer ID must be a positive number!");
            return false;
        }
        customers.add(customer);
        saveCustomer(customer); // Save to file
        System.out.println("Customer added successfully! " + customer);
        return true;
    }

    // Rent a vehicle to a customer
    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED); // Mark as rented
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record); // Save to history
            saveRecord(record); // Save to file
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        } else {
            System.out.println("Vehicle is not available to rent.");
        }
    }

    // Return a rented vehicle
    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE); // Mark as available
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record); // Save to history
            saveRecord(record); // Save to file
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        } else {
            System.out.println("This vehicle is not rented.");
        }
    }

    // Show vehicles by status (e.g., only available ones)
    public void displayVehicles(Vehicle.VehicleStatus status) {
        System.out.println("\n=== " + (status == null ? "All Vehicles" : status + " Vehicles") + " ===");
        System.out.printf("|%-20s | %-12s | %-10s |%n", "Info", "Plate", "Status");
        System.out.println("|----------------------------------------|------------|------------|");

        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                // Show vehicle info (truncate if too long)
                String info = vehicle.getInfo().length() > 20 ? vehicle.getInfo().substring(0, 20) : vehicle.getInfo();
                System.out.printf("|%-20s | %-12s | %-10s |%n", info, vehicle.getLicensePlate(), vehicle.getStatus());
            }
        }
        if (!found) {
            System.out.println("  No vehicles found.");
        }
        System.out.println();
    }

    // Show all registered customers
    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c);
        }
    }

    // Show all rental/return history
    public void displayRentalHistory() {
        List<RentalRecord> records = rentalHistory.getRentalHistory();
        if (records.isEmpty()) {
            System.out.println("  No rental history found.");
            return;
        }

        System.out.println("\n=== Rental History ===");
        System.out.printf("|%-5s | %-12s | %-20s | %-10s | %-8s |%n",
                "Type", "Plate", "Customer", "Date", "Amount");
        System.out.println("|-------|------------|----------------------|------------|----------|");

        for (RentalRecord record : records) {
            // Truncate customer name if too long
            String name = record.getCustomer().getCustomerName().length() > 20 ? 
                    record.getCustomer().getCustomerName().substring(0, 20) : 
                    record.getCustomer().getCustomerName();
            System.out.printf("|%-5s | %-12s | %-20s | %-10s | $%-7.2f |%n",
                    record.getRecordType(),
                    record.getVehicle().getLicensePlate(),
                    name,
                    record.getDate(),
                    record.getTotalAmount());
        }
        System.out.println();
    }

    // Find a vehicle by its license plate (case-insensitive)
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null; // Return null if not found
    }

    // Find a customer by their ID
    public Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id) {
                return c;
            }
        }
        return null; // Return null if not found
    }
}