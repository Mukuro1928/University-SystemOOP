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
 * Developed by: AHMAD MUKHLIS BIN ZAKARIAH 2514371
 * ClubWorkspace.java — Club & Event Management Panel
 * ==================================================
 * Provides a graphical interface for managing university clubs
 * and their associated events.
 *
 * Features:
 *  • Register new clubs and club presidents.
 *  • Create optional event information for each club.
 *  • Display all clubs in a TableView.
 *  • Delete existing clubs.
 *  • Synchronize data with DataManager and persist changes
 *    through FileHandler.
 *
 * Architecture:
 *  • Uses ObservableList for automatic TableView updates.
 *  • Retrieves and modifies data through the DataManager singleton.
 *  • Supports form auto-population when a table row is selected.
 *  • Validates user input and displays errors using JavaFX Alerts.
 */

public class ClubWorkspace {

    private final ObservableList<ClubRow> tableModel = FXCollections.observableArrayList();
    private TableView<ClubRow> table;
    private TextField clubNameField, presidentField, eventNameField,
                      eventDateField, eventLocField, attendeeField;
    private Label statusLabel;

    public Node build() {
        syncFromDataManager();
        VBox root = new VBox(0);
        root.getChildren().addAll(buildPanelHeader(), buildContentArea());
        return root;
    }

    private void syncFromDataManager() {
        tableModel.clear();
        for (Club c : DataManager.getInstance().getClubList())
            tableModel.add(new ClubRow(c));
    }

    private HBox buildPanelHeader() {
        HBox h = new HBox(); h.setPadding(new Insets(0,0,20,0)); h.setAlignment(Pos.CENTER_LEFT);
        VBox g = new VBox(2);
        Label t = new Label("Club Management");
        t.setFont(Font.font("Georgia",FontWeight.BOLD,24)); t.setTextFill(Color.web("#0d2144"));
        Label s = new Label("Register clubs and manage their upcoming events.");
        s.setFont(Font.font("Verdana",12)); s.setTextFill(Color.web("#6b7a99"));
        g.getChildren().addAll(t, s);
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label badge = new Label();
        badge.setStyle("-fx-background-color:#eedcff;-fx-text-fill:#6a1a6a;" +
                       "-fx-font-size:12px;-fx-font-weight:bold;-fx-padding:4 12;-fx-background-radius:20;");
        tableModel.addListener((ListChangeListener<ClubRow>) c ->
            badge.setText(tableModel.size() + " club" + (tableModel.size()==1?"":"s")));
        badge.setText(tableModel.size() + " clubs");
        h.getChildren().addAll(g, sp, badge);
        return h;
    }

