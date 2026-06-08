package barbershopsystem;
 
/**
 *
 * @author UMAR BIN AZIZ (2517039)
 */
 
public class Appointment {
 
    private static int counter = 1;  //uniq id for user
 
    private int appointmentID;
    private Customer customer;
    private Barber barber;
    private Service service;
    private String dateTime;
    private String status;
 
    
 
    public Appointment(Customer customer, Barber barber, Service service, String dateTime) {
        this.appointmentID = counter++;
        this.customer      = customer;
        this.barber        = barber;
        this.service       = service;
        this.dateTime      = dateTime;
        this.status        = "Booked";
    }
 
  
    public Appointment(int appointmentID, Customer customer, Barber barber,
                       Service service, String dateTime, String status) {
        this.appointmentID = appointmentID;
        this.customer      = customer;
        this.barber        = barber;
        this.service       = service;
        this.dateTime      = dateTime;
        this.status        = status;
    }
 

    public static void setCounter(int value) {
        counter = value;
    }
 
    public static int getCounter() {
        return counter;
    }
 
    
    public void cancel() {    //cancel the appointment
        this.status = "Cancelled";
        System.out.println("Appointment " + appointmentID + " has been cancelled.");
    }
 
    public void updateAppointment(String newDateTime) {    //update the appointment
        this.dateTime = newDateTime;
        System.out.println("Appointment " + appointmentID + " updated successfully.");
    }

    public void markInProgress() {
        this.status = "In Progress";
        System.out.println("Appointment " + appointmentID + " is now in progress.");
    }
 
 
    public void setStatus(String status) {
        this.status = status;
    }

    //get details and display in main program
    public String getDetails() {
        return "Appointment ID : " + appointmentID
             + "\nCustomer      : " + customer.getName()
             + "\nBarber        : " + barber.getName()
             + "\nService       : " + service.getServiceName()
             + "\nPrice         : RM" + service.getPrice()
             + "\nDate & Time   : " + dateTime
             + "\nStatus        : " + status;
    }
 

    //for save the data in appointment file
    @Override
    public String toString() {
        return appointmentID + "|" + customer.getCustID() + "|" + customer.getName() + "|" + customer.getPhoneNumber() + "|" + customer.getEmail()
             + "|" + barber.getStaffID() + "|" + barber.getName() + "|" + barber.getPhoneNumber()
             + "|" + service.getServiceID() + "|" + service.getServiceName() + "|" + service.isShave() + "|" + service.getPrice()
             + "|" + dateTime + "|" + status;
    }
 
    //for load data from appointment file
    public static Appointment fromString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
 
        String[] p = line.split("\\|", -1);
        if (p.length < 14) return null;
 
        try {
            int    aptID        = Integer.parseInt(p[0].trim());
            int    custID       = Integer.parseInt(p[1].trim());
            String custName     = p[2].trim();
            String custPhone    = p[3].trim();
            String custEmail    = p[4].trim();

            // Indices 5 to 7: Barber Data (Note: Your toString does NOT save username and password here!)
            String staffID      = p[5].trim();
            String barberName   = p[6].trim();
            String barberPhone  = p[7].trim();

            // Indices 8 to 11: Service Data
            String serviceID    = p[8].trim();
            String serviceName  = p[9].trim();
            boolean shave       = Boolean.parseBoolean(p[10].trim());
            double  basePrice   = Double.parseDouble(p[11].trim());

            // Indices 12 & 13: Core Appointment Info
            String  dateTime    = p[12].trim();
            String  status      = p[13].trim();

            String haircutType  = shave ? serviceName.replace(" + Shave", "").trim()
                                        : serviceName;
 
            Customer customer = new Customer(custName, custPhone, custEmail, custID);
            Barber barber = new Barber(staffID, barberName, barberPhone, "", "", true);
            Service  service  = new Service(serviceID, haircutType, shave, basePrice);
 
            return new Appointment(aptID, customer, barber, service, dateTime, status); // Return the new appointment date
 
        } catch (Exception e) { // warning
            System.out.println("Warning: could not parse appointment line -> " + line);
            return null;
        }
    }
 

    public int      getAppointmentID() { return appointmentID; }
    public Customer getCustomer()      { return customer; }
    public Barber   getBarber()        { return barber; }
    public Service  getService()       { return service; }
    public String   getDateTime()      { return dateTime; }
    public String   getStatus()        { return status; }
}