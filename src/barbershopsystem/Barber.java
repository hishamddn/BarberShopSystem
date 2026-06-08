package barbershopsystem;

/**
 *
 * @author MOHAMMAD HAIKAL BIN KAIBAR (2514445)
 */

import java.util.ArrayList;

public class Barber extends User {
    private String staffID;
    private boolean status;

    private static int counter = 1;

    // for admin creating new barber with login credentials
    public Barber(String name, String phoneNumber, String username, String password, boolean status) {
        super(name, phoneNumber, username, password, "Barber");
        this.staffID = "B00" + counter++;
        this.status = status;
    }
    
    // for loading from file
    public Barber(String staffID, String name, String phoneNumber, String username, String password, boolean status) {
        super(name, phoneNumber, username, password, "Barber");
        this.staffID = staffID;
        this.status = status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getStaffID() {
        return staffID;
    }
    public boolean getStatus() {
        return status;
    }
    
    @Override
    public String toString(){
        return staffID + "," + name + "," + phoneNumber + "," + username + "," + password + "," + status;
    }

    public static ArrayList<Barber> getBarberList() {
        ArrayList<Barber> listBarber = new ArrayList<>();

        listBarber.add(new Barber("B001", "Muhamamd Ali", "0128729700", "ali", "1234", true));
        listBarber.add(new Barber("B002", "Abu Hasan", "0183617000", "abu", "4567", true));
        listBarber.add(new Barber("B003", "Ahmad", "0179371000", "ahmad", "4321", true));
        listBarber.add(new Barber("B004", "Alan Walker", "0118317000", "alan", "7654", true));
        listBarber.add(new Barber("B005", "Aminuddin", "0191318000", "amin", "7890", true));
        return listBarber;
    }

    public static void printBarberList() {
        ArrayList<Barber> listBarber = getBarberList();
        System.out.println("Available Barbers:");
        for (int i = 0; i < listBarber.size(); i++) {
            System.out.println((i + 1) + ". " + listBarber.get(i).getName());
        }
    }
}
