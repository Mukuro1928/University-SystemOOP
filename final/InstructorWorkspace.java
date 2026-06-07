package universitymanagementsystem;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

/**
 * InstructorWorkspace.java — Instructor CRUD Panel
 * ==================================================
 * Team member: [assign to Backend/UI member]
 * Follows the exact same pattern as StudentWorkspace:
 *   ObservableList<InstructorRow> ← DataManager singleton
 *   TableView bound via PropertyValueFactory
 *   Add / Edit / Delete with try-catch → Alert on error
 */
public class InstructorWorkspace {

    private final ObservableList<InstructorRow> tableModel = FXCollections.observableArrayList();
    private TableView<InstructorRow> table;
    private TextField empIDField, nameField, emailField;
    private Label statusLabel;

    public Node build() {
        syncFromDataManager();
        VBox root = new VBox(0);
        root.getChildren().addAll(buildPanelHeader(), buildContentArea());
        return root;
    }

    // ------------------------------------------------------------------ //
    // Data sync
    // ------------------------------------------------------------------ //

    private void syncFromDataManager() {
        tableModel.clear();
        for (Instructor i : DataManager.getInstance().getInstructorList())
            tableModel.add(new InstructorRow(i));
    }

    // ------------------------------------------------------------------ //
    // Panel header
    // ------------------------------------------------------------------ //

    private HBox buildPanelHeader() {
        HBox h = new HBox(); h.setPadding(new Insets(0,0,20,0)); h.setAlignment(Pos.CENTER_LEFT);
        VBox g = new VBox(2);
        Label t = new Label("Instructor Management");
        t.setFont(Font.font("Georgia", FontWeight.BOLD, 24)); t.setTextFill(Color.web("#0d2144"));
        Label s = new Label("Add, edit, or remove instructor records.");
        s.setFont(Font.font("Verdana", 12)); s.setTextFill(Color.web("#6b7a99"));
        g.getChildren().addAll(t, s);
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label badge = new Label();
        badge.setStyle("-fx-background-color:#d4ffe8;-fx-text-fill:#1a7a4a;" +
                       "-fx-font-size:12px;-fx-font-weight:bold;-fx-padding:4 12;-fx-background-radius:20;");
        tableModel.addListener((ListChangeListener<InstructorRow>) c ->
            badge.setText(tableModel.size() + " instructor" + (tableModel.size()==1?"":"s")));
        badge.setText(tableModel.size() + " instructors");
        h.getChildren().addAll(g, sp, badge);
        return h;
    }

    // ------------------------------------------------------------------ //
    // Content area
    // ------------------------------------------------------------------ //

