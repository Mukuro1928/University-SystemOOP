/**
 * Developed by: AHMAD MUKHLIS BIN ZAKARIAH
 * DashboardView.java — Main University Management Dashboard (JavaFX)
 * ===================================================================
 * Provides the main UI shell and navigation system for the application.
 *
 * Features:
 * • Sidebar navigation for Dashboard, Students, Instructors, Courses, and Clubs.
 * • Dynamic workspace loading using a Supplier-based registry system.
 * • Top bar with breadcrumb navigation, user info, and logout confirmation.
 * • Overview dashboard showing real-time system statistics from DataManager.
 * • Modular design where each feature module is loaded independently.
 * • Interactive UI styling with hover, active state, and transitions.
 *
 * Architecture:
 * • Built using JavaFX BorderPane layout (Sidebar | TopBar | Center Workspace).
 * • Uses NavItem enum to define navigation structure in a centralized way.
 * • Workspace registry maps string keys to lazily created UI components.
 * • Integrates with DataManager singleton for centralized data access.
 * • Each module (StudentWorkspace, CourseWorkspace, etc.) is decoupled and injected dynamically.
 *
 * Behavior:
 * • Clicking navigation buttons swaps the center workspace dynamically.
 * • Maintains active navigation state and updates breadcrumb label.
 * • Logout triggers external callback via Runnable confirmation flow.
 *
 * Note:
 * • This class only manages UI structure and navigation flow.
 * • All CRUD operations are handled in individual workspace classes.
 */


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardView {
    private enum NavItem {
        DASHBOARD ("🏠", "Dashboard",   "dashboard"),
        STUDENTS  ("👤", "Students",    "students"),
        INSTRUCTORS("🎓","Instructors", "instructors"),
        COURSES   ("📚", "Courses",     "courses"),
        CLUBS     ("🏆", "Clubs",       "clubs");

        final String icon;
        final String label;
        final String key;

        NavItem(String icon, String label, String key) {
            this.icon = icon; this.label = label; this.key = key;
        }
    }

    private final BorderPane root;
    private final String loggedInUser;
    private final Runnable onLogout;

    private final Map<String, java.util.function.Supplier<Node>> workspaceRegistry;
    private Button activeNavButton;
    private Label breadcrumbLabel;
    private StackPane centerWrapper;

    public DashboardView(String loggedInUser, Runnable onLogout) {
        this.loggedInUser = loggedInUser;
        this.onLogout     = onLogout;
        this.workspaceRegistry = buildWorkspaceRegistry();
        this.root = buildLayout();
    }

    public Pane getRoot() { return root; }

    private Map<String, java.util.function.Supplier<Node>> buildWorkspaceRegistry() {
        Map<String, java.util.function.Supplier<Node>> reg = new LinkedHashMap<>();

        reg.put("dashboard",   this::buildOverviewPanel);
        reg.put("students",    () -> new StudentWorkspace().build());
        reg.put("instructors", () -> new InstructorWorkspace().build());
        reg.put("courses",     () -> new CourseWorkspace().build());
        reg.put("clubs",       () -> new ClubWorkspace().build());

        return reg;
    }

    private BorderPane buildLayout() {
        BorderPane bp = new BorderPane();
        bp.setStyle("-fx-background-color: #eef1f7;");

        breadcrumbLabel = new Label("Dashboard");
        centerWrapper   = new StackPane();

        bp.setLeft(buildSidebar());
        bp.setTop(buildTopBar());
        bp.setCenter(buildCenter());

        navigateTo(NavItem.DASHBOARD.key, NavItem.DASHBOARD.label);

        return bp;
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(0, 0, 20, 0));
        sidebar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #0d2144, #162e5a);" +
            "-fx-border-color: #2a5298; -fx-border-width: 0 1 0 0;"
        );

        sidebar.getChildren().add(buildSidebarHeader());
        sidebar.getChildren().add(sidebarSectionLabel("NAVIGATION"));

        for (NavItem item : NavItem.values()) {
            Button btn = buildNavButton(item);
            sidebar.getChildren().add(btn);

            if (item == NavItem.DASHBOARD) {
                activeNavButton = btn;
                applyActiveStyle(btn);
            }
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);

        Label version = new Label("UMS v2.0  ·  IIUM");
        version.setFont(Font.font("Verdana", 9));
        version.setTextFill(Color.web("#4a6a9a"));
        version.setPadding(new Insets(0, 0, 8, 20));
        sidebar.getChildren().add(version);

        return sidebar;
    }

    private VBox buildSidebarHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(28, 20, 20, 20));
        header.setStyle(
            "-fx-background-color: rgba(0,0,0,0.2);" +
            "-fx-border-color: #2a5298; -fx-border-width: 0 0 1 0;"
        );

        Label title = new Label("UMS");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#f0c040"));

        Label subtitle = new Label("University Management");
        subtitle.setFont(Font.font("Verdana", 10));
        subtitle.setTextFill(Color.web("#6a90c0"));

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private Label sidebarSectionLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Verdana", FontWeight.BOLD, 9));
        l.setTextFill(Color.web("#3a5a8a"));
        l.setPadding(new Insets(16, 0, 4, 22));
        return l;
    }

    private Button buildNavButton(NavItem item) {
        Button btn = new Button(item.icon + "   " + item.label);
        btn.setPrefWidth(220);
        btn.setPrefHeight(44);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 0, 0, 22));
        btn.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        applyInactiveStyle(btn);

        btn.setOnMouseEntered(e -> {
            if (btn != activeNavButton) applyHoverStyle(btn);
        });
        btn.setOnMouseExited(e -> {
            if (btn != activeNavButton) applyInactiveStyle(btn);
        });
        btn.setOnAction(e -> {
            applyInactiveStyle(activeNavButton);
            activeNavButton = btn;
            applyActiveStyle(btn);
            navigateTo(item.key, item.label);
        });

        return btn;
    }

    private void applyActiveStyle(Button btn) {
        btn.setStyle(
            "-fx-background-color: #2a5298;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: transparent transparent transparent #f0c040;" +
            "-fx-border-width: 0 0 0 3;" +
            "-fx-cursor: default;"
        );
    }

    private void applyHoverStyle(Button btn) {
        btn.setStyle(
            "-fx-background-color: rgba(42,82,152,0.4);" +
            "-fx-text-fill: #d0e4ff;" +
            "-fx-cursor: hand;"
        );
    }

    private void applyInactiveStyle(Button btn) {
        if (btn == null) return;
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #8aabcf;" +
            "-fx-cursor: hand;"
        );
    }

    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setPadding(new Insets(14, 24, 14, 24));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-border-color: #d8e2f0; -fx-border-width: 0 0 1 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );

        Label prefix = new Label("UMS  /  ");
        prefix.setFont(Font.font("Verdana", 12));
        prefix.setTextFill(Color.web("#8899bb"));

        breadcrumbLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        breadcrumbLabel.setTextFill(Color.web("#1a3a6e"));

        HBox breadcrumb = new HBox(2, prefix, breadcrumbLabel);
        breadcrumb.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("👤  " + loggedInUser);
        userLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 12));
        userLabel.setTextFill(Color.web("#1a3a6e"));
        userLabel.setPadding(new Insets(0, 16, 0, 0));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #c0392b;" +
            "-fx-font-size: 12px;" +
            "-fx-border-color: #c0392b;" +
            "-fx-border-radius: 4;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 4 12;"
        );
        logoutBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setHeaderText("Are you sure you want to logout?");
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.OK) onLogout.run();
            });
        });

        bar.getChildren().addAll(breadcrumb, spacer, userLabel, logoutBtn);
        return bar;
    }

    private StackPane buildCenter() {
        centerWrapper.setPadding(new Insets(28));
        centerWrapper.setStyle("-fx-background-color: #eef1f7;");
        return centerWrapper;
    }

    private void navigateTo(String key, String label) {
        breadcrumbLabel.setText(label);
        centerWrapper.getChildren().clear();

        java.util.function.Supplier<Node> builder = workspaceRegistry.get(key);
        if (builder != null) {
            centerWrapper.getChildren().add(builder.get());
        } else {
            centerWrapper.getChildren().add(buildPlaceholder(label));
        }
    }

    private Node buildOverviewPanel() {
        VBox panel = new VBox(24);
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPadding(new Insets(8));

        Label heading = new Label("System Overview");
        heading.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        heading.setTextFill(Color.web("#0d2144"));

        Label sub = new Label("Quick snapshot of all registered records.");
        sub.setFont(Font.font("Verdana", 13));
        sub.setTextFill(Color.web("#6b7a99"));

        DataManager dm = DataManager.getInstance();
        HBox cards = new HBox(16,
            buildStatCard("👤 Students",    String.valueOf(dm.getStudentList().size()),    "#2a5298"),
            buildStatCard("🎓 Instructors", String.valueOf(dm.getInstructorList().size()), "#1a7a4a"),
            buildStatCard("📚 Courses",     String.valueOf(dm.getCourseList().size()),     "#7a3a00"),
            buildStatCard("🏆 Clubs",       String.valueOf(dm.getClubList().size()),       "#6a1a6a")
        );

        Label tip = new Label("ℹ  Use the sidebar to navigate to any management panel.");
        tip.setFont(Font.font("Verdana", 11));
        tip.setTextFill(Color.web("#8899bb"));
        tip.setStyle(
            "-fx-background-color: #dce8ff;" +
            "-fx-padding: 10 16;" +
            "-fx-background-radius: 6;"
        );

        panel.getChildren().addAll(heading, sub, cards, tip);
        return panel;
    }

    private VBox buildStatCard(String title, String count, String accentColor) {
        VBox card = new VBox(8);
        card.setPrefWidth(170);
        card.setPrefHeight(110);
        card.setPadding(new Insets(20, 20, 20, 20));
        card.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + accentColor + ";" +
            "-fx-border-width: 0 0 0 4;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
        titleLbl.setTextFill(Color.web("#6b7a99"));

        Label countLbl = new Label(count);
        countLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        countLbl.setTextFill(Color.web(accentColor));

        card.getChildren().addAll(titleLbl, countLbl);
        return card;
    }

    private Node buildPlaceholder(String panelName) {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        Label lbl = new Label("[ " + panelName + " workspace — implementation pending ]");
        lbl.setFont(Font.font("Verdana", 14));
        lbl.setTextFill(Color.web("#8899bb"));
        box.getChildren().add(lbl);
        return box;
    }
}
