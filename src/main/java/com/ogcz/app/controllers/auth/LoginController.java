package com.ogcz.app.controllers.auth;

import java.io.IOException;

import com.ogcz.app.database.AuthService;
import com.ogcz.app.database.User;
import com.ogcz.app.database.UserSession;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    @FXML
    private ImageView ara_login_a;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private boolean isFirstImage = true;
    private boolean isFirstButton = true;

    @FXML
    private ImageView pictureButton;

    @FXML
    private Pane dummyFocus;

    @FXML
    private Pane backgroundPane2;
    @FXML
    private Pane backgroundPane3;
    @FXML
    private Pane backgroundPane4;

    @FXML
    private Button signupButton;

    @FXML
    private Button siginButton;

    private final AuthService authentication = new AuthService();

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/assets/z_favicon.png").toString()));
        alert.showAndWait();
    }

    @FXML
    private void handleSignInClick(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("ARA Motorhub", "Please enter both email and password.");
            return;
        }

        User loggedInUser = authentication.loginAndGetUser(email, password);

        if (loggedInUser == null) {
            showAlert("ARA Motorhub", "Invalid email or password.");
            return;
        }

        UserSession.setCurrentUser(loggedInUser);
        String role = loggedInUser.getRole();

        try {
            FXMLLoader loader;
            switch (role.toLowerCase()) {
                case "customer":
                    loader = new FXMLLoader(getClass().getResource("/fxml/customer/HomeMain.fxml"));
                    break;
                case "seller":
                    loader = new FXMLLoader(getClass().getResource("/fxml/store/Profile.fxml"));
                    break;
                case "admin":
                    loader = new FXMLLoader(getClass().getResource("/fxml/admin/Admin.fxml"));
                    break;
                default:
                    showAlert("ARA Motorhub", "Unrecognized role.");
                    return;
            }

            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("ARA Motorhub", "Failed to load the dashboard.");
        }
    }

    @FXML
    private void handleSignUpClick(MouseEvent event) {
        System.out.println("signup pressed");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/RegisterStep1.fxml"));
            Parent newRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene newScene = new Scene(newRoot);
            currentStage.setScene(newScene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackgroundClick(MouseEvent event) {
        dummyFocus.requestFocus(); // this removes focus from email/password fields
    }

    // switch pictures
    @FXML
    private void handleImageClick(MouseEvent event) {

        {
            String imagePath = isFirstButton
                    ? "resources/assets/ara_login_button_a.png"
                    : "resources/assets/ara_login_button_b.png";

            Image image = new Image(getClass().getClassLoader().getResourceAsStream(imagePath));

            if (image.isError()) {
                System.out.println("Image failed to load: " + imagePath);
            } else {
                pictureButton.setImage(image);
            }

            isFirstButton = !isFirstButton;
        }

        System.out.println("Login Window: Picture Changed");
        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), ara_login_a);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            String imagePath = isFirstImage ? "resources/assets/ara_login_a.png" : "resources/assets/ara_login_b.png";
            Image image = new Image(getClass().getClassLoader().getResourceAsStream(imagePath));

            if (image.isError()) {
                System.out.println("Image failed to load: " + imagePath);
            }

            ara_login_a.setImage(image);
            isFirstImage = !isFirstImage;

            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), ara_login_a);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    @FXML
    public void initialize() {

        Glow glow = new Glow(0.8);

        signupButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> signupButton.setEffect(glow));
        signupButton.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> signupButton.setEffect(null));

        dummyFocus.setFocusTraversable(true);
        backgroundPane2.setFocusTraversable(true);
        backgroundPane3.setFocusTraversable(true);
        backgroundPane4.setFocusTraversable(true);
        Platform.runLater(dummyFocus::requestFocus);

        
        emailField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                emailField.setStyle("-fx-border-color: rgba(0,0,0,0.05); -fx-border-radius: 5; -fx-background-radius: 5; -fx-focus-color: #B7B710; -fx-faint-focus-color: transparent;"); // default color when empty
            } else {
                emailField.setStyle("-fx-border-color: #B7B710; -fx-border-radius: 5; -fx-background-radius: 5; -fx-focus-color: #B7B710; -fx-faint-focus-color: transparent;"); // color when typing
            }
        });

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
               passwordField.setStyle("-fx-border-color: rgba(0,0,0,0.05); -fx-border-radius: 5; -fx-background-radius: 5; -fx-focus-color: #B7B710; -fx-faint-focus-color: transparent;");
            } else {
                passwordField.setStyle("-fx-border-color: #B7B710; -fx-border-radius: 5; -fx-background-radius: 5; -fx-focus-color: #B7B710; -fx-faint-focus-color: transparent;");
            }
        }); 
        
    }
}
