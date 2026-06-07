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
 * Developed by: Aiman
 * StudentWorkspace.java — Student Management Panel
 * ================================================
 * Provides a complete CRUD (Create, Read, Update, Delete) interface
 * for managing student records within the University Management System.
 *
 * Features:
 *  • Add, update, and delete student records.
 *  • Display registered students in a TableView.
 *  • Assign students to courses using course codes.
 *  • Auto-populate form fields when a table row is selected.
 *  • Persist data changes through FileHandler.
 *
 * Architecture:
 *  • Uses ObservableList for automatic UI synchronization.
 *  • Delegates all data operations to the DataManager singleton.
 *  • Employs JavaFX Property wrappers for TableView binding.
 *  • Provides input validation and user-friendly error handling
 *    through JavaFX Alert dialogs.
 *
 * Design:
 *  • Serves as the reference implementation for other workspace
 *    modules such as InstructorWorkspace, CourseWorkspace,
 *    and ClubWorkspace.
 */

public class StudentWorkspace {
    private final ObservableList<StudentRow> tableModel = FXCollections.observableArrayList();

    private TableView<StudentRow>  table;
    private TextField              idField;
    private TextField              nameField;
    private TextField              emailField;
    private TextField              courseCodeField;
    private Label                  statusLabel;

    public Node build() {
        syncFromDataManager();

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: transparent;");

        root.getChildren().addAll(
            buildPanelHeader(),
            buildContentArea()
        );

        return root;
    }

    private void syncFromDataManager() {
        tableModel.clear();
        for (Student s : DataManager.getInstance().getStudentList()) {
            tableModel.add(new StudentRow(s));
        }
    }

