import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        primaryStage.setTitle("IIUM University Management System");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setResizable(true);

        FileHandler.loadAllData();

        if (DataManager.getInstance().getStudentList().isEmpty() && 
            DataManager.getInstance().getCourseList().isEmpty()) {
            seedDemoData();
            FileHandler.saveAllData();
        }

        navigateToDashboard("DebugAdmin");
        primaryStage.show();
        }

    private void showLoginView() {
        LoginView loginView = new LoginView(this::navigateToDashboard);
        Scene loginScene = new Scene(loginView.getRoot(), 900, 600);
        loginScene.getStylesheets().add(getClass().getResource("styles.css") != null
                ? getClass().getResource("styles.css").toExternalForm() : "");
        primaryStage.setScene(loginScene);
    }

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

    private void seedDemoData() {
        DataManager dm = DataManager.getInstance();

        safeRun(() -> dm.addCourse("Intelligent Systems",         "BICS2303", 3, "Dr. Dini"));
        safeRun(() -> dm.addCourse("Digital Logic & Arch",        "BICS1306", 3, "Dr. Hafiz"));
        safeRun(() -> dm.addCourse("Object-Oriented Programming", "BICS2101", 3, "Dr. Amir"));

        safeRun(() -> dm.addInstructor("E001", "Dr. Dini",  "dini@iium.edu.my"));
        safeRun(() -> dm.addInstructor("E002", "Dr. Hafiz", "hafiz@iium.edu.my"));
        safeRun(() -> dm.addInstructor("E003", "Dr. Amir",  "amir@iium.edu.my"));

        safeRun(() -> dm.addStudent(20001, "Ahmad Atep",    "atep@live.iium.edu.my",   "BICS2303"));
        safeRun(() -> dm.addStudent(20002, "Siti Aisyah",   "aisyah@live.iium.edu.my", "BICS1306"));
        safeRun(() -> dm.addStudent(20003, "Muhammad Haziq","haziq@live.iium.edu.my",  "BICS2101"));

        safeRun(() -> dm.addClub("Robotics Club",   "Ahmad Atep",   "Tech Expo 2025", "2025-10-15", "Hall A",  150));
        safeRun(() -> dm.addClub("Chess Club",      "Siti Aisyah",  "Inter-Uni Open", "2025-11-02", "Hall B",   80));
        safeRun(() -> dm.addClub("Coding Society",  "Muhammad Haziq","Hackathon 2025", "2025-12-01", "Lab 3",  200));
    }

    private void safeRun(Runnable r) {
        try { r.run(); } catch (IllegalArgumentException ignored) {}
    }

    public static void main(String[] args) {
        launch(args);
    }
}
