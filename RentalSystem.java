import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
//add the things for T1-2
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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