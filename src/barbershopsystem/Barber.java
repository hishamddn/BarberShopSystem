package barbershopsystem;

/**
 *
 * @author MOHAMMAD HAIKAL BIN KAIBAR (2514445)
 */

import java.util.ArrayList;

public class Barber extends User {
    private String staffID;
    private boolean status;

    public Barber(String staffID, String name, String phoneNumber, boolean status) {
        super(name, phoneNumber);
        this.staffID = staffID;
        this.status = status;
    }

    public String getName() {
        return name; // inherited from User
    }

    public String getStaffID() {
        return staffID;
    }

    public boolean getStatus() {
        return status;
    }

    public static ArrayList<Barber> getBarberList() {
        ArrayList<Barber> listBarber = new ArrayList<>();
        listBarber.add(new Barber("B001", "Ali", "0128729700", true));
        listBarber.add(new Barber("B002", "Abu", "0183617000", true));
        listBarber.add(new Barber("B003", "Ahmad", "0179371000", true));
        listBarber.add(new Barber("B004", "Alan", "0118317000", true));
        listBarber.add(new Barber("B005", "Amin", "0191318000", true));
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

//hisyam g4y
//hisham poke
