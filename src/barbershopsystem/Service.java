
package barbershopsystem;

/**
 *
 * @author FAKHRUL AIMAN BIN KHAIRUL ANUAR (2516211)
 */

public class Service {
    private String serviceID;
    private String haircutType;
    private boolean shave;
    private double basePrice;

    private static int counter = 1;

    // for creating new service
    public Service(String haircutType, boolean shave, double basePrice) {
        this.serviceID = "S00" + counter++;
        this.haircutType = haircutType;
        this.shave = shave;
        this.basePrice = basePrice;
    }

    // for loading from file
    public Service(String serviceID, String haircutType, boolean shave, double basePrice) {
        this.serviceID = serviceID;
        this.haircutType = haircutType;
        this.shave = shave;
        this.basePrice = basePrice;
    }

    public double calculatePrice() {
        return shave ? basePrice + 3.0 : basePrice;
    }

    // added so Appointment.getDetails() works
    public String getServiceName() {
        return haircutType + (shave ? " + Shave" : "");
    }

    
    public double getPrice() {
        return calculatePrice();
    }

    public String getServiceID() {
        return serviceID;
    }

    public boolean isShave() { return shave; }
    
    public String getHaircutType() { return haircutType; }
    
    
    
    public void displayService() {
        System.out.println("Service ID   : " + serviceID);
        System.out.println("Haircut      : " + haircutType);
        System.out.println("Shave        : " + (shave ? "Yes (+RM3)" : "No"));
        System.out.println("Total Price  : RM" + calculatePrice());
    }
    
    @Override
    public String toString() {
        return serviceID + "," + haircutType + "," + shave + "," + basePrice;
    }
}
