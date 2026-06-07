import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.util.function.Consumer;
import javafx.scene.Node;

public class LoginView {
    private static final String[][] VALID_CREDENTIALS = {
        { "admin",   "admin123"  },
        { "staff",   "iium2025"  },
        { "student", "welcome1"  }
    };

    private final BorderPane root;
    private final Consumer<String> onLoginSuccess;

    public LoginView(Consumer<String> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        this.root = buildLayout();
    }

    public Pane getRoot() { return root; }

    private BorderPane buildLayout() {
        BorderPane bp = new BorderPane();
        bp.setLeft(buildBrandPanel());
        bp.setCenter(buildFormPanel());
        bp.setStyle("-fx-background-color: #0a1628;");
        return bp;
    }

    private VBox buildBrandPanel() {
        VBox brand = new VBox(20);
        brand.setPrefWidth(380);
        brand.setAlignment(Pos.CENTER);
        brand.setPadding(new Insets(60, 40, 60, 40));
        brand.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #0d2144, #1a3a6e);" +
            "-fx-border-color: #2a5298; -fx-border-width: 0 1 0 0;"
        );

        Node crest = buildCrest();

        Label uniLabel = styledLabel("IIUM", 34, FontWeight.BOLD, "#f0c040");
        uniLabel.setStyle(uniLabel.getStyle() +
            "-fx-effect: dropshadow(gaussian, rgba(240,192,64,0.4), 12, 0, 0, 0);");

        Label fullName = styledLabel("International Islamic\nUniversity Malaysia", 13,
                FontWeight.NORMAL, "#a8c0e8");
        fullName.setTextAlignment(TextAlignment.CENTER);
        fullName.setWrapText(true);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2a5298;");
        sep.setPrefWidth(100);
        sep.setMaxWidth(100);

        Label systemLabel = styledLabel("University Management System", 12,
                FontWeight.SEMI_BOLD, "#7aa0d4");
        systemLabel.setTextAlignment(TextAlignment.CENTER);
        systemLabel.setWrapText(true);

        Label versionLabel = styledLabel("v2.0  ·  Academic Year 2025/2026", 10,
                FontWeight.NORMAL, "#4a6a9a");

        brand.getChildren().addAll(crest, uniLabel, fullName, sep, systemLabel, versionLabel);
        return brand;
    }

    private Node buildCrest() {
        try {
            java.io.File file = new java.io.File("assets/logoiium.png");
            
            if (file.exists()) {
                javafx.scene.image.Image img = new javafx.scene.image.Image(file.toURI().toString());
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(img);
                
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                
                return imageView;
            } else {
                System.out.println("Warning: assets/logoiium.png not found. Falling back to text placeholder.");
            }
        } catch (Exception e) {
            System.err.println("Failed to load asset brand imagery: " + e.getMessage());
        }

        Label fallback = new Label("IIUM");
        fallback.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        fallback.setTextFill(Color.web("#f0c040"));
        return fallback;
    }

    private StackPane buildFormPanel() {
        StackPane wrapper = new StackPane();
        wrapper.setStyle("-fx-background-color: #0a1628;");

        VBox card = new VBox(22);
        card.setPadding(new Insets(50, 50, 50, 50));
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 30, 0, 0, 8);"
        );

        Label welcome = styledLabel("Welcome back", 13, FontWeight.NORMAL, "#6b7a99");
        Label signIn  = styledLabel("Sign in to continue", 26, FontWeight.BOLD, "#0d2144");

        Separator sep = new Separator();
        sep.setPadding(new Insets(0, 0, 8, 0));

        Label userLabel = fieldLabel("Username");
        TextField userField = styledTextField("e.g. admin");
        userField.setPrefHeight(44);

        Label passLabel = fieldLabel("Password");
        PasswordField passField = new PasswordField();
        passField.setPromptText("••••••••");
        passField.setPrefHeight(44);
        styleInputField(passField);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-size: 12px;");
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);

        Button loginBtn = new Button("Sign In  →");
        loginBtn.setPrefWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(46);
        loginBtn.setStyle(
            "-fx-background-color: #1a3a6e;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );
        loginBtn.setOnMouseEntered(e ->
            loginBtn.setStyle(loginBtn.getStyle().replace("#1a3a6e", "#2a5298")));
        loginBtn.setOnMouseExited(e ->
            loginBtn.setStyle(loginBtn.getStyle().replace("#2a5298", "#1a3a6e")));

        Label hint = styledLabel("Demo credentials: admin / admin123", 10,
                FontWeight.NORMAL, "#aab5cc");

        Runnable doLogin = () -> handleLogin(
            userField.getText().trim(),
            passField.getText(),
            errorLabel,
            passField
        );
        loginBtn.setOnAction(e -> doLogin.run());
        passField.setOnAction(e -> doLogin.run());
        userField.setOnAction(e -> passField.requestFocus());

        card.getChildren().addAll(
            welcome, signIn, sep,
            userLabel, userField,
            passLabel, passField,
            errorLabel,
            loginBtn,
            hint
        );

        wrapper.getChildren().add(card);
        StackPane.setMargin(card, new Insets(40));
        return wrapper;
    }

    private void handleLogin(String username, String password,
                             Label errorLabel, PasswordField passField) {
        if (username.isEmpty() || password.isEmpty()) {
            showError(errorLabel, "Please enter both username and password.");
            return;
        }

        for (String[] cred : VALID_CREDENTIALS) {
            if (cred[0].equalsIgnoreCase(username) && cred[1].equals(password)) {
                onLoginSuccess.accept(username);
                return;
            }
        }

        passField.clear();
        showError(errorLabel, "Invalid username or password. Please try again.");
    }

    private void showError(Label label, String message) {
        label.setText("⚠  " + message);
        label.setVisible(true);
    }

    private Label styledLabel(String text, double size, FontWeight weight, String hexColor) {
        Label l = new Label(text);
        l.setFont(Font.font("Georgia", weight, size));
        l.setTextFill(Color.web(hexColor));
        return l;
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 11));
        l.setTextFill(Color.web("#4a5568"));
        return l;
    }

    private TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        styleInputField(tf);
        return tf;
    }

    private void styleInputField(TextInputControl field) {
        field.setStyle(
            "-fx-background-color: #f7f9fc;" +
            "-fx-border-color: #c8d3e8;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 12;" +
            "-fx-font-size: 13px;"
        );
        field.focusedProperty().addListener((obs, old, focused) -> {
            String base =
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 8 12;";
            if (focused) {
                field.setStyle(base +
                    "-fx-background-color: #ffffff;" +
                    "-fx-border-color: #2a5298;" +
                    "-fx-border-width: 1.5;");
            } else {
                field.setStyle(base +
                    "-fx-background-color: #f7f9fc;" +
                    "-fx-border-color: #c8d3e8;" +
                    "-fx-border-width: 1;");
            }
        });
    }
}