    @SuppressWarnings("unchecked")
    private HBox buildContentArea() {
        // Table columns
        TableColumn<InstructorRow, String> empCol = new TableColumn<>("Employee ID");
        empCol.setCellValueFactory(new PropertyValueFactory<>("employeeID")); empCol.setPrefWidth(120);
        TableColumn<InstructorRow, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name")); nameCol.setPrefWidth(200);
        TableColumn<InstructorRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email")); emailCol.setPrefWidth(250);

        table = new TableView<>(tableModel);
        table.getColumns().addAll(empCol, nameCol, emailCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No instructors registered yet."));
        table.setPrefHeight(420); table.setStyle("-fx-font-size:12px;");
        table.getSelectionModel().selectedItemProperty().addListener(
            (obs,old,sel) -> { if (sel!=null) populateForm(sel); });

        Label tl = new Label("Registered Instructors");
        tl.setFont(Font.font("Verdana",FontWeight.BOLD,13)); tl.setTextFill(Color.web("#2a3a5a"));
        VBox ts = new VBox(12, tl, table);
        ts.setStyle("-fx-background-color:#ffffff;-fx-background-radius:10;-fx-padding:20;" +
                    "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),10,0,0,3);");
        VBox.setVgrow(table, Priority.ALWAYS); HBox.setHgrow(ts, Priority.ALWAYS);

        // Form fields
        empIDField  = sf("Employee ID (e.g. E004)");
        nameField   = sf("Full Name");
        emailField  = sf("Email Address");
        statusLabel = new Label(); statusLabel.setWrapText(true);
        statusLabel.setFont(Font.font("Verdana",11)); statusLabel.setVisible(false);

        Button addBtn    = ab("➕  Add Instructor",    "#1a7a4a", "#23a060");
        Button editBtn   = ab("✏  Update Instructor",  "#1a4a9a", "#2a5298");
        Button deleteBtn = ab("🗑  Delete Instructor",  "#8a1a1a", "#c0392b");
        Button clearBtn  = ab("↺  Clear Form",          "#4a5568", "#6b7a99");

        addBtn.setOnAction(e    -> handleAdd());
        editBtn.setOnAction(e   -> handleEdit());
        deleteBtn.setOnAction(e -> handleDelete());
        clearBtn.setOnAction(e  -> clearForm());

        VBox form = new VBox(12,
            bold("Instructor Details"), new Separator(),
            fg("Employee ID", empIDField), fg("Full Name", nameField), fg("Email", emailField),
            statusLabel, addBtn, editBtn, deleteBtn, clearBtn,
            hint("Select a row to populate this form for editing.")
        );
        form.setPrefWidth(290); form.setMinWidth(260);
        form.setStyle("-fx-background-color:#ffffff;-fx-background-radius:10;-fx-padding:24;" +
                      "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),10,0,0,3);");

        HBox area = new HBox(20, ts, form); area.setFillHeight(true);
        return area;
    }

    // ------------------------------------------------------------------ //
    // CRUD handlers
    // ------------------------------------------------------------------ //

    private void handleAdd() {
        try {
            String empID = req(empIDField, "Employee ID");
            String name  = req(nameField,  "Name");
            String email = emailField.getText().trim();
            DataManager.getInstance().addInstructor(empID, name, email);
            syncFromDataManager(); clearForm();
            FileHandler.saveAllData();
            showStatus("✔  Instructor added successfully.", true);
        } catch (IllegalArgumentException ex) { ea("Add Instructor Failed", ex.getMessage()); }
    }

    private void handleEdit() {
        try {
            String empID    = req(empIDField, "Employee ID");
            String newName  = req(nameField,  "Name");
            String newEmail = emailField.getText().trim();
            boolean ok = DataManager.getInstance().editInstructor(empID, newName, newEmail);
            if (!ok) throw new IllegalArgumentException("No instructor found with ID: " + empID);
            syncFromDataManager(); clearForm();
            FileHandler.saveAllData();
            showStatus("✔  Instructor updated.", true);
        } catch (IllegalArgumentException ex) { ea("Update Failed", ex.getMessage()); }
    }

    private void handleDelete() {
        try {
            String empID = req(empIDField, "Employee ID");
            Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete instructor '" + empID + "'? This cannot be undone.");
            c.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    boolean ok = DataManager.getInstance().deleteInstructor(empID);
                    if (!ok) { ea("Delete Failed", "Instructor not found: " + empID); return; }
                    syncFromDataManager(); clearForm();
                    FileHandler.saveAllData();
                    showStatus("✔  Instructor deleted.", true);
                }
            });
        } catch (IllegalArgumentException ex) { ea("Delete Failed", ex.getMessage()); }
    }

    // ------------------------------------------------------------------ //
    // Form helpers
    // ------------------------------------------------------------------ //

    private void populateForm(InstructorRow r) {
        empIDField.setText(r.getEmployeeID()); nameField.setText(r.getName());
        empIDField.setEditable(false);
        empIDField.setStyle(empIDField.getStyle() + "-fx-opacity:0.7;-fx-cursor:not-allowed;");
        emailField.setText(r.getEmail()); statusLabel.setVisible(false);
    }

    private void clearForm() {
        empIDField.clear(); nameField.clear(); emailField.clear();
        empIDField.setEditable(true);
        empIDField.setStyle(empIDField.getStyle() + "-fx-opacity:1;-fx-cursor:allowed;");
        statusLabel.setVisible(false); table.getSelectionModel().clearSelection();
    }

    private void showStatus(String m, boolean ok) {
        statusLabel.setText(m);
        statusLabel.setTextFill(ok ? Color.web("#1a7a4a") : Color.web("#c0392b"));
        statusLabel.setVisible(true);
    }

    private String req(TextField f, String l) {
        String v = f.getText().trim();
        if (v.isEmpty()) throw new IllegalArgumentException(l + " cannot be empty.");
        return v;
    }

    private void ea(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }

    // ------------------------------------------------------------------ //
    // Layout helpers
    // ------------------------------------------------------------------ //

    private TextField sf(String p) {
        TextField tf = new TextField(); tf.setPromptText(p); tf.setPrefHeight(36);
        tf.setStyle("-fx-background-color:#f7f9fc;-fx-border-color:#c8d3e8;" +
                    "-fx-border-radius:5;-fx-background-radius:5;-fx-font-size:12px;-fx-padding:6 10;");
        return tf;
    }

    private Button ab(String t, String base, String hover) {
        Button btn = new Button(t); btn.setPrefWidth(Double.MAX_VALUE); btn.setPrefHeight(38);
        btn.setFont(Font.font("Verdana",FontWeight.BOLD,12));
        String s = "-fx-background-color:"+base+";-fx-text-fill:white;-fx-background-radius:6;-fx-cursor:hand;";
        btn.setStyle(s);
        btn.setOnMouseEntered(e->btn.setStyle(s.replace(base,hover)));
        btn.setOnMouseExited(e->btn.setStyle(s));
        return btn;
    }

    private VBox fg(String l, TextField f) {
        Label lb = new Label(l); lb.setFont(Font.font("Verdana",11)); lb.setTextFill(Color.web("#4a5568"));
        return new VBox(4, lb, f);
    }

    private Label bold(String t) {
        Label l = new Label(t); l.setFont(Font.font("Verdana",FontWeight.BOLD,13)); l.setTextFill(Color.web("#2a3a5a")); return l;
    }

    private Label hint(String t) {
        Label l = new Label("ℹ  " + t); l.setFont(Font.font("Verdana",10)); l.setTextFill(Color.web("#8899bb")); l.setWrapText(true); return l;
    }

    // ------------------------------------------------------------------ //
    // Row model — JavaFX property wrappers for cellValueFactory binding
    // ------------------------------------------------------------------ //

    public static class InstructorRow {
        private final SimpleStringProperty employeeID, name, email;

        public InstructorRow(Instructor i) {
            this.employeeID = new SimpleStringProperty(i.getEmployeeID());
            this.name       = new SimpleStringProperty(i.getName());
            this.email      = new SimpleStringProperty(i.getEmail());
        }

        public String getEmployeeID() { return employeeID.get(); }
        public String getName()       { return name.get(); }
        public String getEmail()      { return email.get(); }

        public StringProperty employeeIDProperty() { return employeeID; }
        public StringProperty nameProperty()       { return name; }
        public StringProperty emailProperty()      { return email; }
    }
}
