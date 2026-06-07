package barbershopsystem;

import java.util.ArrayList;
import java.util.Scanner;

public class BarbershopSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Appointment> appointmentList = new ArrayList<>();

        System.out.println("==============================");
        System.out.println("  Welcome to BarberShop System");
        System.out.println("==============================\n");

        boolean running = true;
        while (running) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Book Appointment");
            System.out.println("2. Update Appointment");
            System.out.println("3. Cancel Appointment");
            System.out.println("4. Exit");
            System.out.print("Select option (1-4): ");
            int menuChoice = sc.nextInt();
            sc.nextLine();

            switch (menuChoice) {

                case 1:
                    // --- CUSTOMER FORM ---
                    System.out.println("\n=== Customer Details ===");
                    System.out.print("Enter your name        : ");
                    String custName = sc.nextLine();
                    System.out.print("Enter your phone number: ");
                    String custPhone = sc.nextLine();
                    System.out.print("Enter your email       : ");
                    String custEmail = sc.nextLine();

                    Customer customer = new Customer(custName, custPhone, custEmail);
                    System.out.println("Customer registered! ID: " + customer.getCustID());

                    // --- SELECT BARBER ---
                    System.out.println("\n=== Available Barbers ===");
                    Barber.printBarberList();
                    ArrayList<Barber> barberList = Barber.getBarberList();
                    System.out.print("Select barber (1-5): ");
                    int barberChoice = sc.nextInt() - 1;
                    sc.nextLine();
                    Barber selectedBarber = barberList.get(barberChoice);
                    System.out.println("Selected: " + selectedBarber.getName());

                    // --- SELECT SERVICE ---
                    System.out.println("\n=== Available Services ===");
                    System.out.println("1. Crop Cut   - RM15.00");
                    System.out.println("2. Fade Cut   - RM18.00");
                    System.out.println("3. Bowl Cut   - RM12.00");
                    System.out.println("4. Enter manually");
                    System.out.print("Select service (1-4): ");
                    int serviceChoice = sc.nextInt();
                    sc.nextLine();

                    String haircutType;
                    double basePrice;

                    if (serviceChoice == 1) {
                        haircutType = "Crop Cut";
                        basePrice = 15.00;
                    } else if (serviceChoice == 2) {
                        haircutType = "Fade Cut";
                        basePrice = 18.00;
                    } else if (serviceChoice == 3) {
                        haircutType = "Bowl Cut";
                        basePrice = 12.00;
                    } else {
                        System.out.print("Enter your desired haircut: ");
                        haircutType = sc.nextLine();
                        basePrice = 15.00;
                        System.out.println("Fixed price for custom haircut: RM15.00");
                    }

                    // --- SHAVE OPTION ---
                    System.out.print("Do you want a shave? (yes/no): ");
                    String shaveInput = sc.nextLine().trim().toLowerCase();
                    boolean shave = shaveInput.equals("yes");

                    Service selectedService = new Service("S00" + serviceChoice, haircutType, shave, basePrice);
                    System.out.println("\nService summary:");
                    selectedService.displayService();

                    // --- DATE & TIME ---
                    System.out.print("\nEnter appointment date & time (e.g. 2025-06-01 10:00AM): ");
                    String dateTime = sc.nextLine();

                    // --- BOOK APPOINTMENT ---
                    System.out.println("\n=== Booking Appointment ===");
                    Appointment appointment = customer.bookAppointment(selectedBarber, selectedService, dateTime);
                    appointmentList.add(appointment);
                    System.out.println(appointment.getDetails());

                    // --- PAYMENT ---
                    System.out.println("\n=== Payment ===");
                    System.out.println("Total amount: RM" + selectedService.getPrice());
                    System.out.println("1. Cash  2. Card  3. E-Wallet");
                    System.out.print("Select payment method: ");
                    int payChoice = sc.nextInt();
                    sc.nextLine();
                    String[] methods = {"Cash", "Card", "E-Wallet"};
                    String payMethod = methods[payChoice - 1];

                    Payment payment = new Payment(appointment.getAppointmentID(), selectedService.getPrice(), payMethod);
                    payment.paymentDetails();

                    // --- FEEDBACK ---
                    System.out.println("\n=== Leave Feedback ===");
                    System.out.print("Enter rating (1-5): ");
                    int rating = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter comment      : ");
                    String comment = sc.nextLine();
                    String today = java.time.LocalDate.now().toString();
                    customer.leaveFeedback("F00" + appointment.getAppointmentID(), selectedBarber.getName(), rating, comment, today);
                    break;

                case 2:
                    // --- UPDATE APPOINTMENT ---
                    if (appointmentList.isEmpty()) {
                        System.out.println("\nNo appointments found.");
                        break;
                    }

                    System.out.println("\n=== Existing Appointments ===");
                    for (Appointment apt : appointmentList) {
                        System.out.println("-----------------------------");
                        System.out.println(apt.getDetails());
                    }

                    System.out.print("\nEnter Appointment ID to update: ");
                    int updateID = sc.nextInt();
                    sc.nextLine();

                    Appointment toUpdate = null;
                    for (Appointment apt : appointmentList) {
                        if (apt.getAppointmentID() == updateID) {
                            toUpdate = apt;
                            break;
                        }
                    }

                    if (toUpdate == null) {
                        System.out.println("Appointment ID not found.");
                    } else if (toUpdate.getStatus().equals("Cancelled")) {
                        System.out.println("Cannot update a cancelled appointment.");
                    } else {
                        System.out.print("Enter new date & time (e.g. 2025-06-01 02:00PM): ");
                        String newDateTime = sc.nextLine();
                        toUpdate.updateAppointment(newDateTime);
                        System.out.println("\nUpdated appointment details:");
                        System.out.println(toUpdate.getDetails());
                    }
                    break;

                case 3:
                    // --- CANCEL APPOINTMENT ---
                    if (appointmentList.isEmpty()) {
                        System.out.println("\nNo appointments found.");
                        break;
                    }

                    System.out.println("\n=== Existing Appointments ===");
                    for (Appointment apt : appointmentList) {
                        System.out.println("-----------------------------");
                        System.out.println(apt.getDetails());
                    }

                    System.out.print("\nEnter Appointment ID to cancel: ");
                    int cancelID = sc.nextInt();
                    sc.nextLine();

                    Appointment toCancel = null;
                    for (Appointment apt : appointmentList) {
                        if (apt.getAppointmentID() == cancelID) {
                            toCancel = apt;
                            break;
                        }
                    }

                    if (toCancel == null) {
                        System.out.println("Appointment ID not found.");
                    } else if (toCancel.getStatus().equals("Cancelled")) {
                        System.out.println("Appointment is already cancelled.");
                    } else {
                        toCancel.cancel();
                        System.out.println("\nCancelled appointment details:");
                        System.out.println(toCancel.getDetails());
                    }
                    break;

                case 4:
                    System.out.println("\n==============================");
                    System.out.println("  Thank you for visiting!");
                    System.out.println("==============================");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please select 1-4.");
                    break;
            }
        }
        sc.close();
    }
}