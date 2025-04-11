import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VehicleRentalTest {
	class TestVehicle extends Vehicle{
		public TestVehicle() {
			super("TestMake","TestModel", 2023);
		}
		
		public boolean testIsValid(String plate) {
			return plate != null && plate.matches("^[A-Z]{3}\\d{3}$");
		}
	}

	
	@Test
	void testLicensePlateValidation() {
		TestVehicle tester = new TestVehicle();
		
		assertTrue(tester.testIsValid("AAA100"));
		assertTrue(tester.testIsValid("ABC567"));
		assertTrue(tester.testIsValid("ZZZ999"));

		assertFalse(tester.testIsValid(""));
		assertFalse(tester.testIsValid(null));
		assertFalse(tester.testIsValid("AAA10000"));
		assertFalse(tester.testIsValid("ZZZ99"));
	 
		assertThrows(IllegalArgumentException.class,() -> {tester.setLicensePlate("");});
		assertThrows(IllegalArgumentException.class,() -> {tester.setLicensePlate(null);});
		assertThrows(IllegalArgumentException.class,() -> {tester.setLicensePlate("AAA10000");});
		assertThrows(IllegalArgumentException.class,() -> {tester.setLicensePlate("ZZZ99");});
    
		//assertThrows(IllegalArgumentException.class,() -> { Vehicle v4 = new TestVehicle();v4.setLicensePlate("");});
    //assertThrows(IllegalArgumentException.class,() -> { Vehicle v5 = new TestVehicle();v5.setLicensePlate(null);});
	//assertThrows(IllegalArgumentException.class,() -> { Vehicle v6 = new TestVehicle();v6.setLicensePlate("AAA10000");});
	//assertThrows(IllegalArgumentException.class,() -> { Vehicle v7 = new TestVehicle();v7.setLicensePlate("ZZZ99");});

	}
}
