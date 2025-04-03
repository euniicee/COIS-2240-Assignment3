import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

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
       try (FileWriter fw = new FileWriter("vehicles.txt", true); // Open in append mode
            PrintWriter writer = new PrintWriter(fw)) {
           // Assuming Vehicle has methods like getLicensePlate(), getMake(), etc.
           writer.println(vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + vehicle.getModel() + "," + vehicle.getYear());
       } catch (IOException e) {
           System.err.println("Error saving vehicle: " + e.getMessage());
       }
   }
/// Method to save customer details to customers.txt
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
                   + record.getRecordDate() + "," + record.getAmount() + "," + record.getRecordDate());
       } catch (IOException e) {
           System.err.println("Error saving rental record: " + e.getMessage());
       }
   }
   

    

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
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
            String[] data = line.split(",");
            // To ensure valid format
            if (data.length < 5) continue;  

            String make = data[0].trim();
            String model = data[1].trim();
            int year = Integer.parseInt(data[2].trim());
            String licensePlate = data[3].trim();
            String type = data[4].trim(); // This should specify "Car" or "Motorcycle"

            Vehicle vehicle;
            if (type.equalsIgnoreCase("Car")) {
                int numSeats = Integer.parseInt(data[5].trim());
                vehicle = new Car(make, model, year, numSeats);
            } else if (type.equalsIgnoreCase("Motorcycle")) {
                boolean hasSidecar = Boolean.parseBoolean(data[5].trim());
                vehicle = new Motorcycle(make, model, year, hasSidecar);
            } else {
                System.out.println("Unknown vehicle type: " + type);
                continue;
            }

        vehicles.add(vehicle);
    }
} catch (IOException e) {
    e.printStackTrace();
}

// Load customers from customers.txt
try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        // Split the line to get the details of the customer (assuming CSV format)
        String[] customerDetails = line.split(",");
        int customerId = Integer.parseInt(customerDetails[0].trim());
        String customerName = customerDetails[1].trim();
        String customerEmail = customerDetails[2].trim();
        
        // Create a new Customer object and add it to the list
        Customer customer = new Customer(customerId, customerName);
        customers.add(customer);
    }
} catch (IOException e) {
    e.printStackTrace();
}

// Load rental records from rental_records.txt
try (BufferedReader br = new BufferedReader(new FileReader("rental_records.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        // Split the line to get the rental record details (assuming CSV format)
        String[] recordDetails = line.split(",");
        String vehiclePlate = recordDetails[0].trim();
        int customerId = Integer.parseInt(recordDetails[1].trim());
        LocalDate rentalDate = LocalDate.parse(recordDetails[2].trim());
        double amount = Double.parseDouble(recordDetails[3].trim());
        String type = recordDetails[4].trim();

}