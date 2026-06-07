
package barbershopsystem;

/**
 *
 * @author UMAR BIN AZIZ(2517039)
 */

public class Appointment {
    private static int counter = 1;

    private int appointmentID;
    private Customer customer;
    private Barber barber;
    private Service service;
    private String dateTime;
    private String status;

    public Appointment(Customer customer, Barber barber, Service service, String dateTime) {
        this.appointmentID = counter++;
        this.customer = customer;
        this.barber = barber;
        this.service = service;
        this.dateTime = dateTime;
        this.status = "Booked";
    }

    public void cancel() {
        this.status = "Cancelled";
        System.out.println("Appointment " + appointmentID + " has been cancelled.");
    }

    public void updateAppointment(String newDateTime) {
        this.dateTime = newDateTime;
        System.out.println("Appointment " + appointmentID + " updated successfully.");
    }

    public String getDetails() {
        return "Appointment ID : " + appointmentID +
               "\nCustomer      : " + customer.getName() +
               "\nBarber        : " + barber.getName() +
               "\nService       : " + service.getServiceName() +
               "\nPrice         : RM" + service.getPrice() +
               "\nDate & Time   : " + dateTime +
               "\nStatus        : " + status;
    }

    public int getAppointmentID() { return appointmentID; }
    public Customer getCustomer() { return customer; }
    public Barber getBarber() { return barber; }
    public Service getService() { return service; }
    public String getStatus() { return status; }
}

