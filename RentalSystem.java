import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
//add the things for T1-2
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//add the things for 1-3
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class RentalSystem {
    //T1-1
    rivate static RentalSystem instance;
    //keep
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

//t1-1 can't new
private RentalSystem() {
        
    }
// t1-1 public get the only one
    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }

    //Task1-3：ass loadData
    //add vehicle, customer and rentalrecord
    private void loadData() {
        loadVehicles();
        loadCustomers();
        loadRentalRecords();
        System.out.println("data loading completed！");
    }

    // loading vehicle data in vehicles.txt
    private void loadVehicles() {
        File file = new File("vehicles.txt");
        if (!file.exists()) return; //skip if the file does not exist

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 5) continue; // skip if the format is worng

                String licensePlate = parts[0];
                String make = parts[1];
                String model = parts[2];
                int year = Integer.parseInt(parts[3]);
                String vehicleType = parts[4];

     //create the correspoonding instance by vehicle type
      Vehicle vehicle = null;
                switch (vehicleType) {
                    case "Car":
                        vehicle = new Car(make, model, year, 4); // Default 4 seats
                        break;
                    case "Minibus":
                        vehicle = new Minibus(make, model, year, false); // Default no accessibility
                        break;
                    case "PickupTruck":
                        vehicle = new PickupTruck(make, model, year, 1.5, false); // Default 1.5m, no trailer
                        break;
                    case "SportCar":
                        vehicle = new SportCar(make, model, year, 2, 300, true); // Default 2 seats
                        break;
                    default:
                        System.err.println("unknown vehicle type：" + vehicleType);
                        continue;
                }
                vehicle.setLicensePlate(licensePlate);
                vehicle.setStatus(Vehicle.VehicleStatus.Available); // Available by default after loading
                vehicles.add(vehicle);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load vehicle data：" + e.getMessage());
        }
    }

    // Load customer data from customers.txt
    private void loadCustomers() {
        File file = new File("customers.txt");
        if (!file.exists()) return; // skip if the item does not exis

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 2) continue; // skip if format error 

                int customerId = Integer.parseInt(parts[0]);
                String customerName = parts[1];
                customers.add(new Customer(customerId, customerName));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load customer data：" + e.getMessage());
        }
    }

    // Load rental record data from rental_records.txt
    private void loadRentalRecords() {
        File file = new File("rental_records.txt");
        if (!file.exists()) return; // skip if the file does not exist

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 5) continue; // skip if format error 

                String recordType = parts[0];
                String licensePlate = parts[1];
                int customerId = Integer.parseInt(parts[2]);
                LocalDate recordDate = LocalDate.parse(parts[3]);
                double totalAmount = Double.parseDouble(parts[4]);

                // locate the corresponding vehicle and customer 
                Vehicle vehicle = findVehicleByPlate(licensePlate);
                Customer customer = findCustomerById(customerId);
                if (vehicle != null && customer != null) {
                    // update vehicle status during recording.
                    if (recordType.equals("RENT")) {
                        vehicle.setStatus(Vehicle.VehicleStatus.Rented);
                    } else if (recordType.equals("RETURN")) {
                        vehicle.setStatus(Vehicle.VehicleStatus.Available);
                    }
                    rentalHistory.addRecord(new RentalRecord(vehicle, customer, recordDate, totalAmount, recordType));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed, load rental records：" + e.getMessage());
        }
    }



    // task1-2( add 3 file saving)
    // save vehicles to vehicles.txt
    private void saveVehicle(Vehicle vehicle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true))) {
            writer.write(String.format("%s|%s|%s|%d|%s%n",
                    vehicle.getLicensePlate(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getClass().getSimpleName()));
        } catch (IOException e) {
            System.err.println("save vehicles fail：" + e.getMessage());
        }
    }

    //save customer to customers.txt
    private void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(String.format("%d|%s%n",
                    customer.getCustomerId(),
                    customer.getCustomerName()));
        } catch (IOException e) {
            System.err.println("save customer fail：" + e.getMessage());
        }
    }

    //save rental_rocrd to rental_records.txt
    private void saveRecord(RentalRecord record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true))) {
            writer.write(String.format("%s|%s|%d|%s|%.2f%n",
                    record.getRecordType(),
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerId(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()));
        } catch (IOException e) {
            System.err.println("save rental records fail ：" + e.getMessage());
        }
    }


//Task1-2 change the old to , add the save and double check
    //change addVehicle and use saveVehicle
    public boolean addVehicle(Vehicle vehicle) {
        //double check is the liceense plate exist
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
            System.out.println("fail: liceense plate " + vehicle.getLicensePlate() + "existed！");
            return false;
        }
        vehicles.add(vehicle);
        saveVehicle(vehicle); // Task1-2：change saving way
        return true;
    }

    //change addCustomer to , add the save and double check
    public boolean addCustomer(Customer customer) {
        //double check is the customerID exist or not
        if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("Fail：CustomerID" + customer.getCustomerId() + "existed！");
            return false;
        }
        customers.add(customer);
        saveCustomer(customer); // Task1-2：change save way
        return true;
    }
   
    // change rentVehicle: add saveRecord
    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            rentalHistory.addRecord(record);
            saveRecord(record); // Task1-2：change save way
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }
    // change returnVehicle: add saveRecord
    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            rentalHistory.addRecord(record);
            saveRecord(record); // Task1-2：change saving way
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    
    
    //kepp
    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
}