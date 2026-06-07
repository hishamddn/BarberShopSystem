
package barbershopsystem;

/**
 *
 * @author AHMAD HAFIZHAN ZAIM BIN HASNI (2512783)
 */
public class Feedback {
    private String feedbackID;
    private String customerName;
    private String barberName;
    private int rating;
    private String comment;
    private String date;
    
    public Feedback(String feedbackID, String customerName, String barberName, int rating, String comment, String date) {
        this.feedbackID = feedbackID;
        this.customerName = customerName;
        this.barberName = barberName;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }
    
    public void submitFeedback() {
        System.out.println("Feedback " + feedbackID + " submitted successfully.");
    }
    
    public String getDetails() {
        return "Feedback ID: " + feedbackID + 
               "\nCustomer: " + customerName +
               "\nBarber: " + barberName +
               "\nRating: " + rating + "/5" +
               "\nComment: " + comment + 
               "\nDate: " + date;
    }
    
    public String getFeedbackID() { return feedbackID; }
    public String getCustomerName() { return customerName; }
    public String getBarberName() { return barberName; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getDate() { return date; }
    
    @Override
    public String toString() {
        return feedbackID + "," + customerName + "," + barberName + "," + rating + "," + comment + "," + date;
    }
}


