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
 * CourseWorkspace.java — Course CRUD Panel
 * ==========================================
 * Team member: [assign to Backend/UI member]
 * Fields: courseName, courseCode, credit (int), instructorName
 * DataManager calls: addCourse(), deleteCourse()
 *
 * NOTE: DataManager does not expose an editCourse() method in the current
 * backend. The "Edit" strategy here is: delete the old code, re-add with
 * corrections. If you add editCourse() to DataManager, wire it to the
 * Update button instead.
 */
public class CourseWorkspace {

    private final ObservableList<CourseRow> tableModel = FXCollections.observableArrayList();
    private TableView<CourseRow> table;
    private TextField nameField, codeField, creditField, instructorField;
    private Label statusLabel;

    public Node build() {
        syncFromDataManager();
        VBox root = new VBox(0);
        root.getChildren().addAll(buildPanelHeader(), buildContentArea());
        return root;
    }

    private void syncFromDataManager() {
        tableModel.clear();
        for (Course c : DataManager.getInstance().getCourseList())
            tableModel.add(new CourseRow(c));
    }

    private HBox buildPanelHeader() {
        HBox h = new HBox(); h.setPadding(new Insets(0,0,20,0)); h.setAlignment(Pos.CENTER_LEFT);
        VBox g = new VBox(2);
        Label t = new Label("Course Management");
        t.setFont(Font.font("Georgia",FontWeight.BOLD,24)); t.setTextFill(Color.web("#0d2144"));
        Label s = new Label("Register and manage academic courses.");
        s.setFont(Font.font("Verdana",12)); s.setTextFill(Color.web("#6b7a99"));
        g.getChildren().addAll(t, s);
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label badge = new Label();
        badge.setStyle("-fx-background-color:#fff3dc;-fx-text-fill:#7a4a00;" +
                       "-fx-font-size:12px;-fx-font-weight:bold;-fx-padding:4 12;-fx-background-radius:20;");
        tableModel.addListener((ListChangeListener<CourseRow>) c ->
            badge.setText(tableModel.size() + " course" + (tableModel.size()==1?"":"s")));
        badge.setText(tableModel.size() + " courses");
        h.getChildren().addAll(g, sp, badge);
        return h;
    }

