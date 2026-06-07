
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

    public Service(String serviceID, String haircutType, boolean shave, double basePrice) {
        this.serviceID = serviceID;
        this.haircutType = haircutType;
        this.shave = shave;
        this.basePrice = basePrice;
    }

    public double calculatePrice() {
        double total = basePrice;
        if (shave) {
            total += 3.0;
        }
        return total;
    }

    // added so Appointment.getDetails() works
    public String getServiceName() {
        return haircutType + (shave ? " + Shave" : "");
    }

    // added so Payment and main program works
    public double getPrice() {
        return calculatePrice();
    }

    public String getServiceID() {
        return serviceID;
    }

    public void displayService() {
        System.out.println("Service ID   : " + serviceID);
        System.out.println("Haircut      : " + haircutType);
        System.out.println("Shave        : " + (shave ? "Yes (+RM3)" : "No"));
        System.out.println("Total Price  : RM" + calculatePrice());
    }
}
