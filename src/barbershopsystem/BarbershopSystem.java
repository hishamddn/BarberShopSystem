package barbershopsystem;

import java.util.ArrayList;
import java.util.Scanner;

public class BarbershopSystem {

    public static ArrayList<Admin>       adminList       = new ArrayList<>();
    public static ArrayList<Barber>      barberList      = new ArrayList<>();
    public static ArrayList<Appointment> appointmentList = new ArrayList<>();
    public static ArrayList<Payment>     paymentList     = new ArrayList<>();
    public static ArrayList<Feedback>    feedbackList    = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // load all data from files on startup
        adminList       = FileHandler.loadAdmins();
        barberList      = FileHandler.loadBarbers();
        appointmentList = FileHandler.loadAppointments();
        paymentList     = FileHandler.loadPayments();
        feedbackList    = FileHandler.loadFeedbacks();

        // first launch check
        if (adminList.isEmpty()) {
            System.out.println("=== First Launch Setup ===");
            System.out.println("No admin account found. Create the first admin account.");
            System.out.print("Enter your name    : ");
            String adminName = sc.nextLine();
            System.out.print("Enter phone number : ");
            String adminPhone = sc.nextLine();
            System.out.print("Create username    : ");
            String adminUsername = sc.nextLine();
            System.out.print("Create password    : ");
            String adminPassword = sc.nextLine();

            Admin firstAdmin = new Admin(adminName, adminPhone, adminUsername, adminPassword);
            adminList.add(firstAdmin);
            FileHandler.saveAdmins(adminList);
            System.out.println("\nAdmin account created! You can now login.\n");
        }

