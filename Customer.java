
public class Customer {
    private int customerId;   
    private String customerName; 

    // Create a new customer
    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = capitalize(customerName); 
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + " | Name: " + customerName;
    }

    public int getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
}