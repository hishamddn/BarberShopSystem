package barbershopsystem;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;
import java.util.ArrayList;

public class BarbershopGUI extends Application {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final String BG        = "#1A1A2E";   // deep charcoal
    private static final String PANEL     = "#16213E";   // slightly lighter panel
    private static final String GOLD      = "#D4AF37";   // warm gold accent
    private static final String GOLD_DARK = "#A8860A";   // pressed gold
    private static final String TEXT      = "#F0EAD6";   // warm off-white
    private static final String MUTED     = "#8A8A9A";   // muted label text
    private static final String SUCCESS   = "#4CAF82";   // green for success
    private static final String DANGER    = "#E05C5C";   // red for errors

    // ── Common style strings ─────────────────────────────────────────────────
    private static final String STYLE_SCENE =
            "-fx-background-color: " + BG + ";";

    private static final String STYLE_TITLE =
            "-fx-font-family: 'Georgia'; -fx-font-size: 22px; " +
                    "-fx-font-weight: bold; -fx-text-fill: " + GOLD + ";";

    private static final String STYLE_SUBTITLE =
            "-fx-font-family: 'Georgia'; -fx-font-size: 13px; " +
                    "-fx-text-fill: " + MUTED + "; -fx-font-style: italic;";

    private static final String STYLE_LABEL =
            "-fx-font-family: 'Arial'; -fx-font-size: 13px; " +
                    "-fx-text-fill: " + TEXT + ";";

    private static final String STYLE_FIELD =
            "-fx-background-color: #0F3460; -fx-text-fill: " + TEXT + "; " +
                    "-fx-border-color: #2E4070; -fx-border-radius: 4; " +
                    "-fx-background-radius: 4; -fx-padding: 8; -fx-font-size: 13px;";

    private static final String STYLE_BTN_PRIMARY =
            "-fx-background-color: " + GOLD + "; -fx-text-fill: #1A1A2E; " +
                    "-fx-font-weight: bold; -fx-font-size: 13px; " +
                    "-fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 10 20;";

    private static final String STYLE_BTN_SECONDARY =
            "-fx-background-color: transparent; -fx-text-fill: " + GOLD + "; " +
                    "-fx-border-color: " + GOLD + "; -fx-border-radius: 5; " +
                    "-fx-background-radius: 5; -fx-font-size: 13px; " +
                    "-fx-cursor: hand; -fx-padding: 10 20;";

    private static final String STYLE_BTN_DANGER =
            "-fx-background-color: " + DANGER + "; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-font-size: 13px; " +
                    "-fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 10 20;";

    private static final String STYLE_STAT_CARD =
            "-fx-background-color: " + PANEL + "; " +
                    "-fx-border-color: " + GOLD + "; -fx-border-width: 0 0 0 3; " +
                    "-fx-padding: 12 16; -fx-background-radius: 4;";

    private static final String STYLE_ERROR =
            "-fx-text-fill: " + DANGER + "; -fx-font-size: 12px;";

    private static final String STYLE_SUCCESS_MSG =
            "-fx-text-fill: " + SUCCESS + "; -fx-font-size: 12px;";

    // ── Primary stage reference ───────────────────────────────────────────────
    private Stage primaryStage;

    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Load all data (mirrors BarbershopSystem.main startup)
        BarbershopSystem.adminList       = FileHandler.loadAdmins();
        BarbershopSystem.barberList      = FileHandler.loadBarbers();
        if (!BarbershopSystem.barberList.isEmpty()) {
            // Get the last barber in the list (e.g., B005)
            String lastID = BarbershopSystem.barberList.get(BarbershopSystem.barberList.size() - 1).getStaffID();

            // Extract the numeric part (removes "B" and parses "005" to integer 5)
            int lastNumber = Integer.parseInt(lastID.substring(1));

            // Set the counter to the next available number (e.g., 6)
            Barber.setCounter(lastNumber + 1);
        }
        BarbershopSystem.appointmentList = FileHandler.loadAppointments();
        BarbershopSystem.paymentList     = FileHandler.loadPayments();
        BarbershopSystem.feedbackList    = FileHandler.loadFeedbacks();

