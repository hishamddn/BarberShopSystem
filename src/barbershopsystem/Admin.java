/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barbershopsystem;

/**
 *
 * @author MOHAMAD HISHAMUDDIN
 */

public class Admin extends User {
    private String adminID;

    private static int counter = 1;

    public Admin(String name, String phoneNumber, String username, String password) {
        super(name, phoneNumber, username, password, "Admin");
        this.adminID = "A00" + counter++;
    }

    // for loading from file
    public Admin(String adminID, String name, String phoneNumber, String username, String password) {
        super(name, phoneNumber, username, password, "Admin");
        this.adminID = adminID;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String getAdminID() { return adminID; }

    @Override
    public String toString() {
        return adminID + "," + name + "," + phoneNumber + "," + username + "," + password + "," + role;
    }
}
