import java.time.LocalDate;

public class RentalRecord {
    private Vehicle vehicle;       
    private Customer customer;     
    private LocalDate date;        
    private double amount;         
    private String recordType;     

    // Create a new record
    public RentalRecord(Vehicle vehicle, Customer customer, LocalDate date, double amount, String recordType) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.date = date;
        this.amount = amount;
        this.recordType = recordType.toUpperCase(); 
    }

    public String getRecordType() { return recordType; }
    public Vehicle getVehicle() { return vehicle; }
    public Customer getCustomer() { return customer; }
    public LocalDate getDate() { return date; }
    public double getTotalAmount() { return amount; }
}