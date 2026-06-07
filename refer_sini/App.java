import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App.java — University Management System Entry Point
 * =====================================================
 * Primary JavaFX Application class. Owns the single primary Stage and acts
 * as the scene-switching router between LoginView and DashboardView.
 *
 * Architecture note:
 *   All UI state lives inside the view classes. App only manages Stage
 *   lifecycle and wires the "on success" callback from LoginView so it can
 *   swap scenes without the views knowing about each other.
 */
public class App extends Application {

    /** The one window shared by all scenes throughout the application. */
    private Stage primaryStage;

    // ---------------------------------------------------------------
    // JavaFX lifecycle
    // ---------------------------------------------------------------

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Window chrome
        primaryStage.setTitle("IIUM University Management System");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setResizable(true);

        // Seed some demo data so the UI is not completely empty on first run
        seedDemoData();

        // Show the login screen first
        showLoginView();

        primaryStage.show();
    }

    // ---------------------------------------------------------------
    // Scene routing (called by view callbacks)
    // ---------------------------------------------------------------

    /**
     * Builds and displays the Login scene.
     * Passes itself as a callback so LoginView can call navigateToDashboard()
     * without a direct reference to App.
     */
    private void showLoginView() {
        LoginView loginView = new LoginView(this::navigateToDashboard);
        Scene loginScene = new Scene(loginView.getRoot(), 900, 600);
        loginScene.getStylesheets().add(getClass().getResource("styles.css") != null
                ? getClass().getResource("styles.css").toExternalForm() : "");
        primaryStage.setScene(loginScene);
    }

    /**
     * Called by LoginView on successful authentication.
     * Replaces the Login scene with the full Dashboard scene.
     *
     * @param loggedInUser The username string returned by the login form,
     *                     passed through to the dashboard header.
     */
    public void navigateToDashboard(String loggedInUser) {
        DashboardView dashboardView = new DashboardView(loggedInUser, this::showLoginView);
        Scene dashScene = new Scene(dashboardView.getRoot(), 1200, 720);
        dashScene.getStylesheets().add(getClass().getResource("styles.css") != null
                ? getClass().getResource("styles.css").toExternalForm() : "");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(720);
        primaryStage.centerOnScreen();
        primaryStage.setScene(dashScene);
    }

    // ---------------------------------------------------------------
    // Demo data bootstrap
    // ---------------------------------------------------------------

    /**
     * Populates DataManager with a handful of realistic seed records so
     * teammates can see working TableViews immediately without typing input.
     * Safe to call multiple times — each addX() throws if a duplicate ID is
     * detected, so we guard with try-catch here.
     */
    private void seedDemoData() {
        DataManager dm = DataManager.getInstance();

        // Courses
        safeRun(() -> dm.addCourse("Intelligent Systems",         "BICS2303", 3, "Dr. Dini"));
        safeRun(() -> dm.addCourse("Digital Logic & Arch",        "BICS1306", 3, "Dr. Hafiz"));
        safeRun(() -> dm.addCourse("Object-Oriented Programming", "BICS2101", 3, "Dr. Amir"));

        // Instructors
        safeRun(() -> dm.addInstructor("E001", "Dr. Dini",  "dini@iium.edu.my"));
        safeRun(() -> dm.addInstructor("E002", "Dr. Hafiz", "hafiz@iium.edu.my"));
        safeRun(() -> dm.addInstructor("E003", "Dr. Amir",  "amir@iium.edu.my"));

        // Students
        safeRun(() -> dm.addStudent(20001, "Ahmad Atep",    "atep@live.iium.edu.my",   "BICS2303"));
        safeRun(() -> dm.addStudent(20002, "Siti Aisyah",   "aisyah@live.iium.edu.my", "BICS1306"));
        safeRun(() -> dm.addStudent(20003, "Muhammad Haziq","haziq@live.iium.edu.my",  "BICS2101"));

        // Clubs
        safeRun(() -> dm.addClub("Robotics Club",   "Ahmad Atep",   "Tech Expo 2025", "2025-10-15", "Hall A",  150));
        safeRun(() -> dm.addClub("Chess Club",      "Siti Aisyah",  "Inter-Uni Open", "2025-11-02", "Hall B",   80));
        safeRun(() -> dm.addClub("Coding Society",  "Muhammad Haziq","Hackathon 2025", "2025-12-01", "Lab 3",  200));
    }

    /** Swallows expected IllegalArgumentException from duplicate seed inserts. */
    private void safeRun(Runnable r) {
        try { r.run(); } catch (IllegalArgumentException ignored) {}
    }

    // ---------------------------------------------------------------
    // Main
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        launch(args);
    }
}
