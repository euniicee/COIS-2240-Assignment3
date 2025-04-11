import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VehicleRentalTest {

    class TestVehicle extends Vehicle {
        public TestVehicle() {
            super("TestMake", "TestModel", 2023);
        }

        // Publicly accessible version of the validation logic for testing
        public boolean testIsValid(String plate) {
            return plate != null && plate.matches("^[A-Z]{3}[0-9]{3}$");
        }
    }

    RentalSystem system;
    Vehicle vehicle;
    Customer customer;

    @BeforeEach
    void setUp() {
        system = RentalSystem.getInstance();
        system.reset(); 
        vehicle = new TestVehicle();
        customer = new Customer(1, "George");
        system.addVehicle(vehicle);
        system.addCustomer(customer);
    }
    @Test
    void testLicensePlateValidation() {
        TestVehicle tester = new TestVehicle();

        // Valid plates
        assertTrue(tester.testIsValid("AAA100"));
        assertTrue(tester.testIsValid("ABC567"));
        assertTrue(tester.testIsValid("ZZZ999"));

        // Invalid plates
        assertFalse(tester.testIsValid(""));
        assertFalse(tester.testIsValid(null));  
        assertFalse(tester.testIsValid("AAA10000"));
        assertFalse(tester.testIsValid("ZZZ99"));

        // Invalid plates should throw exception
        assertThrows(IllegalArgumentException.class, () -> tester.setLicensePlate(""));
        assertThrows(IllegalArgumentException.class, () -> tester.setLicensePlate(null));
        assertThrows(IllegalArgumentException.class, () -> tester.setLicensePlate("AAA10000"));
        assertThrows(IllegalArgumentException.class, () -> tester.setLicensePlate("ZZZ99"));
    }

    
    @Test
    void testRentAndReturnVehicle() {
        // Set a valid plate
         assertDoesNotThrow(() -> vehicle.setLicensePlate("AAA123"));

        // Initially available
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getStatus());

        // Rent
        boolean rentSuccess = system.rentVehicle(vehicle, customer);
        assertTrue(rentSuccess);
        assertEquals(Vehicle.VehicleStatus.RENTED, vehicle.getStatus());

        // Try renting again
        boolean rentAgain = system.rentVehicle(vehicle, customer);
        assertFalse(rentAgain);

        // Return
        boolean returnSuccess = system.returnVehicle(vehicle, customer);
        assertTrue(returnSuccess);
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getStatus());

        // Try returning again
        boolean returnAgain = system.returnVehicle(vehicle, customer);
        assertFalse(returnAgain);
    }
    @Test
    public void testSingletonRentalSystem() throws Exception {
        // Access the constructor of RentalSystem using reflection
        Constructor<RentalSystem> constructor = RentalSystem.class.getDeclaredConstructor();

        // Get the access modifier of the constructor (should be private)
        int modifiers = constructor.getModifiers();

        // Assert that the constructor is private
        assertTrue(Modifier.isPrivate(modifiers), "Constructor should be private to enforce Singleton pattern.");

        // Use getInstance() to get the singleton instance
        RentalSystem instance = RentalSystem.getInstance();

        // Assert the instance is not null
        assertNotNull(instance, "Singleton instance should not be null.");
    }
}