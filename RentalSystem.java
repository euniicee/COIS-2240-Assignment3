import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
	private static RentalSystem instance;
	private static RentalSystem instance;
	public static RentalSystem getInstance() {
        // If the instance is null, create it
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
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
}