        // main app loop
        boolean appRunning = true;
        while (appRunning) {

            System.out.println("==============================");
            System.out.println("  Welcome to BarberShop System");
            System.out.println("==============================");
            System.out.println("1. Admin");
            System.out.println("2. Barber");
            System.out.println("3. Customer");
            System.out.println("4. Exit");
            System.out.print("Login as (1-4): ");
            int userType = getValidInt(sc, 1, 4);

            switch (userType) {
                case 1 -> adminFlow(sc);
                case 2 -> barberFlow(sc);
                case 3 -> customerFlow(sc);
                case 4 -> {
                    saveAllData();
                    System.out.println("\n==============================");
                    System.out.println("  All data saved. Goodbye!");
                    System.out.println("==============================");
                    appRunning = false;
                }
            }
        }
        sc.close();
    }

    // =====================================================
    // ADMIN FLOW
    // =====================================================
    private static void adminFlow(Scanner sc) {
        // login
        Admin currentAdmin = null;
        int attempts = 0;

        while (currentAdmin == null) {
            if (attempts >= 3) {
                System.out.println("Too many failed attempts. Returning to start.\n");
                return;
            }
            System.out.println("\n=== Admin Login ===");
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            for (Admin admin : adminList) {
                if (admin.login(username, password)) {
                    currentAdmin = admin;
                    break;
                }
            }
            if (currentAdmin == null) {
                attempts++;
                System.out.println("Invalid credentials. " + (3 - attempts) + " attempt(s) left.\n");
            }
        }

        System.out.println("\nWelcome, " + currentAdmin.getName() + "!\n");

        // admin dashboard
        boolean adminRunning = true;
        while (adminRunning) {
            printAdminDashboard();
            System.out.print("Select option (1-7): ");
            int choice = getValidInt(sc, 1, 7);

            switch (choice) {
                case 1 -> bookAppointment(sc);
                case 2 -> manageAppointments(sc);
                case 3 -> completeAndPay(sc);
                case 4 -> manageBarbers(sc);
                case 5 -> viewFeedback(sc);
                case 6 -> viewAllAppointments();
                case 7 -> {
                    saveAllData();
                    System.out.println("Logged out.\n");
                    adminRunning = false;
                }
            }
        }
    }

    private static void printAdminDashboard() {
        long booked    = appointmentList.stream().filter(a -> a.getStatus().equals("Booked")).count();
        long inProgress = appointmentList.stream().filter(a -> a.getStatus().equals("In Progress")).count();
        long completed = appointmentList.stream().filter(a -> a.getStatus().equals("Completed")).count();
        long cancelled = appointmentList.stream().filter(a -> a.getStatus().equals("Cancelled")).count();
        double revenue = paymentList.stream().mapToDouble(Payment::getAmount).sum();

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         ADMIN DASHBOARD              ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf( "║  Total Appointments : %-15d║%n", appointmentList.size());
        System.out.printf( "║  Booked             : %-15d║%n", booked);
        System.out.printf( "║  In Progress        : %-15d║%n", inProgress);
        System.out.printf( "║  Completed          : %-15d║%n", completed);
        System.out.printf( "║  Cancelled          : %-15d║%n", cancelled);
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf( "║  Total Barbers      : %-15d║%n", barberList.size());
        System.out.printf( "║  Total Feedback     : %-15d║%n", feedbackList.size());
        System.out.printf( "║  Total Revenue      : RM%-13.2f║%n", revenue);
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Book Appointment                 ║");
        System.out.println("║  2. Manage Appointments              ║");
        System.out.println("║  3. Complete & Process Payment       ║");
        System.out.println("║  4. Manage Barbers                   ║");
        System.out.println("║  5. View Feedback                    ║");
        System.out.println("║  6. View All Appointments            ║");
        System.out.println("║  7. Logout                           ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // =====================================================
    // BARBER FLOW
    // =====================================================
    private static void barberFlow(Scanner sc) {
        // login
        Barber currentBarber = null;
        int attempts = 0;

        while (currentBarber == null) {
            if (attempts >= 3) {
                System.out.println("Too many failed attempts. Returning to start.\n");
                return;
            }
            System.out.println("\n=== Barber Login ===");
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            for (Barber barber : barberList) {
                if (barber.getUsername().equals(username) &&
                        barber.getPassword().equals(password)) {
                    currentBarber = barber;
                    break;
                }
            }
            if (currentBarber == null) {
                attempts++;
                System.out.println("Invalid credentials. " + (3 - attempts) + " attempt(s) left.\n");
            }
        }

        System.out.println("\nWelcome, " + currentBarber.getName() + "!\n");

        // barber dashboard
        boolean barberRunning = true;
        while (barberRunning) {
            // get this barber's appointments
            ArrayList<Appointment> myAppointments = getAppointmentsForBarber(currentBarber.getName());

            long myBooked     = myAppointments.stream().filter(a -> a.getStatus().equals("Booked")).count();
            long myInProgress = myAppointments.stream().filter(a -> a.getStatus().equals("In Progress")).count();
            long myCompleted  = myAppointments.stream().filter(a -> a.getStatus().equals("Completed")).count();

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         BARBER DASHBOARD             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.printf( "║  My Appointments    : %-15d║%n", myAppointments.size());
            System.out.printf( "║  Booked             : %-15d║%n", myBooked);
            System.out.printf( "║  In Progress        : %-15d║%n", myInProgress);
            System.out.printf( "║  Completed          : %-15d║%n", myCompleted);
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. View My Schedule                 ║");
            System.out.println("║  2. Mark Appointment as In Progress  ║");
            System.out.println("║  3. Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Select option (1-3): ");
            int choice = getValidInt(sc, 1, 3);

            switch (choice) {
                case 1 -> {
                    // view my schedule
                    if (myAppointments.isEmpty()) {
                        System.out.println("No appointments assigned to you.");
                    } else {
                        System.out.println("\n=== My Schedule ===");
                        for (Appointment apt : myAppointments) {
                            System.out.println("-----------------------------");
                            System.out.println(apt.getDetails());
                        }
                    }
                }
                case 2 -> {
                    // mark as in progress
                    ArrayList<Appointment> bookedApts = new ArrayList<>();
                    for (Appointment apt : myAppointments) {
                        if (apt.getStatus().equals("Booked")) bookedApts.add(apt);
                    }
                    if (bookedApts.isEmpty()) {
                        System.out.println("No booked appointments to mark.");
                        break;
                    }
                    System.out.println("\n=== Booked Appointments ===");
                    for (Appointment apt : bookedApts) {
                        System.out.println("-----------------------------");
                        System.out.println(apt.getDetails());
                    }
                    System.out.print("Enter Appointment ID to mark In Progress: ");
                    int aptID = getValidInt(sc, 1, Integer.MAX_VALUE);
                    Appointment apt = findAppointment(aptID);
                    if (apt == null || !apt.getBarber().getName().equals(currentBarber.getName())) {
                        System.out.println("Appointment not found in your schedule.");
                    } else if (!apt.getStatus().equals("Booked")) {
                        System.out.println("Only booked appointments can be marked in progress.");
                    } else {
                        apt.markInProgress();
                        FileHandler.saveAppointments(appointmentList);
                        System.out.println("Appointment marked as In Progress.");
                    }
                }
                case 3 -> {
                    System.out.println("Logged out.\n");
                    barberRunning = false;
                }
            }
        }
    }

    // =====================================================
    // CUSTOMER FLOW
    // =====================================================
    private static void customerFlow(Scanner sc) {
        System.out.println("\n=== Customer Booking Form ===");

        // customer details
        System.out.print("Enter your name        : ");
        String custName = sc.nextLine();
        System.out.print("Enter your phone number: ");
        String custPhone = sc.nextLine();
        System.out.print("Enter your email       : ");
        String custEmail = sc.nextLine();

        Customer customer = new Customer(custName, custPhone, custEmail);

        // select barber
        if (barberList.isEmpty()) {
            System.out.println("No barbers available. Please contact the shop.");
            return;
        }
        System.out.println("\n=== Available Barbers ===");
        Barber.printBarberList();
        System.out.print("Select barber (1-" + barberList.size() + "): ");
        int barberChoice = getValidInt(sc, 1, barberList.size()) - 1;
        Barber selectedBarber = barberList.get(barberChoice);
        System.out.println("Selected: " + selectedBarber.getName());

        // select service
        System.out.println("\n=== Available Services ===");
        System.out.println("1. Crop Cut   - RM15.00");
        System.out.println("2. Fade Cut   - RM18.00");
        System.out.println("3. Bowl Cut   - RM12.00");
        System.out.println("4. Enter manually (RM15.00 fixed)");
        System.out.print("Select service (1-4): ");
        int serviceChoice = getValidInt(sc, 1, 4);

        String haircutType;
        double basePrice;

        if (serviceChoice == 1) {
            haircutType = "Crop Cut";  basePrice = 15.00;
        } else if (serviceChoice == 2) {
            haircutType = "Fade Cut";  basePrice = 18.00;
        } else if (serviceChoice == 3) {
            haircutType = "Bowl Cut";  basePrice = 12.00;
        } else {
            System.out.print("Enter desired haircut: ");
            haircutType = sc.nextLine();
            basePrice = 15.00;
            System.out.println("Fixed price: RM15.00");
        }

        System.out.print("Add shave? (yes/no): ");
        boolean shave = sc.nextLine().trim().equalsIgnoreCase("yes");

        Service selectedService = new Service("S00" + serviceChoice, haircutType, shave, basePrice);
        System.out.println("\nService summary:");
        selectedService.displayService();

        // date and time
        System.out.print("\nEnter preferred date & time (e.g. 2025-06-10 10:00AM): ");
        String dateTime = sc.nextLine();

        // create appointment
        Appointment appointment = customer.bookAppointment(selectedBarber, selectedService, dateTime);
        appointmentList.add(appointment);
        FileHandler.saveAppointments(appointmentList);

        System.out.println("\n=== Booking Confirmed ===");
        System.out.println(appointment.getDetails());
        System.out.println("Payment will be collected after your service. See you soon!\n");
    }

    // =====================================================
    // ADMIN MENU METHODS
    // =====================================================
    private static void bookAppointment(Scanner sc) {
        System.out.println("\n=== Book Appointment ===");

        System.out.print("Customer name        : ");
        String custName = sc.nextLine();
        System.out.print("Customer phone       : ");
        String custPhone = sc.nextLine();
        System.out.print("Customer email       : ");
        String custEmail = sc.nextLine();
        Customer customer = new Customer(custName, custPhone, custEmail);

        if (barberList.isEmpty()) {
            System.out.println("No barbers available. Add barbers first.");
            return;
        }
        Barber.printBarberList();
        System.out.print("Select barber (1-" + barberList.size() + "): ");
        int barberChoice = getValidInt(sc, 1, barberList.size()) - 1;
        Barber selectedBarber = barberList.get(barberChoice);

        System.out.println("\n1. Crop Cut - RM15  2. Fade Cut - RM18  3. Bowl Cut - RM12  4. Manual");
        System.out.print("Select service (1-4): ");
        int serviceChoice = getValidInt(sc, 1, 4);

        String haircutType;
        double basePrice;
        if (serviceChoice == 1) { haircutType = "Crop Cut"; basePrice = 15.00; }
        else if (serviceChoice == 2) { haircutType = "Fade Cut"; basePrice = 18.00; }
        else if (serviceChoice == 3) { haircutType = "Bowl Cut"; basePrice = 12.00; }
        else {
            System.out.print("Enter haircut type: ");
            haircutType = sc.nextLine();
            basePrice = 15.00;
        }

        System.out.print("Add shave? (yes/no): ");
        boolean shave = sc.nextLine().trim().equalsIgnoreCase("yes");
        Service selectedService = new Service("S00" + serviceChoice, haircutType, shave, basePrice);

        System.out.print("Date & time: ");
        String dateTime = sc.nextLine();

        Appointment apt = customer.bookAppointment(selectedBarber, selectedService, dateTime);
        appointmentList.add(apt);
        FileHandler.saveAppointments(appointmentList);
        System.out.println("\nBooked! " + apt.getDetails());
    }

    private static void manageAppointments(Scanner sc) {
        if (appointmentList.isEmpty()) { System.out.println("No appointments found."); return; }

        viewAllAppointments();
        System.out.println("\n1. Update  2. Cancel  3. Back");
        System.out.print("Select (1-3): ");
        int choice = getValidInt(sc, 1, 3);
        if (choice == 3) return;

        System.out.print("Enter Appointment ID: ");
        int aptID = getValidInt(sc, 1, Integer.MAX_VALUE);
        Appointment apt = findAppointment(aptID);

        if (apt == null) {
            System.out.println("Appointment not found.");
            return;
        }

        if (choice == 1) {
            if (!apt.getStatus().equals("Booked")) {
                System.out.println("Only booked appointments can be updated.");
                return;
            }
            System.out.print("New date & time: ");
            String newDateTime = sc.nextLine();
            apt.updateAppointment(newDateTime);
            FileHandler.saveAppointments(appointmentList);
            System.out.println("Updated: " + apt.getDetails());

        } else {
            if (apt.getStatus().equals("Cancelled")) {
                System.out.println("Already cancelled.");
            } else if (apt.getStatus().equals("Completed")) {
                System.out.println("Cannot cancel a completed appointment.");
            } else {
                apt.cancel();
                FileHandler.saveAppointments(appointmentList);
                System.out.println("Cancelled: " + apt.getDetails());
            }
        }
    }

    private static void completeAndPay(Scanner sc) {
        System.out.println("\n=== Complete Appointment & Process Payment ===");

        boolean hasBooked = false;
        for (Appointment apt : appointmentList) {
            if (apt.getStatus().equals("Booked") || apt.getStatus().equals("In Progress")) {
                System.out.println("-----------------------------");
                System.out.println(apt.getDetails());
                hasBooked = true;
            }
        }
        if (!hasBooked) { System.out.println("No active appointments."); return; }

        System.out.print("Enter Appointment ID to complete: ");
        int aptID = getValidInt(sc, 1, Integer.MAX_VALUE);
        Appointment apt = findAppointment(aptID);

        if (apt == null) {
            System.out.println("Appointment not found.");
        } else if (apt.getStatus().equals("Cancelled") || apt.getStatus().equals("Completed")) {
            System.out.println("Cannot process. Status: " + apt.getStatus());
        } else {
            apt.setStatus("Completed");
            System.out.println("\nTotal: RM" + apt.getService().getPrice());
            System.out.println("1. Cash  2. Card  3. E-Wallet");
            System.out.print("Payment method: ");
            int payChoice = getValidInt(sc, 1, 3);
            String[] methods = {"Cash", "Card", "E-Wallet"};
            Payment payment = new Payment(apt.getAppointmentID(), apt.getService().getPrice(), methods[payChoice - 1]);
            paymentList.add(payment);
            FileHandler.saveAppointments(appointmentList);
            FileHandler.savePayments(paymentList);
            System.out.println("\n=== Payment Successful ===");
            payment.paymentDetails();
        }
    }

    private static void manageBarbers(Scanner sc) {
        boolean managing = true;
        while (managing) {
            System.out.println("\n=== Manage Barbers ===");
            System.out.println("1. View all barbers");
            System.out.println("2. Add new barber");
            System.out.println("3. Back");
            System.out.print("Select (1-3): ");
            int choice = getValidInt(sc, 1, 3);

            switch (choice) {
                case 1 -> {
                    if (barberList.isEmpty()) {
                        System.out.println("No barbers found.");
                    } else {
                        System.out.println("\n=== Barber List ===");
                        for (Barber b : barberList) {
                            System.out.println("ID: " + b.getStaffID() +
                                    " | Name: " + b.getName() +
                                    " | Phone: " + b.getPhoneNumber() +
                                    " | Username: " + b.getUsername() +
                                    " | Status: " + (b.getStatus() ? "Available" : "Unavailable"));
                        }
                    }
                }
                case 2 -> {
                    System.out.print("Barber name    : ");
                    String bName = sc.nextLine();
                    System.out.print("Phone number   : ");
                    String bPhone = sc.nextLine();
                    System.out.print("Set username   : ");
                    String bUsername = sc.nextLine();
                    System.out.print("Set password   : ");
                    String bPassword = sc.nextLine();

                    Barber newBarber = new Barber(bName, bPhone, bUsername, bPassword, true);
                    barberList.add(newBarber);
                    FileHandler.saveBarbers(barberList);
                    System.out.println("Barber added! ID: " + newBarber.getStaffID());
                }
                case 3 -> managing = false;
            }
        }
    }

    private static void viewFeedback(Scanner sc) {
        if (feedbackList.isEmpty()) {
            System.out.println("\nNo feedback found.");
            return;
        }
        System.out.println("\n=== All Feedback ===");
        for (Feedback f : feedbackList) {
            System.out.println("-----------------------------");
            System.out.println(f.getDetails());
        }
    }

    private static void viewAllAppointments() {
        if (appointmentList.isEmpty()) {
            System.out.println("\nNo appointments found.");
            return;
        }
        System.out.println("\n=== All Appointments ===");
        for (Appointment apt : appointmentList) {
            System.out.println("-----------------------------");
            System.out.println(apt.getDetails());
        }
    }

    // =====================================================
    // HELPERS
    // =====================================================
    private static void saveAllData() {
        FileHandler.saveAdmins(adminList);
        FileHandler.saveBarbers(barberList);
        FileHandler.saveAppointments(appointmentList);
        FileHandler.savePayments(paymentList);
        FileHandler.saveFeedbacks(feedbackList);
    }

    private static Appointment findAppointment(int id) {
        for (Appointment apt : appointmentList) {
            if (apt.getAppointmentID() == id) return apt;
        }
        return null;
    }

    private static ArrayList<Appointment> getAppointmentsForBarber(String barberName) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment apt : appointmentList) {
            if (apt.getBarber().getName().equals(barberName)) result.add(apt);
        }
        return result;
    }

    private static int getValidInt(Scanner sc, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.print("Enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}