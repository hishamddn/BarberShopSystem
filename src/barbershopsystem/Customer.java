/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barbershopsystem;

/**
 *
 * @author MOHAMAD HISHAMUDDIN BIN MOHAMAD MANSOR(2518745)
 */

public class Customer extends User {
    private int custID;
    private String email;

    private static int idCounter = 1;

    public Customer(String name, String phoneNumber, String email) {
        super(name, phoneNumber);
        this.custID = idCounter++;
        this.email = email;
    }

    public Appointment bookAppointment(Barber barber, Service service, String dateTime) {
        Appointment appointment = new Appointment(this, barber, service, dateTime);
        System.out.println("Appointment booked for " + this.name);
        return appointment;
    }

    public void cancelAppointment(Appointment appointment) {
        appointment.cancel();
        System.out.println("Appointment cancelled for " + this.name);
    }

    public Feedback leaveFeedback(String feedbackID, String barberName, int rating, String comment, String date) {
        Feedback feedback = new Feedback(feedbackID, this.name, barberName, rating, comment, date);
        System.out.println(this.name + " submitted feedback:");
        System.out.println(feedback.getDetails());
        return feedback;
    }

    public int getCustID(){ 
        return custID; 
    }
    public String getName() { 
        return name; 
    }
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    public String getEmail() { 
        return email; 
    }
}
