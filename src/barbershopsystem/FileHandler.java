package barbershopsystem;

import java.io.*;
import java.util.ArrayList;


public class FileHandler {

    //          ADMIN
    // ==========================

    public static void saveAdmins(ArrayList<Admin> admins) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("admins.txt"))) {
            for (Admin admin : admins) {
                pw.println(admin.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving admins.");
        }
    }

    public static ArrayList<Admin> loadAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("admins.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 6) {
                    admins.add(new Admin(
                            data[0], // adminID
                            data[1], // name
                            data[2], // phone
                            data[3], // username
                            data[4]  // password
                    ));
                }
            }
        }  catch (IOException e) {
            // file doesn't exist yet — create default admin
            System.out.println("Admin file not found. Creating default admin.");
            admins.add(new Admin("Admin", "0000000000", "admin", "admin123"));
            saveAdmins(admins);
        }

        return admins;
    }


    //          BARBER
    // ==========================

    public static void saveBarbers(ArrayList<Barber> barbers) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("barbers.txt"))) {
            for (Barber barber : barbers) {
                pw.println(barber.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving barbers.");
        }
    }

    public static ArrayList<Barber> loadBarbers() {
        ArrayList<Barber> barbers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("barbers.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 6) {
                    barbers.add(new Barber(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            Boolean.parseBoolean(data[5])
                    ));
                }
            }
        } catch (IOException e) {
            // file doesn't exist yet — load default barbers
            System.out.println("Barber file not found. Loading default barbers.");
            barbers = Barber.getBarberList();
            saveBarbers(barbers);
        }

        return barbers;
    }


    //         APPOINTMENT
    // ==========================

    public static void saveAppointments(ArrayList<Appointment> appointments) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("appointments.txt"))) {
            for (Appointment appointment : appointments) {
                pw.println(appointment.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving appointments.");
        }
    }

    public static ArrayList<Appointment> loadAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("appointments.txt"))) {
            String line;
            int maxID = 0;
            while ((line = br.readLine()) != null) {
                Appointment appointment = Appointment.fromString(line);

                if (appointment != null) {
                    appointments.add(appointment);
                }
            }
            // sync counter so new appointments don't repeat IDs
            if (maxID > 0) Appointment.setCounter(maxID + 1);

        } catch (IOException e) {
            System.out.println("Appointment file not found.");
        }
        return appointments;
    }


    //          PAYMENT
    // ==========================

    public static void savePayments(ArrayList<Payment> payments) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("payments.txt"))) {
            for (Payment payment : payments) {
                pw.println(payment.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving payments.");
        }
    }

    public static ArrayList<Payment> loadPayments() {
        ArrayList<Payment> payments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("payments.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                Payment payment = Payment.fromString(line);

                if (payment != null) {
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            System.out.println("Payment file not found.");
        }

        return payments;
    }


    //         FEEDBACK
    // ==========================

    public static void saveFeedbacks(ArrayList<Feedback> feedbacks) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("feedbacks.txt"))) {
            for (Feedback feedback : feedbacks) {
                pw.println(feedback.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving feedbacks.");
        }
    }

    public static ArrayList<Feedback> loadFeedbacks() {
        ArrayList<Feedback> feedbacks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("feedbacks.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 6) {
                    feedbacks.add(new Feedback(
                            data[0],
                            data[1],
                            data[2],
                            Integer.parseInt(data[3]),
                            data[4],
                            data[5]
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Feedback file not found.");
        }

        return feedbacks;
    }
}


