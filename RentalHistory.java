import java.util.ArrayList;
import java.util.List;

public class RentalHistory {
    private List<RentalRecord> rentalRecords; 

    // Create empty history (starts with no records)
    public RentalHistory() {
        this.rentalRecords = new ArrayList<>();
    }

    // Add a new record to history
    public void addRecord(RentalRecord record) {
        rentalRecords.add(record);
    }

    // Get all records (so other classes can show them)
    public List<RentalRecord> getRentalHistory() {
        return new ArrayList<>(rentalRecords); // Return a copy (no one can change the original)
    }
}