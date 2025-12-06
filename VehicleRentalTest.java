
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;

public class VehicleRentalTest {
    private RentalSystem rentalSystem;
    private Vehicle testCar;
    private Customer testCustomer;

    // Run before each test to set up shared objects
    @BeforeEach
    void setUp() {
        rentalSystem = RentalSystem.getInstance();
        // Create valid test vehicle and customer (uses valid plate)
        testCar = new Car("Toyota", "Corolla", 2019, "AAA111", 4);
        testCustomer = new Customer(101, "Alice");
        // Add them to the system (ensure no duplicates)
        rentalSystem.addCustomer(testCustomer);
        rentalSystem.addVehicle(testCar);
    }

    
    // Test 1: License Plate Validation 
    @Test
    void testLicensePlate() {
        // Valid plates: should not throw errors
        assertDoesNotThrow(() -> new Car("Honda", "Civic", 2021, "ABC567", 5));
        assertDoesNotThrow(() -> new Minibus("Ford", "Focus", 2024, "ZZZ999", false));
        assertDoesNotThrow(() -> new PickupTruck("Chevrolet", "Silverado", 2023, "XYZ123", 2.0, true));

        // Invalid plates: should throw IllegalArgumentException
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
                () -> new Car("Toyota", "Camry", 2020, "", 5)); // Empty
        assertEquals("Invalid license plate! Must be 3 letters + 3 numbers (e.g., AAA111)", e1.getMessage());

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
                () -> new Minibus("Honda", "Odyssey", 2022, "AAA1000", true)); // 7 characters
        assertEquals("Invalid license plate! Must be 3 letters + 3 numbers (e.g., AAA111)", e2.getMessage());

        IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class,
                () -> new PickupTruck("Ford", "F-150", 2021, "ZZZ99", false)); // 5 characters
        assertEquals("Invalid license plate! Must be 3 letters + 3 numbers (e.g., AAA111)", e3.getMessage());

        IllegalArgumentException e4 = assertThrows(IllegalArgumentException.class,
                () -> new Car("Toyota", "Prius", 2020, "123AAA", 4)); // Numbers first
        assertEquals("Invalid license plate! Must be 3 letters + 3 numbers (e.g., AAA111)", e4.getMessage());
    }

   
    // Test 2: Rent/Return Vehicle Validation 
    @Test
    void testRentAndReturnVehicle() {
        // Initial state: vehicle is available
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, testCar.getStatus());

        // Rent the vehicle: should succeed
        boolean rentSuccess = rentalSystem.rentVehicle(testCar, testCustomer, LocalDate.now(), 150.0);
        assertTrue(rentSuccess);
        assertEquals(Vehicle.VehicleStatus.RENTED, testCar.getStatus());

        // Try renting again: should fail
        boolean rentFail = rentalSystem.rentVehicle(testCar, testCustomer, LocalDate.now(), 150.0);
        assertFalse(rentFail);
        assertEquals(Vehicle.VehicleStatus.RENTED, testCar.getStatus());

        // Return the vehicle: should succeed
        boolean returnSuccess = rentalSystem.returnVehicle(testCar, testCustomer, LocalDate.now(), 0.0);
        assertTrue(returnSuccess);
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, testCar.getStatus());

        // Try returning again: should fail
        boolean returnFail = rentalSystem.returnVehicle(testCar, testCustomer, LocalDate.now(), 0.0);
        assertFalse(returnFail);
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, testCar.getStatus());
    }

   
    // Test 3: Singleton Validation 
    @Test
    void testSingletonRentalSystem() throws Exception {
        // Step 1: Get RentalSystem's constructor using reflection
        Constructor<RentalSystem> constructor = RentalSystem.class.getDeclaredConstructor();

        // Step 2: Verify constructor is private (prevents direct instantiation)
        int modifiers = constructor.getModifiers();
        assertTrue(Modifier.isPrivate(modifiers));

        // Step 3: Verify getInstance() returns a non-null instance
        RentalSystem instance1 = RentalSystem.getInstance();
        assertNotNull(instance1);

        // Step 4: Verify getInstance() returns the SAME instance every time
        RentalSystem instance2 = RentalSystem.getInstance();
        assertSame(instance1, instance2);
    }
}