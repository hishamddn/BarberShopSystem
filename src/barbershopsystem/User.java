/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package barbershopsystem;

/**
 *
 * @author MOHAMAD HISHAMUDDIN 
 */
public class User{
    String name;
    String phoneNumber;
    String username;
    String password;
    String role;
    
    public User(String name, String phoneNumber, String username, String password, String role){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public String getName(){return name;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getRole(){return role;}
    
    @Override
    public String toString(){
        return name + "," + phoneNumber + "," + username + "," + password + "," + role;
    }
    
}