        // First-launch: if no admin, show setup screen
        if (BarbershopSystem.adminList.isEmpty()) {
            showFirstLaunchScreen();
        } else {
            showMainMenu();
        }
    }

    // =========================================================================
    // SCREEN 0 — First-Launch Admin Setup
    // =========================================================================
    private void showFirstLaunchScreen() {
        VBox root = new VBox(14);
        root.setStyle(STYLE_SCENE + " -fx-padding: 50;");
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(420);

        Label title = new Label("First-Time Setup");
        title.setStyle(STYLE_TITLE);

        Label sub = new Label("Create the first admin account to get started.");
        sub.setStyle(STYLE_SUBTITLE);

        TextField nameF     = styledField("Full Name");
        TextField phoneF    = styledField("Phone Number");
        TextField usernameF = styledField("Username");
        PasswordField passF = styledPassField("Password");
        Label errLbl = new Label("");
        errLbl.setStyle(STYLE_ERROR);

        Button createBtn = primaryBtn("Create Admin Account");
        createBtn.setMaxWidth(Double.MAX_VALUE);

        createBtn.setOnAction(e -> {
            String n = nameF.getText().trim();
            String p = phoneF.getText().trim();
            String u = usernameF.getText().trim();
            String pw = passF.getText();
            if (n.isEmpty() || p.isEmpty() || u.isEmpty() || pw.isEmpty()) {
                errLbl.setText("All fields are required.");
                return;
            }
            Admin firstAdmin = new Admin(n, p, u, pw);
            BarbershopSystem.adminList.add(firstAdmin);
            FileHandler.saveAdmins(BarbershopSystem.adminList);
            showMainMenu();
        });

        root.getChildren().addAll(
                title, sub,
                labeledField("Name", nameF),
                labeledField("Phone", phoneF),
                labeledField("Username", usernameF),
                labeledField("Password", passF),
                errLbl, createBtn
        );

        Scene scene = new Scene(wrapCenter(root), 520, 520);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Barbershop System — Setup");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // =========================================================================
    // SCREEN 1 — Main Menu
    // =========================================================================
    private void showMainMenu() {
        VBox root = new VBox(16);
        root.setStyle(STYLE_SCENE + " -fx-padding: 50 60;");
        root.setAlignment(Pos.CENTER);

        // Decorative top bar
        HBox bar = new HBox();
        bar.setStyle("-fx-background-color: " + GOLD + "; -fx-min-height: 4;");
        bar.setMaxWidth(60);

        Label title = new Label("✂  BarberShop");
        title.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 28px; " +
                "-fx-font-weight: bold; -fx-text-fill: " + GOLD + ";");

        Label sub = new Label("Management System");
        sub.setStyle(STYLE_SUBTITLE + " -fx-font-size: 14px;");

        Region spacer = new Region();
        spacer.setMinHeight(20);

        Button adminBtn    = primaryBtn("Admin");
        Button barberBtn   = secondaryBtn("Barber");
        Button customerBtn = secondaryBtn("Customer");
        Button exitBtn     = dangerBtn("Exit");

        for (Button b : new Button[]{adminBtn, barberBtn, customerBtn, exitBtn}) {
            b.setMaxWidth(260);
        }

        adminBtn.setOnAction(e    -> showAdminLogin());
        barberBtn.setOnAction(e   -> showBarberLogin());
        customerBtn.setOnAction(e -> showCustomerMenu());
        exitBtn.setOnAction(e     -> {
            saveAllData();
            primaryStage.close();
        });

        root.getChildren().addAll(
                bar, title, sub, spacer,
                adminBtn, barberBtn, customerBtn, exitBtn
        );

        Scene scene = new Scene(wrapCenter(root), 480, 440);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Barbershop System");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // =========================================================================
    // SCREEN 2 — Admin Login
    // =========================================================================
    private void showAdminLogin() {
        final int[] attempts = {0};

        VBox root = new VBox(14);
        root.setStyle(STYLE_SCENE + " -fx-padding: 50;");
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(380);

        Label title = new Label("Admin Login");
        title.setStyle(STYLE_TITLE);

        TextField usernameF = styledField("Username");
        PasswordField passF = styledPassField("Password");
        Label errLbl = new Label("");
        errLbl.setStyle(STYLE_ERROR);

        Button loginBtn = primaryBtn("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        Button backBtn = secondaryBtn("← Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);

        loginBtn.setOnAction(e -> {
            if (attempts[0] >= 3) return;
            String u = usernameF.getText().trim();
            String p = passF.getText();
            Admin found = null;
            for (Admin a : BarbershopSystem.adminList) {
                if (a.login(u, p)) { found = a; break; }
            }
            if (found != null) {
                showAdminDashboard(found);
            } else {
                attempts[0]++;
                int left = 3 - attempts[0];
                if (left == 0) {
                    errLbl.setText("Too many failed attempts. Returning to menu.");
                    loginBtn.setDisable(true);
                    // auto-return after short delay
                    javafx.animation.PauseTransition delay =
                            new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
                    delay.setOnFinished(ev -> showMainMenu());
                    delay.play();
                } else {
                    errLbl.setText("Invalid credentials. " + left + " attempt(s) left.");
                }
            }
        });

        backBtn.setOnAction(e -> showMainMenu());

        root.getChildren().addAll(
                title,
                labeledField("Username", usernameF),
                labeledField("Password", passF),
                errLbl, loginBtn, backBtn
        );

        Scene scene = new Scene(wrapCenter(root), 480, 420);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Login");
    }

    // =========================================================================
    // SCREEN 3 — Admin Dashboard
    // =========================================================================
    private void showAdminDashboard(Admin admin) {
        BorderPane root = new BorderPane();
        root.setStyle(STYLE_SCENE);

        // ── Sidebar ───────────────────────────────────────────────────────────
        VBox sidebar = new VBox(10);
        sidebar.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 30 20;");
        sidebar.setMinWidth(220);

        Label brand = new Label("✂  BarberShop");
        brand.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-text-fill: " + GOLD + ";");
        Label adminName = new Label("Admin: " + admin.getName());
        adminName.setStyle(STYLE_SUBTITLE);

        Region gap = new Region(); gap.setMinHeight(20);

        // 1. ADDED: Added "🏠 Dashboard Overview" to the button array
        Button[] sideButtons = {
                sideBtn("🏠  Dashboard Overview"),
                sideBtn("📅  Book Appointment"),
                sideBtn("🗂  Manage Appointments"),
                sideBtn("✅  Complete & Pay"),
                sideBtn("💈  Manage Barbers"),
                sideBtn("⭐  View Feedback"),
                sideBtn("📋  All Appointments"),
        };

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(STYLE_BTN_DANGER + " -fx-max-width: 180;");
        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(brand, adminName, gap);
        sidebar.getChildren().addAll(sideButtons);
        sidebar.getChildren().addAll(bottomSpacer, logoutBtn);

        // ── Main content area (swappable) ─────────────────────────────────────
        ScrollPane contentArea = new ScrollPane();
        contentArea.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + ";");
        contentArea.setFitToWidth(true);

        // Build and show the stats home pane
        Runnable showHome = () -> contentArea.setContent(buildAdminHomePane());
        showHome.run();

        // 2. UPDATED: Remapped indices since "Dashboard Overview" is now at index 0
        sideButtons[0].setOnAction(e -> showHome.run()); // Sets content back to the stats panel
        sideButtons[1].setOnAction(e -> contentArea.setContent(buildBookingPane(null)));
        sideButtons[2].setContentDisplay(null); // original index 1 shifts to index 2
        sideButtons[2].setOnAction(e -> contentArea.setContent(buildManageAppointmentsPane()));
        sideButtons[3].setOnAction(e -> contentArea.setContent(buildCompletePayPane()));
        sideButtons[4].setOnAction(e -> contentArea.setContent(buildManageBarbersPane()));
        sideButtons[5].setOnAction(e -> contentArea.setContent(buildFeedbackPane()));
        sideButtons[6].setOnAction(e -> contentArea.setContent(buildAllAppointmentsPane()));

        logoutBtn.setOnAction(e -> { saveAllData(); showMainMenu(); });

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 920, 620);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Admin Dashboard");
    }

    // Admin home pane — live stats
    private VBox buildAdminHomePane() {
        VBox pane = new VBox(20);
        pane.setStyle("-fx-padding: 30;");

        Label heading = new Label("Dashboard Overview");
        heading.setStyle(STYLE_TITLE);

        long booked     = BarbershopSystem.appointmentList.stream().filter(a -> a.getStatus().equals("Booked")).count();
        long inProgress = BarbershopSystem.appointmentList.stream().filter(a -> a.getStatus().equals("In Progress")).count();
        long completed  = BarbershopSystem.appointmentList.stream().filter(a -> a.getStatus().equals("Completed")).count();
        long cancelled  = BarbershopSystem.appointmentList.stream().filter(a -> a.getStatus().equals("Cancelled")).count();
        double revenue  = BarbershopSystem.paymentList.stream().mapToDouble(Payment::getAmount).sum();

        GridPane stats = new GridPane();
        stats.setHgap(14); stats.setVgap(14);

        stats.add(statCard("Total Appointments", String.valueOf(BarbershopSystem.appointmentList.size())), 0, 0);
        stats.add(statCard("Booked",             String.valueOf(booked)),    1, 0);
        stats.add(statCard("In Progress",         String.valueOf(inProgress)), 2, 0);
        stats.add(statCard("Completed",           String.valueOf(completed)), 0, 1);
        stats.add(statCard("Cancelled",           String.valueOf(cancelled)), 1, 1);
        stats.add(statCard("Total Barbers",       String.valueOf(BarbershopSystem.barberList.size())), 2, 1);
        stats.add(statCard("Total Feedback",      String.valueOf(BarbershopSystem.feedbackList.size())), 0, 2);
        stats.add(statCard("Total Revenue",       "RM " + String.format("%.2f", revenue)), 1, 2);

        pane.getChildren().addAll(heading, stats);
        return pane;
    }

    // =========================================================================
    // SCREEN 4 — Barber Login
    // =========================================================================
    private void showBarberLogin() {
        final int[] attempts = {0};

        VBox root = new VBox(14);
        root.setStyle(STYLE_SCENE + " -fx-padding: 50;");
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(380);

        Label title = new Label("Barber Login");
        title.setStyle(STYLE_TITLE);

        TextField usernameF = styledField("Username");
        PasswordField passF = styledPassField("Password");
        Label errLbl = new Label("");
        errLbl.setStyle(STYLE_ERROR);

        Button loginBtn = primaryBtn("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        Button backBtn = secondaryBtn("← Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);

        loginBtn.setOnAction(e -> {
            if (attempts[0] >= 3) return;
            String u = usernameF.getText().trim();
            String p = passF.getText();
            Barber found = null;
            for (Barber b : BarbershopSystem.barberList) {
                if (b.getUsername().equals(u) && b.getPassword().equals(p)) {
                    found = b; break;
                }
            }
            if (found != null) {
                showBarberDashboard(found);
            } else {
                attempts[0]++;
                int left = 3 - attempts[0];
                if (left == 0) {
                    errLbl.setText("Too many failed attempts. Returning to menu.");
                    loginBtn.setDisable(true);
                    javafx.animation.PauseTransition delay =
                            new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
                    delay.setOnFinished(ev -> showMainMenu());
                    delay.play();
                } else {
                    errLbl.setText("Invalid credentials. " + left + " attempt(s) left.");
                }
            }
        });

        backBtn.setOnAction(e -> showMainMenu());

        root.getChildren().addAll(
                title,
                labeledField("Username", usernameF),
                labeledField("Password", passF),
                errLbl, loginBtn, backBtn
        );

        Scene scene = new Scene(wrapCenter(root), 480, 420);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Barber Login");
    }

    // =========================================================================
    // SCREEN 5 — Barber Dashboard
    // =========================================================================
    private void showBarberDashboard(Barber barber) {
        BorderPane root = new BorderPane();
        root.setStyle(STYLE_SCENE);

        // ── Sidebar ───────────────────────────────────────────────────────────
        VBox sidebar = new VBox(10);
        sidebar.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 30 20;");
        sidebar.setMinWidth(220);

        Label brand = new Label("✂  BarberShop");
        brand.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-text-fill: " + GOLD + ";");
        Label nameLbl = new Label("Barber: " + barber.getName());
        nameLbl.setStyle(STYLE_SUBTITLE);

        Region gap = new Region(); gap.setMinHeight(20);

        // 1. ADDED: Added "🏠 Dashboard Overview" as the first button in the array
        Button[] sideButtons = {
                sideBtn("🏠  Dashboard Overview"),
                sideBtn("📅  My Schedule"),
                sideBtn("▶   Mark In Progress"),
                sideBtn("✅  Complete & Collect"),
        };

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(STYLE_BTN_DANGER + " -fx-max-width: 180;");
        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(brand, nameLbl, gap);
        sidebar.getChildren().addAll(sideButtons);
        sidebar.getChildren().addAll(bottomSpacer, logoutBtn);

        ScrollPane contentArea = new ScrollPane();
        contentArea.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + ";");
        contentArea.setFitToWidth(true);

        // Set initial home content view
        ArrayList<Appointment> myApts = getAppointmentsForBarber(barber.getName());
        contentArea.setContent(buildBarberHomePane(barber, myApts));

        // 2. UPDATED: Configured actions and shifted indices down by 1
        sideButtons[0].setOnAction(e -> {
            // Re-fetch appointments so the home overview stats refresh live when clicked
            ArrayList<Appointment> updatedApts = getAppointmentsForBarber(barber.getName());
            contentArea.setContent(buildBarberHomePane(barber, updatedApts));
        });
        sideButtons[1].setOnAction(e -> contentArea.setContent(buildBarberSchedulePane(barber)));
        sideButtons[2].setOnAction(e -> contentArea.setContent(buildBarberMarkInProgressPane(barber)));
        sideButtons[3].setOnAction(e -> contentArea.setContent(buildBarberCompletePane(barber)));

        logoutBtn.setOnAction(e -> { saveAllData(); showMainMenu(); });

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 860, 580);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Barber Dashboard — " + barber.getName());
    }

    private VBox buildBarberHomePane(Barber barber, ArrayList<Appointment> myApts) {
        VBox pane = new VBox(20);
        pane.setStyle("-fx-padding: 30;");

        Label heading = new Label("My Dashboard");
        heading.setStyle(STYLE_TITLE);

        long myBooked     = myApts.stream().filter(a -> a.getStatus().equals("Booked")).count();
        long myInProgress = myApts.stream().filter(a -> a.getStatus().equals("In Progress")).count();
        long myCompleted  = myApts.stream().filter(a -> a.getStatus().equals("Completed")).count();

        HBox stats = new HBox(14);
        stats.getChildren().addAll(
                statCard("My Appointments", String.valueOf(myApts.size())),
                statCard("Booked",          String.valueOf(myBooked)),
                statCard("In Progress",      String.valueOf(myInProgress)),
                statCard("Completed",        String.valueOf(myCompleted))
        );

        pane.getChildren().addAll(heading, stats);
        return pane;
    }

    // =========================================================================
    // SCREEN 6 — Customer Menu
    // =========================================================================
    private void showCustomerMenu() {
        VBox root = new VBox(16);
        root.setStyle(STYLE_SCENE + " -fx-padding: 60;");
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Customer");
        title.setStyle(STYLE_TITLE);
        Label sub = new Label("No account needed");
        sub.setStyle(STYLE_SUBTITLE);

        Button bookBtn     = primaryBtn("📅  Book Appointment");
        Button feedbackBtn = secondaryBtn("⭐  Leave Feedback");
        Button backBtn     = secondaryBtn("← Back");

        bookBtn.setMaxWidth(260);
        feedbackBtn.setMaxWidth(260);
        backBtn.setMaxWidth(260);

        bookBtn.setOnAction(e -> showBookingForm());
        feedbackBtn.setOnAction(e -> showFeedbackForm());
        backBtn.setOnAction(e -> showMainMenu());

        root.getChildren().addAll(title, sub, new Region() {{ setMinHeight(10); }},
                bookBtn, feedbackBtn, backBtn);

        Scene scene = new Scene(wrapCenter(root), 480, 380);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer");
    }

    // =========================================================================
    // SCREEN 7 — Booking Form
    // =========================================================================
    private void showBookingForm() {
        showBookingFormInStage(primaryStage, true);
    }

    // Shared booking form used by both Customer screen and Admin "Book" pane
    // If isFullScreen, replaces primaryStage scene; otherwise returns as a VBox (for admin pane).
    private void showBookingFormInStage(Stage stage, boolean showBackBtn) {
        stage.setScene(new Scene(wrapCenter(buildBookingPane(showBackBtn ? () -> showCustomerMenu() : null)), 560, 700));
        stage.setTitle("Book Appointment");
    }

    private VBox buildBookingPane(Runnable onBack) {
        VBox pane = new VBox(12);
        pane.setStyle("-fx-padding: 30;");

        Label title = new Label("Book Appointment");
        title.setStyle(STYLE_TITLE);

        if (BarbershopSystem.barberList.isEmpty()) {
            Label noBarber = new Label("No barbers available. Admin must add barbers first.");
            noBarber.setStyle(STYLE_ERROR);
            pane.getChildren().addAll(title, noBarber);
            if (onBack != null) {
                Button b = secondaryBtn("← Back");
                b.setOnAction(e -> onBack.run());
                pane.getChildren().add(b);
            }
            return pane;
        }

        // ── Customer Info ─────────────────────────────────────────────────────
        Label secCust = sectionLabel("Customer Details");
        TextField nameF  = styledField("Full Name");
        TextField phoneF = styledField("Phone Number");
        TextField emailF = styledField("Email Address");

        // ── Barber select ─────────────────────────────────────────────────────
        Label secBarber = sectionLabel("Select Barber");
        ComboBox<String> barberCB = new ComboBox<>();
        barberCB.setStyle(STYLE_FIELD + " -fx-min-width: 300;");
        barberCB.setPromptText("— Choose a barber —");
        for (Barber b : BarbershopSystem.barberList) {
            barberCB.getItems().add(b.getName() + "  [" + b.getStaffID() + "]");
        }

        // ── Service select ────────────────────────────────────────────────────
        Label secService = sectionLabel("Select Service");
        ToggleGroup serviceGroup = new ToggleGroup();
        RadioButton rb1 = serviceRadio("Crop Cut   — RM 15.00", serviceGroup);
        RadioButton rb2 = serviceRadio("Fade Cut    — RM 18.00", serviceGroup);
        RadioButton rb3 = serviceRadio("Bowl Cut   — RM 12.00", serviceGroup);
        rb1.setSelected(true);

        CheckBox shaveCB = new CheckBox("Add Shave (+RM 5.00)");
        shaveCB.setStyle("-fx-text-fill: " + TEXT + "; -fx-font-size: 13px;");

        // ── Date & Time ───────────────────────────────────────────────────────
        Label secDT = sectionLabel("Date & Time");
        TextField dateTimeF = styledField("e.g. 2025-06-10 10:00AM");

        // ── Result ────────────────────────────────────────────────────────────
        Label resultLbl = new Label("");
        resultLbl.setWrapText(true);
        resultLbl.setStyle(STYLE_ERROR);

        Button bookBtn = primaryBtn("Confirm Booking");
        bookBtn.setMaxWidth(Double.MAX_VALUE);

        bookBtn.setOnAction(e -> {
            // Validate
            String custName  = nameF.getText().trim();
            String custPhone = phoneF.getText().trim();
            String custEmail = emailF.getText().trim();
            String dateTime  = dateTimeF.getText().trim();
            int barberIdx    = barberCB.getSelectionModel().getSelectedIndex();

            if (custName.isEmpty() || custPhone.isEmpty() || custEmail.isEmpty() || dateTime.isEmpty()) {
                resultLbl.setStyle(STYLE_ERROR);
                resultLbl.setText("Please fill in all fields.");
                return;
            }
            if (barberIdx < 0) {
                resultLbl.setStyle(STYLE_ERROR);
                resultLbl.setText("Please select a barber.");
                return;
            }

            Barber selectedBarber = BarbershopSystem.barberList.get(barberIdx);

            // Determine service
            String haircutType; double basePrice;
            if (rb1.isSelected())      { haircutType = "Crop Cut"; basePrice = 15.00; }
            else if (rb2.isSelected()) { haircutType = "Fade Cut"; basePrice = 18.00; }
            else                       { haircutType = "Bowl Cut"; basePrice = 12.00; }

            boolean shave = shaveCB.isSelected();
            Service selectedService = new Service("S001", haircutType, shave, basePrice);

            // Schedule conflict check
            if (hasScheduleConflict(selectedBarber, dateTime)) {
                resultLbl.setStyle(STYLE_ERROR);
                resultLbl.setText(selectedBarber.getName() +
                        " already has an appointment at " + dateTime +
                        ". Please choose a different time or barber.");
                return;
            }

            Customer customer = new Customer(custName, custPhone, custEmail);
            Appointment apt = customer.bookAppointment(selectedBarber, selectedService, dateTime);
            BarbershopSystem.appointmentList.add(apt);
            FileHandler.saveAppointments(BarbershopSystem.appointmentList);

            resultLbl.setStyle(STYLE_SUCCESS_MSG);
            resultLbl.setText("✅ Booking confirmed!\n" + apt.getDetails() +
                    "\n\nPayment will be collected after your service.");
            bookBtn.setDisable(true);
        });

        VBox serviceBox = new VBox(6, rb1, rb2, rb3, shaveCB);

        pane.getChildren().addAll(
                title, secCust,
                labeledField("Name", nameF),
                labeledField("Phone", phoneF),
                labeledField("Email", emailF),
                secBarber, barberCB,
                secService, serviceBox,
                secDT, dateTimeF,
                bookBtn, resultLbl
        );

        if (onBack != null) {
            Button backBtn = secondaryBtn("← Back");
            backBtn.setOnAction(e -> onBack.run());
            pane.getChildren().add(backBtn);
        }

        return pane;
    }

    // =========================================================================
    // SCREEN 8 — Feedback Form
    // =========================================================================
    private void showFeedbackForm() {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");

        Label title = new Label("Leave Feedback");
        title.setStyle(STYLE_TITLE);
        Label sub = new Label("Available only for completed appointments.");
        sub.setStyle(STYLE_SUBTITLE);

        TextField aptIDField = styledField("Appointment ID (number)");
        Label aptInfoLbl = new Label("");
        aptInfoLbl.setWrapText(true);
        aptInfoLbl.setStyle(STYLE_SUBTITLE);

        Label secRating = sectionLabel("Rating");
        ToggleGroup ratingGroup = new ToggleGroup();
        HBox stars = new HBox(8);
        for (int i = 1; i <= 5; i++) {
            RadioButton rb = new RadioButton("★ " + i);
            rb.setToggleGroup(ratingGroup);
            rb.setStyle("-fx-text-fill: " + GOLD + "; -fx-font-size: 14px;");
            rb.setUserData(i);
            if (i == 5) rb.setSelected(true);
            stars.getChildren().add(rb);
        }

        Label secComment = sectionLabel("Comment");
        TextArea commentArea = new TextArea();
        commentArea.setStyle(STYLE_FIELD + " -fx-pref-height: 90;");
        commentArea.setPromptText("Share your experience...");
        commentArea.setWrapText(true);

        Label resultLbl = new Label("");
        resultLbl.setWrapText(true);
        resultLbl.setStyle(STYLE_ERROR);

        Button submitBtn = primaryBtn("Submit Feedback");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        Button backBtn = secondaryBtn("← Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);

        submitBtn.setOnAction(e -> {
            String idText = aptIDField.getText().trim();
            if (idText.isEmpty()) {
                resultLbl.setStyle(STYLE_ERROR);
                resultLbl.setText("Please enter your Appointment ID.");
                return;
            }
            int aptID;
            try { aptID = Integer.parseInt(idText); }
            catch (NumberFormatException ex) {
                resultLbl.setStyle(STYLE_ERROR);
                resultLbl.setText("Appointment ID must be a number.");
                return;
            }

            Appointment apt = findAppointment(aptID);
            if (apt == null) {
                resultLbl.setStyle(STYLE_ERROR);
                resultLbl.setText("Appointment ID not found.");
                return;
            }
            if (!apt.getStatus().equals("Completed")) {
                resultLbl.setStyle(STYLE_ERROR);
                resultLbl.setText("Feedback only allowed after service is completed.\nCurrent status: " + apt.getStatus());
                return;
            }
            // Duplicate check
            for (Feedback f : BarbershopSystem.feedbackList) {
                if (f.getCustomerName().equals(apt.getCustomer().getName()) &&
                        f.getBarberName().equals(apt.getBarber().getName())) {
                    resultLbl.setStyle(STYLE_ERROR);
                    resultLbl.setText("Feedback already submitted for this appointment.");
                    return;
                }
            }

            int rating = (int) ratingGroup.getSelectedToggle().getUserData();
            String comment = commentArea.getText().trim();
            if (comment.isEmpty()) {
                resultLbl.setStyle(STYLE_ERROR);
                resultLbl.setText("Please enter a comment.");
                return;
            }

            String today = java.time.LocalDate.now().toString();
            String fID   = "F" + (BarbershopSystem.feedbackList.size() + 1);
            Feedback feedback = new Feedback(
                    fID, apt.getCustomer().getName(), apt.getBarber().getName(),
                    rating, comment, today
            );
            feedback.submitFeedback();
            BarbershopSystem.feedbackList.add(feedback);
            FileHandler.saveFeedbacks(BarbershopSystem.feedbackList);

            resultLbl.setStyle(STYLE_SUCCESS_MSG);
            resultLbl.setText("✅ Thank you for your feedback!\n" + feedback.getDetails());
            submitBtn.setDisable(true);
        });

        backBtn.setOnAction(ev -> showCustomerMenu());

        pane.getChildren().addAll(
                title, sub,
                labeledField("Appointment ID", aptIDField),
                aptInfoLbl,
                secRating, stars,
                secComment, commentArea,
                submitBtn, resultLbl, backBtn
        );

        primaryStage.setScene(new Scene(wrapCenter(pane), 520, 600));
        primaryStage.setTitle("Leave Feedback");
    }

    // =========================================================================
    // ADMIN CONTENT PANES (used inside the dashboard scroll area)
    // =========================================================================

    // Manage Appointments
    private VBox buildManageAppointmentsPane() {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");
        Label title = new Label("Manage Appointments");
        title.setStyle(STYLE_TITLE);

        if (BarbershopSystem.appointmentList.isEmpty()) {
            pane.getChildren().addAll(title, noDataLabel("No appointments yet."));
            return pane;
        }

        ListView<String> listView = appointmentListView(BarbershopSystem.appointmentList);

        TextField idField = styledField("Appointment ID");
        TextField newDT   = styledField("New Date & Time (for update)");
        Label result = new Label(""); result.setWrapText(true); result.setStyle(STYLE_SUCCESS_MSG);

        Button updateBtn = secondaryBtn("Update Date/Time");
        Button cancelBtn = dangerBtn("Cancel Appointment");

        HBox btnRow = new HBox(10, updateBtn, cancelBtn);

        updateBtn.setOnAction(e -> {
            Appointment apt = getSelectedApt(idField);
            if (apt == null) { result.setStyle(STYLE_ERROR); result.setText("Appointment not found."); return; }
            if (!apt.getStatus().equals("Booked")) {
                result.setStyle(STYLE_ERROR); result.setText("Only Booked appointments can be updated."); return;
            }
            String dt = newDT.getText().trim();
            if (dt.isEmpty()) { result.setStyle(STYLE_ERROR); result.setText("Enter new date & time."); return; }
            apt.updateAppointment(dt);
            FileHandler.saveAppointments(BarbershopSystem.appointmentList);
            result.setStyle(STYLE_SUCCESS_MSG); result.setText("✅ Updated: " + apt.getDetails());
            listView.getItems().setAll(appointmentStrings(BarbershopSystem.appointmentList));
        });

        cancelBtn.setOnAction(e -> {
            Appointment apt = getSelectedApt(idField);
            if (apt == null) { result.setStyle(STYLE_ERROR); result.setText("Appointment not found."); return; }
            if (apt.getStatus().equals("Cancelled")) { result.setStyle(STYLE_ERROR); result.setText("Already cancelled."); return; }
            if (apt.getStatus().equals("Completed")) { result.setStyle(STYLE_ERROR); result.setText("Cannot cancel a completed appointment."); return; }
            apt.cancel();
            FileHandler.saveAppointments(BarbershopSystem.appointmentList);
            result.setStyle(STYLE_SUCCESS_MSG); result.setText("✅ Cancelled.");
            listView.getItems().setAll(appointmentStrings(BarbershopSystem.appointmentList));
        });

        pane.getChildren().addAll(
                title, listView,
                sectionLabel("Action"),
                labeledField("Appointment ID", idField),
                labeledField("New Date & Time (update only)", newDT),
                btnRow, result
        );
        return pane;
    }

    // Complete & Pay
    private VBox buildCompletePayPane() {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");
        Label title = new Label("Complete & Process Payment");
        title.setStyle(STYLE_TITLE);

        ArrayList<Appointment> active = new ArrayList<>();
        for (Appointment a : BarbershopSystem.appointmentList) {
            if (a.getStatus().equals("Booked") || a.getStatus().equals("In Progress")) active.add(a);
        }

        if (active.isEmpty()) {
            pane.getChildren().addAll(title, noDataLabel("No active appointments."));
            return pane;
        }

        ListView<String> listView = appointmentListView(active);

        TextField idField = styledField("Appointment ID");
        ComboBox<String> methodCB = new ComboBox<>();
        methodCB.setStyle(STYLE_FIELD);
        methodCB.getItems().addAll("Cash", "Card", "E-Wallet");
        methodCB.setValue("Cash");

        Label result = new Label(""); result.setWrapText(true); result.setStyle(STYLE_SUCCESS_MSG);
        Button completeBtn = primaryBtn("Complete & Collect Payment");

        completeBtn.setOnAction(e -> {
            Appointment apt = getSelectedApt(idField);
            if (apt == null) { result.setStyle(STYLE_ERROR); result.setText("Appointment not found."); return; }
            if (apt.getStatus().equals("Cancelled") || apt.getStatus().equals("Completed")) {
                result.setStyle(STYLE_ERROR); result.setText("Cannot process. Status: " + apt.getStatus()); return;
            }
            apt.setStatus("Completed");
            Payment payment = new Payment(
                    apt.getAppointmentID(),
                    apt.getService().getPrice(),
                    methodCB.getValue()
            );
            BarbershopSystem.paymentList.add(payment);
            FileHandler.saveAppointments(BarbershopSystem.appointmentList);
            FileHandler.savePayments(BarbershopSystem.paymentList);
            result.setStyle(STYLE_SUCCESS_MSG);
            result.setText("✅ Payment collected!\n");
            payment.paymentDetails(); // Assuming this method internally updates or prints what you need
            listView.getItems().setAll(appointmentStrings(active));
        });

        pane.getChildren().addAll(
                title, listView,
                sectionLabel("Process"),
                labeledField("Appointment ID", idField),
                labeledField("Payment Method", methodCB),
                completeBtn, result
        );
        return pane;
    }

    // Manage Barbers
    private VBox buildManageBarbersPane() {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");
        Label title = new Label("Manage Barbers");
        title.setStyle(STYLE_TITLE);

        // Barber list
        ListView<String> listView = new ListView<>();
        listView.setStyle("-fx-background-color: " + PANEL + "; -fx-text-fill: " + TEXT + ";");
        listView.setPrefHeight(160);
        Runnable refreshList = () -> {
            listView.getItems().clear();
            for (Barber b : BarbershopSystem.barberList) {
                listView.getItems().add(
                        b.getStaffID() + "  |  " + b.getName() +
                                "  |  " + b.getPhoneNumber() +
                                "  |  @" + b.getUsername() +
                                "  |  " + (b.getStatus() ? "Available" : "Unavailable")
                );
            }
        };
        refreshList.run();

        // Add new barber form
        Label addSec = sectionLabel("Add New Barber");
        TextField bName  = styledField("Name");
        TextField bPhone = styledField("Phone");
        TextField bUser  = styledField("Username");
        PasswordField bPass = styledPassField("Password");
        Label addResult  = new Label(""); addResult.setStyle(STYLE_SUCCESS_MSG);

        Button addBtn = primaryBtn("Add Barber");
        addBtn.setOnAction(e -> {
            String n = bName.getText().trim(), p = bPhone.getText().trim(),
                    u = bUser.getText().trim(), pw = bPass.getText();
            if (n.isEmpty() || p.isEmpty() || u.isEmpty() || pw.isEmpty()) {
                addResult.setStyle(STYLE_ERROR); addResult.setText("All fields required."); return;
            }
            Barber newBarber = new Barber(n, p, u, pw, true);
            BarbershopSystem.barberList.add(newBarber);
            FileHandler.saveBarbers(BarbershopSystem.barberList);
            addResult.setStyle(STYLE_SUCCESS_MSG);
            addResult.setText("✅ Barber added. ID: " + newBarber.getStaffID());
            refreshList.run();
            bName.clear(); bPhone.clear(); bUser.clear(); bPass.clear();
        });

        // Delete barber
        Label delSec = sectionLabel("Delete Barber");
        TextField delIDField = styledField("Staff ID to delete");
        Label delResult = new Label(""); delResult.setStyle(STYLE_SUCCESS_MSG);
        Button delBtn = dangerBtn("Delete Barber");
        delBtn.setOnAction(e -> {
            String sid = delIDField.getText().trim();
            Barber target = findBarber(sid);
            if (target == null) { delResult.setStyle(STYLE_ERROR); delResult.setText("Barber not found."); return; }
            boolean hasActive = BarbershopSystem.appointmentList.stream().anyMatch(
                    a -> a.getBarber().getStaffID().equals(sid) &&
                            (a.getStatus().equals("Booked") || a.getStatus().equals("In Progress"))
            );
            if (hasActive) {
                delResult.setStyle(STYLE_ERROR); delResult.setText("Cannot delete — barber has active appointments."); return;
            }
            BarbershopSystem.barberList.remove(target);
            FileHandler.saveBarbers(BarbershopSystem.barberList);
            delResult.setStyle(STYLE_SUCCESS_MSG); delResult.setText("✅ Barber deleted.");
            refreshList.run();
        });

        pane.getChildren().addAll(
                title, listView,
                addSec,
                labeledField("Name", bName),
                labeledField("Phone", bPhone),
                labeledField("Username", bUser),
                labeledField("Password", bPass),
                addBtn, addResult,
                delSec,
                labeledField("Staff ID", delIDField),
                delBtn, delResult
        );
        return pane;
    }

    // View Feedback
    private VBox buildFeedbackPane() {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");
        Label title = new Label("Customer Feedback");
        title.setStyle(STYLE_TITLE);

        if (BarbershopSystem.feedbackList.isEmpty()) {
            pane.getChildren().addAll(title, noDataLabel("No feedback received yet."));
            return pane;
        }

        for (Feedback f : BarbershopSystem.feedbackList) {
            VBox card = new VBox(4);
            card.setStyle(STYLE_STAT_CARD + " -fx-margin: 0 0 8 0;");
            Label customer = new Label("👤 " + f.getCustomerName() + " → 💈 " + f.getBarberName());
            customer.setStyle(STYLE_LABEL + " -fx-font-weight: bold;");
            Label rating  = new Label("★".repeat(f.getRating()) + "  (" + f.getRating() + "/5)");
            rating.setStyle("-fx-text-fill: " + GOLD + "; -fx-font-size: 13px;");
            Label comment = new Label(f.getComment());
            comment.setWrapText(true);
            comment.setStyle(STYLE_SUBTITLE);
            card.getChildren().addAll(customer, rating, comment);
            pane.getChildren().add(card);
        }
        return pane;
    }

    // All Appointments
    private VBox buildAllAppointmentsPane() {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");
        Label title = new Label("All Appointments");
        title.setStyle(STYLE_TITLE);

        if (BarbershopSystem.appointmentList.isEmpty()) {
            pane.getChildren().addAll(title, noDataLabel("No appointments yet."));
            return pane;
        }

        for (Appointment apt : BarbershopSystem.appointmentList) {
            VBox card = new VBox(4);
            card.setStyle(STYLE_STAT_CARD);
            Label idLbl = new Label("Appointment #" + apt.getAppointmentID() +
                    "  |  " + apt.getStatus());
            idLbl.setStyle(STYLE_LABEL + " -fx-font-weight: bold;");
            Label info = new Label(
                    "Customer: " + apt.getCustomer().getName() +
                            "  |  Barber: " + apt.getBarber().getName() +
                            "  |  Service: " + apt.getService().getServiceName() +
                            "  |  Date: " + apt.getDateTime() +
                            "  |  Price: RM" + apt.getService().getPrice()
            );
            info.setWrapText(true);
            info.setStyle(STYLE_SUBTITLE);
            card.getChildren().addAll(idLbl, info);
            pane.getChildren().add(card);
        }
        return pane;
    }

    // Barber: My Schedule
    private VBox buildBarberSchedulePane(Barber barber) {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");
        Label title = new Label("My Schedule");
        title.setStyle(STYLE_TITLE);

        ArrayList<Appointment> myApts = getAppointmentsForBarber(barber.getName());
        if (myApts.isEmpty()) {
            pane.getChildren().addAll(title, noDataLabel("No appointments assigned to you."));
            return pane;
        }

        for (Appointment apt : myApts) {
            pane.getChildren().add(appointmentCard(apt));
        }
        return pane;
    }

    // Barber: Mark In Progress
    private VBox buildBarberMarkInProgressPane(Barber barber) {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");
        Label title = new Label("Mark Appointment as In Progress");
        title.setStyle(STYLE_TITLE);

        ArrayList<Appointment> booked = new ArrayList<>();
        for (Appointment a : getAppointmentsForBarber(barber.getName())) {
            if (a.getStatus().equals("Booked")) booked.add(a);
        }

        if (booked.isEmpty()) {
            pane.getChildren().addAll(title, noDataLabel("No booked appointments."));
            return pane;
        }

        ListView<String> listView = appointmentListView(booked);
        TextField idField = styledField("Appointment ID");
        Label result = new Label(""); result.setStyle(STYLE_SUCCESS_MSG);
        Button markBtn = primaryBtn("Mark as In Progress");

        markBtn.setOnAction(e -> {
            Appointment apt = getSelectedApt(idField);
            if (apt == null || !apt.getBarber().getStaffID().equals(barber.getStaffID())) {
                result.setStyle(STYLE_ERROR); result.setText("Appointment not found in your schedule."); return;
            }
            apt.markInProgress();
            FileHandler.saveAppointments(BarbershopSystem.appointmentList);
            result.setStyle(STYLE_SUCCESS_MSG); result.setText("✅ Marked as In Progress.");
            listView.getItems().setAll(appointmentStrings(booked));
        });

        pane.getChildren().addAll(title, listView, labeledField("Appointment ID", idField), markBtn, result);
        return pane;
    }

    // Barber: Complete & Collect
    private VBox buildBarberCompletePane(Barber barber) {
        VBox pane = new VBox(14);
        pane.setStyle("-fx-padding: 30;");
        Label title = new Label("Complete & Collect Payment");
        title.setStyle(STYLE_TITLE);

        ArrayList<Appointment> active = new ArrayList<>();
        for (Appointment a : getAppointmentsForBarber(barber.getName())) {
            if (a.getStatus().equals("Booked") || a.getStatus().equals("In Progress")) active.add(a);
        }

        if (active.isEmpty()) {
            pane.getChildren().addAll(title, noDataLabel("No active appointments."));
            return pane;
        }

        ListView<String> listView = appointmentListView(active);
        TextField idField = styledField("Appointment ID");
        ComboBox<String> methodCB = new ComboBox<>();
        methodCB.setStyle(STYLE_FIELD);
        methodCB.getItems().addAll("Cash", "Card", "E-Wallet");
        methodCB.setValue("Cash");

        Label result = new Label(""); result.setWrapText(true); result.setStyle(STYLE_SUCCESS_MSG);
        Button completeBtn = primaryBtn("Complete & Collect");

        completeBtn.setOnAction(e -> {
            Appointment apt = getSelectedApt(idField);
            if (apt == null || !apt.getBarber().getStaffID().equals(barber.getStaffID())) {
                result.setStyle(STYLE_ERROR); result.setText("Appointment not found in your schedule."); return;
            }
            apt.setStatus("Completed");
            Payment payment = new Payment(
                    apt.getAppointmentID(),
                    apt.getService().getPrice(),
                    methodCB.getValue()
            );
            BarbershopSystem.paymentList.add(payment);
            FileHandler.saveAppointments(BarbershopSystem.appointmentList);
            FileHandler.savePayments(BarbershopSystem.paymentList);
            result.setStyle(STYLE_SUCCESS_MSG);
            result.setText("✅ Payment collected!\n");
            payment.paymentDetails(); // Assuming this method internally updates or prints what you need
            listView.getItems().setAll(appointmentStrings(active));
        });

        pane.getChildren().addAll(
                title, listView,
                labeledField("Appointment ID", idField),
                labeledField("Payment Method", methodCB),
                completeBtn, result
        );
        return pane;
    }

    // =========================================================================
    // HELPERS — Logic
    // =========================================================================

    private boolean hasScheduleConflict(Barber barber, String dateTime) {
        for (Appointment apt : BarbershopSystem.appointmentList) {
            if (apt.getBarber().getStaffID().equals(barber.getStaffID()) &&
                    apt.getDateTime().equalsIgnoreCase(dateTime) &&
                    (apt.getStatus().equals("Booked") || apt.getStatus().equals("In Progress"))) {
                return true;
            }
        }
        return false;
    }

    private Appointment findAppointment(int id) {
        for (Appointment apt : BarbershopSystem.appointmentList) {
            if (apt.getAppointmentID() == id) return apt;
        }
        return null;
    }

    private Barber findBarber(String staffID) {
        for (Barber b : BarbershopSystem.barberList) {
            if (b.getStaffID().equals(staffID)) return b;
        }
        return null;
    }

    private ArrayList<Appointment> getAppointmentsForBarber(String barberName) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment apt : BarbershopSystem.appointmentList) {
            if (apt.getBarber().getName().equals(barberName)) result.add(apt);
        }
        return result;
    }

    private Appointment getSelectedApt(TextField idField) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            return findAppointment(id);
        } catch (NumberFormatException e) { return null; }
    }

    private void saveAllData() {
        FileHandler.saveAdmins(BarbershopSystem.adminList);
        FileHandler.saveBarbers(BarbershopSystem.barberList);
        FileHandler.saveAppointments(BarbershopSystem.appointmentList);
        FileHandler.savePayments(BarbershopSystem.paymentList);
        FileHandler.saveFeedbacks(BarbershopSystem.feedbackList);
    }

    // =========================================================================
    // HELPERS — UI Factories
    // =========================================================================

    private StackPane wrapCenter(VBox content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + ";");
        StackPane stack = new StackPane(scroll);
        stack.setStyle("-fx-background-color: " + BG + ";");
        return stack;
    }

    private Button primaryBtn(String text) {
        Button b = new Button(text);
        b.setStyle(STYLE_BTN_PRIMARY);
        b.setOnMouseEntered(e -> b.setStyle(STYLE_BTN_PRIMARY.replace(GOLD, GOLD_DARK)));
        b.setOnMouseExited(e  -> b.setStyle(STYLE_BTN_PRIMARY));
        return b;
    }

    private Button secondaryBtn(String text) {
        Button b = new Button(text);
        b.setStyle(STYLE_BTN_SECONDARY);
        return b;
    }

    private Button dangerBtn(String text) {
        Button b = new Button(text);
        b.setStyle(STYLE_BTN_DANGER);
        return b;
    }

    private Button sideBtn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + TEXT + "; " +
                        "-fx-font-size: 13px; -fx-padding: 10 14; -fx-cursor: hand; " +
                        "-fx-border-color: transparent;"
        );
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: #0F3460; -fx-text-fill: " + GOLD + "; " +
                        "-fx-font-size: 13px; -fx-padding: 10 14; -fx-cursor: hand;"
        ));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + TEXT + "; " +
                        "-fx-font-size: 13px; -fx-padding: 10 14; -fx-cursor: hand;"
        ));
        return b;
    }

    private TextField styledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(STYLE_FIELD);
        return f;
    }

    private PasswordField styledPassField(String prompt) {
        PasswordField f = new PasswordField();
        f.setPromptText(prompt);
        f.setStyle(STYLE_FIELD);
        return f;
    }

    private VBox labeledField(String labelText, Control field) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-text-fill: " + MUTED + "; -fx-font-size: 11px; -fx-font-family: 'Arial';");
        VBox box = new VBox(3, lbl, field);
        return box;
    }

    private Label sectionLabel(String text) {
        Label lbl = new Label(text.toUpperCase());
        lbl.setStyle(
                "-fx-text-fill: " + GOLD + "; -fx-font-size: 11px; -fx-font-weight: bold; " +
                        "-fx-font-family: 'Arial'; -fx-padding: 10 0 2 0;"
        );
        return lbl;
    }

    private VBox statCard(String label, String value) {
        VBox card = new VBox(4);
        card.setStyle(STYLE_STAT_CARD);
        card.setMinWidth(160);
        Label valLbl = new Label(value);
        valLbl.setStyle(
                "-fx-font-family: 'Georgia'; -fx-font-size: 22px; " +
                        "-fx-font-weight: bold; -fx-text-fill: " + GOLD + ";"
        );
        Label nameLbl = new Label(label);
        nameLbl.setStyle("-fx-text-fill: " + MUTED + "; -fx-font-size: 12px; -fx-font-family: 'Arial';");
        card.getChildren().addAll(valLbl, nameLbl);
        return card;
    }

    private VBox appointmentCard(Appointment apt) {
        VBox card = new VBox(4);
        card.setStyle(STYLE_STAT_CARD);
        Label header = new Label("Appointment #" + apt.getAppointmentID() + "  ·  " + apt.getStatus());
        header.setStyle(STYLE_LABEL + " -fx-font-weight: bold;");
        Label info = new Label(
                apt.getCustomer().getName() + "  |  " + apt.getService().getServiceName() +
                        "  |  RM" + apt.getService().getPrice() + "  |  " + apt.getDateTime()
        );
        info.setStyle(STYLE_SUBTITLE);
        card.getChildren().addAll(header, info);
        return card;
    }

    private ListView<String> appointmentListView(ArrayList<Appointment> list) {
        ListView<String> lv = new ListView<>();
        lv.setStyle("-fx-background-color: " + PANEL + ";");
        lv.setPrefHeight(180);
        lv.getItems().addAll(appointmentStrings(list));
        return lv;
    }

    private ArrayList<String> appointmentStrings(ArrayList<Appointment> list) {
        ArrayList<String> result = new ArrayList<>();
        for (Appointment a : list) {
            result.add(
                    "#" + a.getAppointmentID() + "  " + a.getStatus() +
                            "  |  " + a.getCustomer().getName() +
                            "  →  " + a.getBarber().getName() +
                            "  |  " + a.getDateTime()
            );
        }
        return result;
    }

    private RadioButton serviceRadio(String text, ToggleGroup group) {
        RadioButton rb = new RadioButton(text);
        rb.setToggleGroup(group);
        rb.setStyle("-fx-text-fill: " + TEXT + "; -fx-font-size: 13px;");
        return rb;
    }

    private Label noDataLabel(String msg) {
        Label l = new Label(msg);
        l.setStyle(STYLE_SUBTITLE + " -fx-font-size: 14px; -fx-padding: 20 0;");
        return l;
    }

    // =========================================================================
    public static void main(String[] args) {
        launch(args);
    }
}