    @SuppressWarnings("unchecked")
    private HBox buildContentArea() {
        TableColumn<CourseRow,String>  codeCol  = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));  codeCol.setPrefWidth(120);
        TableColumn<CourseRow,String>  nameCol  = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));  nameCol.setPrefWidth(220);
        TableColumn<CourseRow,Integer> creditCol = new TableColumn<>("Credits");
        creditCol.setCellValueFactory(new PropertyValueFactory<>("credit")); creditCol.setPrefWidth(80);
        creditCol.setStyle("-fx-alignment:CENTER;");
        TableColumn<CourseRow,String>  instrCol = new TableColumn<>("Instructor");
        instrCol.setCellValueFactory(new PropertyValueFactory<>("instructor")); instrCol.setPrefWidth(180);

        table = new TableView<>(tableModel);
        table.getColumns().addAll(codeCol, nameCol, creditCol, instrCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No courses registered yet."));
        table.setPrefHeight(420); table.setStyle("-fx-font-size:12px;");
        table.getSelectionModel().selectedItemProperty().addListener(
            (obs,old,sel) -> { if (sel!=null) populateForm(sel); });

        Label tl = new Label("Registered Courses");
        tl.setFont(Font.font("Verdana",FontWeight.BOLD,13)); tl.setTextFill(Color.web("#2a3a5a"));
        VBox ts = new VBox(12, tl, table);
        ts.setStyle("-fx-background-color:#ffffff;-fx-background-radius:10;-fx-padding:20;" +
                    "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),10,0,0,3);");
        VBox.setVgrow(table, Priority.ALWAYS); HBox.setHgrow(ts, Priority.ALWAYS);

        nameField       = sf("Course Name");
        codeField       = sf("Course Code (e.g. BICS2303)");
        creditField     = sf("Credit Hours (number)");
        instructorField = sf("Instructor Name");
        statusLabel     = new Label(); statusLabel.setWrapText(true);
        statusLabel.setFont(Font.font("Verdana",11)); statusLabel.setVisible(false);

        Button addBtn    = ab("➕  Add Course",    "#7a4a00", "#b06a10");
        Button deleteBtn = ab("🗑  Delete Course",  "#8a1a1a", "#c0392b");
        Button clearBtn  = ab("↺  Clear Form",      "#4a5568", "#6b7a99");

        addBtn.setOnAction(e    -> handleAdd());
        deleteBtn.setOnAction(e -> handleDelete());
        clearBtn.setOnAction(e  -> clearForm());

        Label note = new Label("ℹ  To edit a course: delete it and re-add. " +
                               "Add editCourse() to DataManager for a dedicated update flow.");
        note.setFont(Font.font("Verdana",10)); note.setTextFill(Color.web("#8899bb")); note.setWrapText(true);

        VBox form = new VBox(12,
            bold("Course Details"), new Separator(),
            fg("Course Name",    nameField),
            fg("Course Code",    codeField),
            fg("Credit Hours",   creditField),
            fg("Instructor",     instructorField),
            statusLabel, addBtn, deleteBtn, clearBtn, note
        );
        form.setPrefWidth(290); form.setMinWidth(260);
        form.setStyle("-fx-background-color:#ffffff;-fx-background-radius:10;-fx-padding:24;" +
                      "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),10,0,0,3);");

        HBox area = new HBox(20, ts, form); area.setFillHeight(true);
        return area;
    }

    private void handleAdd() {
        try {
            String name   = req(nameField,       "Course Name");
            String code   = req(codeField,        "Course Code");
            int    credit = parseCredit();
            String instr  = req(instructorField,  "Instructor Name");
            DataManager.getInstance().addCourse(name, code, credit, instr);
            syncFromDataManager(); clearForm();
            showStatus("✔  Course added successfully.", true);
        } catch (IllegalArgumentException ex) { ea("Add Course Failed", ex.getMessage()); }
    }

    private void handleDelete() {
        try {
            String code = req(codeField, "Course Code");
            Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete course '" + code + "'? Enrolled students will be un-enrolled.");
            c.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    boolean ok = DataManager.getInstance().deleteCourse(code);
                    if (!ok) { ea("Delete Failed", "Course not found: " + code); return; }
                    syncFromDataManager(); clearForm();
                    showStatus("✔  Course deleted.", true);
                }
            });
        } catch (IllegalArgumentException ex) { ea("Delete Failed", ex.getMessage()); }
    }

    private int parseCredit() {
        String v = creditField.getText().trim();
        if (v.isEmpty()) throw new IllegalArgumentException("Credit hours cannot be empty.");
        try {
            int n = Integer.parseInt(v);
            if (n <= 0) throw new IllegalArgumentException("Credits must be > 0.");
            return n;
        } catch (NumberFormatException e) { throw new IllegalArgumentException("Credit hours must be a number."); }
    }

    private void populateForm(CourseRow r) {
        nameField.setText(r.getCourseName()); codeField.setText(r.getCourseCode());
        creditField.setText(String.valueOf(r.getCredit())); instructorField.setText(r.getInstructor());
        statusLabel.setVisible(false);
    }

    private void clearForm() {
        nameField.clear(); codeField.clear(); creditField.clear(); instructorField.clear();
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
        Alert a = new Alert(Alert.AlertType.ERROR); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }

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
        btn.setStyle(s); btn.setOnMouseEntered(e->btn.setStyle(s.replace(base,hover))); btn.setOnMouseExited(e->btn.setStyle(s));
        return btn;
    }

    private VBox fg(String l, TextField f) {
        Label lb = new Label(l); lb.setFont(Font.font("Verdana",11)); lb.setTextFill(Color.web("#4a5568"));
        return new VBox(4, lb, f);
    }

    private Label bold(String t) {
        Label l = new Label(t); l.setFont(Font.font("Verdana",FontWeight.BOLD,13)); l.setTextFill(Color.web("#2a3a5a")); return l;
    }

    // Row model
    public static class CourseRow {
        private final SimpleStringProperty  courseName, courseCode, instructor;
        private final SimpleIntegerProperty credit;

        public CourseRow(Course c) {
            this.courseName = new SimpleStringProperty(c.getCourseName());
            this.courseCode = new SimpleStringProperty(c.getCourseCode());
            this.credit     = new SimpleIntegerProperty(c.getCredit());
            this.instructor = new SimpleStringProperty(c.getInstructor());
        }

        public String getCourseName() { return courseName.get(); }
        public String getCourseCode() { return courseCode.get(); }
        public int    getCredit()     { return credit.get(); }
        public String getInstructor() { return instructor.get(); }

        public StringProperty  courseNameProperty() { return courseName; }
        public StringProperty  courseCodeProperty() { return courseCode; }
        public IntegerProperty creditProperty()     { return credit; }
        public StringProperty  instructorProperty() { return instructor; }
    }
}
