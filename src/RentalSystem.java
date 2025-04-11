import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class RentalSystem {
	private static RentalSystem instance;
	public static RentalSystem getInstance() {
        // If the instance is null, create it
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
	}
	 private RentalSystem() {
	    loadData();  // Load the saved data when the object is created
	 }
	 
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
 // Method to save vehicle details to vehicles.txt
    public void saveVehicle(Vehicle vehicle) {
        // Try-with-resources automatically closes the FileWriter and PrintWriter when done.
        try (FileWriter fw = new FileWriter("vehicles.txt", true); // Open the file in append mode.
             PrintWriter writer = new PrintWriter(fw)) {
             
             // Check if the vehicle is an instance of Car.
             if (vehicle instanceof Car) {
                 // Down-cast to Car to access car-specific attributes.
                 Car car = (Car) vehicle;
                 // Write CSV data including license plate, make, model, year, vehicle type ("Car"), and number of seats.
                 writer.println(
                    vehicle.getLicensePlate() + "," +     // License plate
                    vehicle.getMake() + "," +              // Make
                    vehicle.getModel() + "," +             // Model
                    vehicle.getYear() + "," +              // Year
                    "Car," +                              // Vehicle type indicator
                    car.getNumSeats()                      // Number of seats
                 );
             } else if (vehicle instanceof Motorcycle) {
                 // Down-cast to Motorcycle to access motorcycle-specific attributes.
                 Motorcycle motorcycle = (Motorcycle) vehicle;
                 // Write CSV data including license plate, make, model, year, vehicle type ("Motorcycle"), and sidecar flag.
                 writer.println(
                    vehicle.getLicensePlate() + "," +     // License plate
                    vehicle.getMake() + "," +              // Make
                    vehicle.getModel() + "," +             // Model
                    vehicle.getYear() + "," +              // Year
                    "Motorcycle," +                       // Vehicle type indicator
                    motorcycle.hasSidecar()               // Whether it has a sidecar (true/false)
                 );
             }
        } catch (IOException e) {
            // Print an error message if something goes wrong with file IO.
            System.err.println("Error saving vehicle: " + e.getMessage());
        }
    }
// Method to save customer details to customers.txt
   public void saveCustomer(Customer customer) {
       try (FileWriter fw = new FileWriter("customers.txt", true); // Open in append mode
            PrintWriter writer = new PrintWriter(fw)) {
           // Assuming Customer has methods like getCustomerId(), getCustomerName()
           writer.println(customer.getCustomerId() + "," + customer.getCustomerName());
       } catch (IOException e) {
           System.err.println("Error saving customer: " + e.getMessage());
       }
   }
   
   // Method to save rental record details to rental_records.txt
   public void saveRecord(RentalRecord record) {
       try (FileWriter fw = new FileWriter("rental_records.txt", true); // Open in append mode
            PrintWriter writer = new PrintWriter(fw)) {
           // Assuming RentalRecord has methods like getVehicle(), getCustomer(), getDate(), etc.
           writer.println(record.getVehicle().getLicensePlate() + "," + record.getCustomer().getCustomerName() + ","
                   + record.getrecordDate() + "," + record.gettotalAmount() + "," + record.getrecordDate());
       } catch (IOException e) {
           System.err.println("Error saving rental record: " + e.getMessage());
       }
   }

    public boolean addVehicle(Vehicle vehicle) {
    	// Check if the vehicle with the same license plate already exists
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
            System.out.println("Error: A vehicle with this license plate already exists.");
            return false; // Return false to indicate a duplicate
        }
        // If no duplicate is found, add the vehicle
           vehicles.add(vehicle);
        // Save the vehicle to the file
           saveVehicle(vehicle);
           return true;
    }

    public boolean addCustomer(Customer customer) {
    	// Check if customer with the same ID already exists
        if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("Error: A customer with this ID already exists.");
            return false; // Return false to indicate a duplicate
        }
        // If no duplicate is found, add the customer
        customers.add(customer);
     // Save the customer to the file
        saveCustomer(customer);  
        return true;
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
    	amount = 500;
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
    	extraFees = 200;
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayAvailableVehicles() {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getInfo());
        }
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
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

    public Customer findCustomerByName(String name) {
        for (Customer c : customers)
            if (c.getCustomerName().equalsIgnoreCase(name))
                return c;
        return null;
    }
    
    
    private void loadData() {
    	// Load vehicles from vehicles.txt
    	try (BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"))) {
    	    String line;
    	    while ((line = reader.readLine()) != null) {
    	        // Split the line into fields.
    	        String[] data = line.split(",");
    	        
    	        // Determine if we have the detailed format (6 fields) or the simpler one (4 fields).
    	        String licensePlate;
    	        String make;
    	        String model;
    	        int year;
    	        Vehicle vehicle = null;
    	        
    	        if (data.length >= 6) {
    	            // Detailed format assumed: licensePlate, make, model, year, type, extra details.
    	            licensePlate = data[0].trim();
    	            make = data[1].trim();
    	            model = data[2].trim();
    	            year = Integer.parseInt(data[3].trim());
    	            // Assuming type is at index 4 and extra details (like number of seats) at index 5.
    	            // Here we handle only cars as an example.
    	            vehicle = new Car(make, model, year, Integer.parseInt(data[5].trim()));
    	        } else if (data.length >= 4) {
    	            // Simple format assumed: licensePlate, make, model, year.
    	            licensePlate = data[0].trim();
    	            make = data[1].trim();
    	            model = data[2].trim();
    	            year = Integer.parseInt(data[3].trim());
    	            // Default to Car with 5 seats.
    	            vehicle = new Car(make, model, year, 5);
    	        } else {
    	            // If the data doesn't have enough fields, skip this line.
    	            continue;
    	        }
    	        
    	        // Set the vehicle's license plate.
    	        vehicle.setLicensePlate(licensePlate);
    	        
    	        // Check if a vehicle with this license plate already exists.
    	        // Only add the vehicle if it doesn't exist already.
    	        if (findVehicleByPlate(licensePlate) == null) {
    	            vehicles.add(vehicle);
    	        } else {
    	            // Optionally, print a debug message to indicate that a duplicate was skipped.
    	            // System.out.println("Duplicate found and skipped: " + licensePlate);
    	        }
    	    }
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}

 // Load customers from customers.txt
    try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            // Split the line assuming CSV format with exactly 2 fields: id and name
            String[] customerDetails = line.split(",");
            // Ensure there are at least 2 parts: id and name
            if (customerDetails.length < 2) continue;

            int customerId = Integer.parseInt(customerDetails[0].trim());
            String customerName = customerDetails[1].trim();

            // Create a new Customer object and add it to the list
            Customer customer = new Customer(customerId, customerName);
            customers.add(customer);
        }
    } catch (IOException g) {
        g.printStackTrace();
    }
    
 // Load rental records from rental_records.txt
    File file = new File("rental_records.txt");
    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line to get the rental record details (assuming CSV format)
                String[] recordDetails = line.split(",");
                // Ensure you have enough elements before processing
                if (recordDetails.length < 5) continue;
                
                String vehiclePlate = recordDetails[0].trim();
                int customerId = Integer.parseInt(recordDetails[1].trim());
                LocalDate rentalDate = LocalDate.parse(recordDetails[2].trim());
                double amount = Double.parseDouble(recordDetails[3].trim());
                String type = recordDetails[4].trim();
                
                // TODO: Add code to reconstruct and add a RentalRecord to the rentalHistory list
            }
        } catch (IOException f) {
            f.printStackTrace();
        }
    } else {
       
    }
    }
    
}