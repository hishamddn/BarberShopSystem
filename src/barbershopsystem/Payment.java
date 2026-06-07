package barbershopsystem;

/**
 *
 * @author DANISH AIRIL HISYAM BIN MOHD FAIRUS (2511113)
 */

public class Payment {

    private static int counter = 1;

    private String paymentID;
    private int    appointmentID;
    private double amount;
    private String paymentMethod;
    private String status;


    public Payment(int appointmentID, double amount, String paymentMethod) {
        this.paymentID     = "PAY" + counter++;
        this.appointmentID = appointmentID;
        this.amount        = amount;
        this.paymentMethod = paymentMethod;
        this.status        = "Paid";
    }

 
    public Payment(String paymentID, int appointmentID,
                   double amount, String paymentMethod, String status) {
        this.paymentID     = paymentID;
        this.appointmentID = appointmentID;
        this.amount        = amount;
        this.paymentMethod = paymentMethod;
        this.status        = status;

    
        try {
            int loadedNum = Integer.parseInt(paymentID.replace("PAY", ""));
            if (loadedNum >= counter) counter = loadedNum + 1;
        } catch (NumberFormatException ignored) { }
    }

    public static void setCounter(int value) {
        counter = value;
    }

    public static int getCounter() {
        return counter;
    }

    // -------------------------------------------------------
    // Core actions
    // -------------------------------------------------------
    public void refund() {    //update to refund
        this.status = "Refunded";
        System.out.println("Payment " + paymentID + " has been refunded.");
    }

 
    public void setStatus(String status) {
        this.status = status;
    }


    // Display

    public void paymentDetails() {      //display full payment
        System.out.println("Payment ID    : " + paymentID);
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Amount        : RM" + amount);
        System.out.println("Method        : " + paymentMethod);
        System.out.println("Status        : " + status);
    }


    @Override
    public String toString() {
        return paymentID
             + "|" + appointmentID
             + "|" + amount
             + "|" + paymentMethod
             + "|" + status;
    }


    public static Payment fromString(String line) {
        if (line == null || line.trim().isEmpty()) return null;

        String[] p = line.split("\\|", -1);
        if (p.length < 5) return null;

        try {
            String paymentID     = p[0].trim();
            int    appointmentID = Integer.parseInt(p[1].trim());
            double amount        = Double.parseDouble(p[2].trim());
            String paymentMethod = p[3].trim();
            String status        = p[4].trim();

            return new Payment(paymentID, appointmentID, amount, paymentMethod, status);

        } catch (Exception e) {
            System.out.println("Warning: could not parse payment line -> " + line);
            return null;
        }
    }

  
    public String getPaymentID()     { return paymentID; }
    public int    getAppointmentID() { return appointmentID; }
    public double getAmount()        { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus()        { return status; }
}