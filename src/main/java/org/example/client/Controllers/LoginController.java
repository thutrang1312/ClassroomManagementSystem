// Nơi: org.example.client.Controllers/LoginController.java
// ĐÂY LÀ PHIÊN BẢN BẤT ĐỒNG BỘ (ASYNCHRONOUS) CHUẨN

package org.example.client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.client.Network.ClientConnection; // <-- Khớp với file ClientConnection của bạn
import org.example.shared.LoginRequest;
import org.example.shared.MessageType;
import org.example.shared.TransferObject;
import org.example.shared.UserInfo;

public class LoginController {

    @FXML private TextField txtServerIp;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    private UserInfo currentUser;

    // SỬA: Thêm Singleton
    private static LoginController instance;

    public LoginController() {
        instance = this; // Tự gán mình khi FXML loader tạo ra
    }

    public static LoginController getInstance() {
        return instance;
    }
    // ------------------------------------

    // SỬA: Xóa 2 dòng này
    // private ClientConnection connection;
    // public void setConnection(ClientConnection connection) { ... }

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String serverIp = txtServerIp.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (serverIp.isEmpty() || username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Nhập đủ IP, Username và Password.");
            return;
        }

        try {
            // SỬA: Kết nối bằng hàm static connect()
            ClientConnection.connect(serverIp, 5000); // 5000 là port của bạn

            // 1. Tạo request login
            LoginRequest req = new LoginRequest(username, password);
            TransferObject to = new TransferObject(MessageType.LOGIN_REQUEST, req);

            // 2. Gửi (Fire-and-Forget)
            ClientConnection.getInstance().send(to);
            lblStatus.setText("Đang đăng nhập..."); // UI không bị đơ

            // SỬA: XÓA HOÀN TOÀN logic "receive()" và "if/else"
            /*
             TransferObject resp = connection.receive(); // <-- XÓA
             if (resp.getType() == MessageType.LOGIN_SUCCESS) { // <-- XÓA
                ...
             } else ... // <-- XÓA
            */

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Không kết nối được tới server. Kiểm tra lại IP.");
        }
    }

    // SỬA: Thêm 2 hàm "Callback"
    // ClientConnection sẽ GỌI các hàm này

    public void onLoginSuccess(Object data) {
        this.currentUser = (UserInfo) data;
        lblStatus.setText("Xin chào " + currentUser.getFullName());
        openClassroomScene(); // Gọi hàm chuyển scene
    }

    public void onLoginFail() {
        lblStatus.setText("Sai tài khoản hoặc mật khẩu.");
    }

    // SỬA: Sửa lại hàm openClassroomScene
    private void openClassroomScene() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ViewsFXML/classroom.fxml")
            );
            Parent root = loader.load();

            // (Giữ nguyên logic truyền dữ liệu nếu cần)
            // ClassroomController controller = loader.getController();
            // controller.setCurrentUser(currentUser);

            Scene scene = new Scene(root, 920, 700);

            // (Giữ nguyên logic load CSS)
            try {
                String cssPath = getClass().getResource("/css/classroom.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (Exception cssError) {
                System.err.println("⚠️ Không load được CSS: " + cssError.getMessage());
            }

            // SỬA: Lấy Stage từ một component bất kỳ (ví dụ: lblStatus)
            Stage stage = (Stage) lblStatus.getScene().getWindow();
            stage.setScene(scene); 
            stage.setTitle("Student Panel - " + currentUser.getFullName()); // Đặt tiêu đề
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Không load được màn hình classroom.");
        }
    }
}