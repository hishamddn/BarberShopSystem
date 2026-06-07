/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barbershopsystem;

/**
 *
 * @author DANISH AIRIL HISYAM BIN MOHD FAIRUS (2511113)
 */

public class Payment {
    private static int counter = 1;

    private String paymentID;
    private int appointmentID;  // changed to int to match Appointment
    private double amount;
    private String paymentMethod;
    private String status;      // changed to String

    public Payment(int appointmentID, double amount, String paymentMethod) {
        this.paymentID = "PAY" + counter++;
        this.appointmentID = appointmentID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "Paid";
    }

    public void refund() {
        this.status = "Refunded";
        System.out.println("Payment " + paymentID + " has been refunded.");
    }

    public void paymentDetails() {
        System.out.println("Payment ID    : " + paymentID);
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Amount        : RM" + amount);
        System.out.println("Method        : " + paymentMethod);
        System.out.println("Status        : " + status);
    }

    public String getPaymentID() { return paymentID; }
    public int getAppointmentID() { return appointmentID; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
}