    private HBox buildPanelHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(0, 0, 20, 0));
        header.setAlignment(Pos.BOTTOM_LEFT);

        VBox titleGroup = new VBox(2);
        Label title = new Label("Student Management");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#0d2144"));

        Label sub = new Label("Add, edit, or remove student records from the system.");
        sub.setFont(Font.font("Verdana", 12));
        sub.setTextFill(Color.web("#6b7a99"));

        titleGroup.getChildren().addAll(title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label countBadge = new Label();
        countBadge.setStyle(
            "-fx-background-color: #dce8ff;" +
            "-fx-text-fill: #2a5298;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 4 12;" +
            "-fx-background-radius: 20;"
        );

        tableModel.addListener((javafx.collections.ListChangeListener<StudentRow>) c ->
            countBadge.setText(tableModel.size() + " student" + (tableModel.size() == 1 ? "" : "s"))
        );
        countBadge.setText(tableModel.size() + " students");

        header.getChildren().addAll(titleGroup, spacer, countBadge);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private HBox buildContentArea() {
        HBox area = new HBox(20);
        area.setFillHeight(true);

        Node tableSection = buildTableSection();
        Node formSection  = buildFormSection();

        HBox.setHgrow(tableSection, Priority.ALWAYS);
        area.getChildren().addAll(tableSection, formSection);
        return area;
    }

    @SuppressWarnings("unchecked")
    private VBox buildTableSection() {
        VBox section = new VBox(12);
        section.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 3);"
        );

        Label sectionTitle = new Label("Registered Students");
        sectionTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        sectionTitle.setTextFill(Color.web("#2a3a5a"));

        TableColumn<StudentRow, Integer> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        idCol.setPrefWidth(100);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<StudentRow, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);

        TableColumn<StudentRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(220);

        TableColumn<StudentRow, String> courseCol = new TableColumn<>("Enrolled Course");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("enrolledCourseName"));
        courseCol.setPrefWidth(180);

        table = new TableView<>(tableModel);
        table.getColumns().addAll(idCol, nameCol, emailCol, courseCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No students registered yet."));
        table.setPrefHeight(420);
        table.setStyle("-fx-font-size: 12px;");

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, selected) -> {
                if (selected != null) populateFormFromRow(selected);
            }
        );

        VBox.setVgrow(table, Priority.ALWAYS);
        section.getChildren().addAll(sectionTitle, table);
        return section;
    }

    private VBox buildFormSection() {
        VBox form = new VBox(14);
        form.setPrefWidth(290);
        form.setMinWidth(260);
        form.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 24;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 3);"
        );

        Label formTitle = new Label("Student Details");
        formTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        formTitle.setTextFill(Color.web("#2a3a5a"));

        Separator sep = new Separator();
        sep.setPadding(new Insets(0, 0, 4, 0));

        idField         = buildTextField("Student ID (numbers only)");
        nameField       = buildTextField("Full Name");
        emailField      = buildTextField("Email Address");
        courseCodeField = buildTextField("Course Code (e.g. BICS2303)");

        statusLabel = new Label();
        statusLabel.setWrapText(true);
        statusLabel.setFont(Font.font("Verdana", 11));
        statusLabel.setVisible(false);

        Button addBtn    = buildActionButton("➕  Add Student",    "#1a7a4a", "#23a060");
        Button editBtn   = buildActionButton("✏  Update Student",  "#1a4a9a", "#2a5298");
        Button deleteBtn = buildActionButton("🗑  Delete Student",  "#8a1a1a", "#c0392b");
        Button clearBtn  = buildActionButton("↺  Clear Form",      "#4a5568", "#6b7a99");

        addBtn.setOnAction(e    -> handleAdd());
        editBtn.setOnAction(e   -> handleEdit());
        deleteBtn.setOnAction(e -> handleDelete());
        clearBtn.setOnAction(e  -> clearForm());

        Label hint = new Label(
            "ℹ  Select a row in the table to populate this form, then click Update or Delete."
        );
        hint.setFont(Font.font("Verdana", 10));
        hint.setTextFill(Color.web("#8899bb"));
        hint.setWrapText(true);

        form.getChildren().addAll(
            formTitle, sep,
            fieldGroup("Student ID", idField),
            fieldGroup("Full Name", nameField),
            fieldGroup("Email", emailField),
            fieldGroup("Enrolled Course Code", courseCodeField),
            statusLabel,
            addBtn, editBtn, deleteBtn, clearBtn,
            hint
        );
        return form;
    }

    private VBox fieldGroup(String label, TextField field) {
        Label lbl = new Label(label);
        lbl.setFont(Font.font("Verdana", FontWeight.NORMAL, 11));
        lbl.setTextFill(Color.web("#4a5568"));
        VBox group = new VBox(4, lbl, field);
        return group;
    }

    private TextField buildTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(36);
        tf.setStyle(
            "-fx-background-color: #f7f9fc;" +
            "-fx-border-color: #c8d3e8;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 6 10;"
        );
        return tf;
    }

    private Button buildActionButton(String text, String baseColor, String hoverColor) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        String base = "-fx-background-color: " + baseColor + "; -fx-text-fill: white;" +
                      "-fx-background-radius: 6; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(base.replace(baseColor, hoverColor)));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    private void handleAdd() {
        try {
            int    id         = parseID();
            String name       = requireField(nameField, "Name");
            String email      = emailField.getText().trim();
            String courseCode = courseCodeField.getText().trim();

            DataManager.getInstance().addStudent(id, name, email, courseCode);
            syncFromDataManager();
            clearForm();
            FileHandler.saveAllData();
            showStatus("✔  Student added successfully.", true);

        } catch (IllegalArgumentException ex) {
            showErrorAlert("Add Student Failed", ex.getMessage());
        }
    }

    private void handleEdit() {
        try {
            int id = parseID();

            if (DataManager.getInstance().findStudentByID(id) == null) {
                throw new IllegalArgumentException("No student found with ID " + id + ".");
            }

            String newName   = requireField(nameField, "Name");
            String newEmail  = emailField.getText().trim();
            String newCode   = courseCodeField.getText().trim();

            boolean updated = DataManager.getInstance().editStudent(id, newName, newEmail, newCode);
            if (!updated) throw new IllegalArgumentException("Update failed — student not found.");

            syncFromDataManager();
            clearForm();
            FileHandler.saveAllData();
            showStatus("✔  Student record updated.", true);

        } catch (IllegalArgumentException ex) {
            showErrorAlert("Update Student Failed", ex.getMessage());
        }
    }

    private void handleDelete() {
        try {
            int id = parseID();

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete student with ID " + id + "?");
            confirm.setContentText("This action cannot be undone.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean deleted = DataManager.getInstance().deleteStudent(id);
                    if (!deleted) {
                        showErrorAlert("Delete Failed", "No student found with ID " + id + ".");
                        return;
                    }
                    syncFromDataManager();
                    clearForm();
                    FileHandler.saveAllData();
                    showStatus("✔  Student deleted.", true);
                }
            });

        } catch (IllegalArgumentException ex) {
            showErrorAlert("Delete Student Failed", ex.getMessage());
        }
    }

    private void clearForm() {
        idField.clear();
        idField.setEditable(true);
        idField.setStyle(idField.getStyle() + "-fx-opacity:1;-fx-cursor:allowed;");
        nameField.clear();
        emailField.clear();
        courseCodeField.clear();
        statusLabel.setVisible(false);
        table.getSelectionModel().clearSelection();
    }

    private void populateFormFromRow(StudentRow row) {
        idField.setText(String.valueOf(row.getStudentID()));
        idField.setEditable(false);
        idField.setStyle(idField.getStyle() + "-fx-opacity:0.7;-fx-cursor:not-allowed;");
        nameField.setText(row.getName());
        emailField.setText(row.getEmail());
        courseCodeField.setText(row.getEnrolledCourseCode());
        statusLabel.setVisible(false);
    }

    private void showStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setTextFill(success ? Color.web("#1a7a4a") : Color.web("#c0392b"));
        statusLabel.setVisible(true);
    }

    private int parseID() {
        String raw = idField.getText().trim();
        if (raw.isEmpty()) throw new IllegalArgumentException("Student ID cannot be empty.");
        try {
            int val = Integer.parseInt(raw);
            if (val <= 0) throw new IllegalArgumentException("Student ID must be a positive number.");
            return val;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Student ID must be numeric (e.g. 20001).");
        }
    }

    private String requireField(TextField field, String fieldName) {
        String val = field.getText().trim();
        if (val.isEmpty()) throw new IllegalArgumentException(fieldName + " cannot be empty.");
        return val;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class StudentRow {

        private final SimpleIntegerProperty studentID;
        private final SimpleStringProperty  name;
        private final SimpleStringProperty  email;
        private final SimpleStringProperty  enrolledCourseName;
        private final String                enrolledCourseCode;

        public StudentRow(Student student) {
            this.studentID          = new SimpleIntegerProperty(student.getStudentID());
            this.name               = new SimpleStringProperty(student.getName());
            this.email              = new SimpleStringProperty(student.getEmail());

            Course c = student.getEnrolledCourse();
            this.enrolledCourseName = new SimpleStringProperty(c != null ? c.getCourseName() : "Not enrolled");
            this.enrolledCourseCode = c != null ? c.getCourseCode() : "";
        }

        public int    getStudentID()          { return studentID.get(); }
        public String getName()               { return name.get(); }
        public String getEmail()              { return email.get(); }
        public String getEnrolledCourseName() { return enrolledCourseName.get(); }
        public String getEnrolledCourseCode() { return enrolledCourseCode; }

        public IntegerProperty  studentIDProperty()          { return studentID; }
        public StringProperty   nameProperty()               { return name; }
        public StringProperty   emailProperty()              { return email; }
        public StringProperty   enrolledCourseNameProperty() { return enrolledCourseName; }
    }
}
