package org.example.client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class ClassroomController {

    @FXML private AnchorPane root;
    @FXML private Button startBtn;
    @FXML private Button homeBtn;
    @FXML private Button notificationsBtn;
    @FXML private Button settingsBtn;

    @FXML private VBox shareScreenCard;
    @FXML private VBox viewShareCard;
    @FXML private VBox whiteBoardCard;
    @FXML private VBox remoteControlCard;
    @FXML private VBox audioCallCard;
    @FXML private VBox videoCallCard;
    @FXML private VBox chatCard;
    @FXML private VBox sendFileCard;
    @FXML private VBox tasksCard;
    @FXML private VBox profileCard;

    @FXML
    public void initialize() {
        // Load CSS file
        loadCSS();
        setupCardHandlers();
        setupNavigationHandlers();
        setupStartButton();
    }

    private void loadCSS() {
        try {
            // Get the root AnchorPane's scene
            if (root != null && root.getScene() != null) {
                String css = getClass().getResource("/css/classroom.css").toExternalForm();
                root.getScene().getStylesheets().add(css);
                System.out.println("CSS loaded successfully: " + css);
            } else {
                // If scene is not ready, add listener
                if (root != null) {
                    root.sceneProperty().addListener((obs, oldScene, newScene) -> {
                        if (newScene != null) {
                            String css = getClass().getResource("/css/classroom.css").toExternalForm();
                            newScene.getStylesheets().add(css);
                            System.out.println("CSS loaded via listener: " + css);
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupCardHandlers() {
        // Share Screen
        shareScreenCard.setOnMouseClicked(e -> handleFeature("Share Screen"));

        // View Share
        viewShareCard.setOnMouseClicked(e -> handleFeature("View Share"));

        // White Board
        whiteBoardCard.setOnMouseClicked(e -> handleFeature("White Board"));

        // Remote Control
        remoteControlCard.setOnMouseClicked(e -> handleFeature("Remote Control"));

        // Audio Call
        audioCallCard.setOnMouseClicked(e -> handleFeature("Audio Call"));

        // Video Call
        videoCallCard.setOnMouseClicked(e -> handleFeature("Video Call"));

        // Chat
        chatCard.setOnMouseClicked(e -> handleFeature("Chat"));

        // Send File
        sendFileCard.setOnMouseClicked(e -> handleFeature("Send File"));

        // Tasks
        tasksCard.setOnMouseClicked(e -> handleFeature("Tasks"));

        // Profile
        profileCard.setOnMouseClicked(e -> handleFeature("Profile"));
    }

    private void setupNavigationHandlers() {
        homeBtn.setOnAction(e -> switchTab("Home"));
        notificationsBtn.setOnAction(e -> switchTab("Notifications"));
        settingsBtn.setOnAction(e -> switchTab("Settings"));
    }

    private void setupStartButton() {
        startBtn.setOnAction(e -> {
            showInfo("Session Started", "Your classroom session has started!");
        });
    }

    private void handleFeature(String featureName) {
        System.out.println(featureName + " clicked");
        showInfo(featureName, "Opening " + featureName + " feature...");

        // Add your specific feature logic here
        switch(featureName) {
            case "Share Screen":
                // Implement screen sharing logic
                break;
            case "Video Call":
                // Implement video call logic
                break;
            case "Chat":
                // Implement chat logic
                break;
            // Add other cases as needed
        }
    }

    private void switchTab(String tabName) {
        System.out.println("Switched to " + tabName + " tab");

        // Remove active class from all buttons
        homeBtn.getStyleClass().remove("nav-btn-active");
        notificationsBtn.getStyleClass().remove("nav-btn-active");
        settingsBtn.getStyleClass().remove("nav-btn-active");

        // Add active class to clicked button
        switch(tabName) {
            case "Home":
                homeBtn.getStyleClass().add("nav-btn-active");
                break;
            case "Notifications":
                notificationsBtn.getStyleClass().add("nav-btn-active");
                break;
            case "Settings":
                settingsBtn.getStyleClass().add("nav-btn-active");
                break;
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}