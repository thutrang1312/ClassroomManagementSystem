// Nơi: org.example.client.Controllers/ClassroomController.java
package org.example.client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

// SỬA: Thêm các import để load FXML
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

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

    // (Hàm initialize() và các hàm setup... giữ nguyên)
    // ...
    // (Hàm loadCSS() giữ nguyên)
    // ...
    // (Hàm setupCardHandlers() giữ nguyên)
    // ...
    // (Hàm setupNavigationHandlers() giữ nguyên)
    // ...
    // (Hàm setupStartButton() giữ nguyên)
    // ...
    // (Hàm switchTab() giữ nguyên)
    // ...

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


    /**
     * SỬA: Hàm này đã được cập nhật
     * Nó sẽ gọi hàm "openChatWindow()" khi nhấn vào Chat
     * và hiển thị thông báo cho TẤT CẢ các nút khác.
     */
    private void handleFeature(String featureName) {
        System.out.println(featureName + " clicked");
        
        switch(featureName) {
            case "Chat":
                // SỬA: Gọi hàm mở cửa sổ Chat
                openChatWindow();
                break;
                
            case "Share Screen":
                // (Chưa làm)
                showInfo(featureName, "Opening " + featureName + " feature...");
                break;
                
            case "Video Call":
                 // (Chưa làm)
                showInfo(featureName, "Opening " + featureName + " feature...");
                break;
                
            // ... (Thêm các case khác ở đây)
            
            default:
                // SỬA: Các nút còn lại sẽ đi vào đây
                showInfo(featureName, "Opening " + featureName + " feature...");
                break;
        }
    }

    /**
     * SỬA: Thêm hàm mới để mở cửa sổ Chat
     * (Đây là code tôi gửi bạn ở tin nhắn trước)
     */
    private void openChatWindow() {
        try {
            // 1. Tạo một FXML Loader
            // (Đảm bảo đường dẫn /ViewsFXML/chat.fxml là chính xác)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewsFXML/chat.fxml"));
            
            // 2. Load file FXML
            Parent chatRoot = loader.load();

            // 3. Tạo Scene mới
            Scene chatScene = new Scene(chatRoot, 400, 500); // Kích thước

            // 4. SỬA: THÊM CSS CỦA JFOENIX (BẮT BUỘC)
            // Đây là bước sửa lỗi "textNode is null"
            try {
                // Đường dẫn CSS của JFoenix (bắt buộc)
            	String jfoenixCSS = getClass().getResource("/com/jfoenix/controls/css/jfoenix-components.css").toExternalForm();
                
                // Đường dẫn CSS của bạn (ví dụ: main.css, nếu có)
                String mainCSS = getClass().getResource("/css/main.css").toExternalForm();
                
                // Thêm cả 2 file CSS vào Scene
                chatScene.getStylesheets().addAll(jfoenixCSS, mainCSS);
                
            } catch (Exception e) {
                System.err.println("LỖI: Không thể tìm thấy file CSS của JFoenix.");
                // Ứng dụng vẫn sẽ chạy nhưng JFoenix sẽ bị lỗi
            }
            // --- KẾT THÚC SỬA ---

            // 5. Tạo một Cửa sổ (Stage) mới
            Stage chatStage = new Stage();
            chatStage.setTitle("Cửa sổ Chat"); // Đặt tiêu đề
            
            // 6. Đặt Scene vào Cửa sổ
            chatStage.setScene(chatScene);

            // 7. Hiển thị cửa sổ Chat mới
            chatStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Hiển thị lỗi nếu không tìm thấy file chat.fxml
            showError("Lỗi FXML", "Không thể tải file chat.fxml." +
                      "\nLỗi: " + e.getMessage());
        }
    }
    // (Hàm switchTab() giữ nguyên)
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

    // (Hàm showInfo() giữ nguyên)
    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * SỬA: Thêm hàm mới để báo lỗi (nếu cần)
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}