    @SuppressWarnings("unchecked")
    private HBox buildContentArea() {
        TableColumn<ClubRow,String>  nameCol = new TableColumn<>("Club Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("clubName")); nameCol.setPrefWidth(160);
        TableColumn<ClubRow,String>  presCol = new TableColumn<>("President");
        presCol.setCellValueFactory(new PropertyValueFactory<>("presidentName")); presCol.setPrefWidth(150);
        TableColumn<ClubRow,String>  evtCol = new TableColumn<>("Upcoming Event");
        evtCol.setCellValueFactory(new PropertyValueFactory<>("eventName")); evtCol.setPrefWidth(180);
        TableColumn<ClubRow,String>  dateCol = new TableColumn<>("Event Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("eventDate")); dateCol.setPrefWidth(110);
        TableColumn<ClubRow,String>  locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(new PropertyValueFactory<>("eventLocation")); locCol.setPrefWidth(120);
        TableColumn<ClubRow,Integer> attCol = new TableColumn<>("Attendees");
        attCol.setCellValueFactory(new PropertyValueFactory<>("attendeeCount")); attCol.setPrefWidth(80);
        attCol.setStyle("-fx-alignment:CENTER;");

        table = new TableView<>(tableModel);
        table.getColumns().addAll(nameCol, presCol, evtCol, dateCol, locCol, attCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No clubs registered yet."));
        table.setPrefHeight(420); table.setStyle("-fx-font-size:12px;");
        table.getSelectionModel().selectedItemProperty().addListener(
            (obs,old,sel) -> { if (sel!=null) populateForm(sel); });

        Label tl = new Label("Registered Clubs");
        tl.setFont(Font.font("Verdana",FontWeight.BOLD,13)); tl.setTextFill(Color.web("#2a3a5a"));
        VBox ts = new VBox(12, tl, table);
        ts.setStyle("-fx-background-color:#ffffff;-fx-background-radius:10;-fx-padding:20;" +
                    "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),10,0,0,3);");
        VBox.setVgrow(table, Priority.ALWAYS); HBox.setHgrow(ts, Priority.ALWAYS);

        clubNameField  = sf("Club Name");
        presidentField = sf("President Name");
        eventNameField = sf("Event Name");
        eventDateField = sf("Event Date (e.g. 2025-12-01)");
        eventLocField  = sf("Event Location");
        attendeeField  = sf("Expected Attendees (number)");
        statusLabel    = new Label(); statusLabel.setWrapText(true);
        statusLabel.setFont(Font.font("Verdana",11)); statusLabel.setVisible(false);

        Button addBtn    = ab("➕  Add Club",    "#6a1a6a", "#9a2a9a");
        Button deleteBtn = ab("🗑  Delete Club",  "#8a1a1a", "#c0392b");
        Button clearBtn  = ab("↺  Clear Form",    "#4a5568", "#6b7a99");

        addBtn.setOnAction(e    -> handleAdd());
        deleteBtn.setOnAction(e -> handleDelete());
        clearBtn.setOnAction(e  -> clearForm());

        Label note = new Label("ℹ  Event fields are optional — leave blank to register " +
                               "a club without an event yet.");
        note.setFont(Font.font("Verdana",10)); note.setTextFill(Color.web("#8899bb")); note.setWrapText(true);

        VBox form = new VBox(10,
            bold("Club Details"), new Separator(),
            fg("Club Name",   clubNameField),
            fg("President",   presidentField),
            bold("Event Details (optional)"), new Separator(),
            fg("Event Name",  eventNameField),
            fg("Date",        eventDateField),
            fg("Location",    eventLocField),
            fg("Attendees",   attendeeField),
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
            String cName = req(clubNameField,  "Club Name");
            String pName = req(presidentField, "President Name");
            String eName = eventNameField.getText().trim();
            String eDate = eventDateField.getText().trim();
            String eLoc  = eventLocField.getText().trim();
            int    att   = parseAttendees();
            DataManager.getInstance().addClub(cName, pName, eName, eDate, eLoc, att);
            syncFromDataManager(); clearForm();
            FileHandler.saveAllData();
            showStatus("✔  Club added successfully.", true);
        } catch (IllegalArgumentException ex) { ea("Add Club Failed", ex.getMessage()); }
    }

    private void handleDelete() {
        try {
            String cName = req(clubNameField, "Club Name");
            Alert c = new Alert(Alert.AlertType.CONFIRMATION, "Delete club '" + cName + "'?");
            c.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    try {
                        DataManager.getInstance().deleteClub(cName);
                        syncFromDataManager(); clearForm();
                        FileHandler.saveAllData();
                        showStatus("✔  Club deleted.", true);
                    } catch (IllegalArgumentException ex) { ea("Delete Failed", ex.getMessage()); }
                }
            });
        } catch (IllegalArgumentException ex) { ea("Delete Failed", ex.getMessage()); }
    }

    private int parseAttendees() {
        String v = attendeeField.getText().trim();
        if (v.isEmpty()) return 0;
        try { return Integer.parseInt(v); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Attendee count must be a number."); }
    }

    private void populateForm(ClubRow r) {
        clubNameField.setText(r.getClubName());
        presidentField.setText(r.getPresidentName());        
        eventNameField.setText(r.getEventName()); eventDateField.setText(r.getEventDate());
        eventLocField.setText(r.getEventLocation()); attendeeField.setText(String.valueOf(r.getAttendeeCount()));
        statusLabel.setVisible(false);
    }

    private void clearForm() {
        clubNameField.clear(); presidentField.clear(); eventNameField.clear();
        eventDateField.clear(); eventLocField.clear(); attendeeField.clear();
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

    public static class ClubRow {
        private final SimpleStringProperty  clubName, presidentName, eventName, eventDate, eventLocation;
        private final SimpleIntegerProperty attendeeCount;

        public ClubRow(Club c) {
            Event e = c.getEvent();
            this.clubName      = new SimpleStringProperty(c.getClubName());
            this.presidentName = new SimpleStringProperty(c.getPresidentName());
            this.eventName     = new SimpleStringProperty(e != null ? e.getEventName()     : "—");
            this.eventDate     = new SimpleStringProperty(e != null ? e.getEventDate()     : "—");
            this.eventLocation = new SimpleStringProperty(e != null ? e.getEventLocation() : "—");
            this.attendeeCount = new SimpleIntegerProperty(e != null ? e.getAttendeeCount(): 0);
        }

        public String getClubName()      { return clubName.get(); }
        public String getPresidentName() { return presidentName.get(); }
        public String getEventName()     { return eventName.get(); }
        public String getEventDate()     { return eventDate.get(); }
        public String getEventLocation() { return eventLocation.get(); }
        public int    getAttendeeCount() { return attendeeCount.get(); }

        public StringProperty  clubNameProperty()      { return clubName; }
        public StringProperty  presidentNameProperty() { return presidentName; }
        public StringProperty  eventNameProperty()     { return eventName; }
        public StringProperty  eventDateProperty()     { return eventDate; }
        public StringProperty  eventLocationProperty() { return eventLocation; }
        public IntegerProperty attendeeCountProperty() { return attendeeCount; }
    }
}
