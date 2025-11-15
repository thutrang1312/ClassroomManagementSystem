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
import org.example.client.Network.ClientConnection;
import org.example.shared.LoginRequest;
import org.example.shared.MessageType;
import org.example.shared.TransferObject;
import org.example.shared.UserInfo;

public class LoginController {

    @FXML
    private TextField txtServerIp;   // ô nhập IP server

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblStatus;

    private ClientConnection connection;
    private UserInfo currentUser;

    // Nếu bạn vẫn muốn set connection từ ClientMain thì vẫn giữ, còn không thì có thể bỏ
    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String serverIp = txtServerIp.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (serverIp.isEmpty()) {
            lblStatus.setText("Nhập IP server (máy giáo viên) trước đã.");
            return;
        }
        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Nhập đủ username và password.");
            return;
        }

        try {
            // Nếu chưa có connection thì tạo mới (mỗi lần login)
            if (connection == null) {
                int port = 5000; // port bạn dùng ở ServerMain
                connection = new ClientConnection(serverIp, port);
            }

            // 1. Tạo request login
            LoginRequest req = new LoginRequest(username, password);
            TransferObject to = new TransferObject(MessageType.LOGIN_REQUEST, req);

            // 2. Gửi lên server
            connection.send(to);

            // 3. Nhận phản hồi
            TransferObject resp = connection.receive();

            // 4. Xử lý kết quả
            if (resp.getType() == MessageType.LOGIN_SUCCESS) {
                currentUser = (UserInfo) resp.getData();
                lblStatus.setText("Xin chào " + currentUser.getFullName());
                openClassroomScene(event);

            } else if (resp.getType() == MessageType.LOGIN_FAIL) {
                lblStatus.setText("Sai tài khoản hoặc mật khẩu.");
            } else {
                lblStatus.setText("Server trả về message không hợp lệ.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Không kết nối được tới server. Kiểm tra lại IP & port.");
            // Nếu lỗi mạng thì reset connection
            connection = null;
        }
    }

    private void openClassroomScene(ActionEvent event) {
        try {
            String fxmlPath;
            String title; // <-- tạo biến title

            if (currentUser.getRole().equalsIgnoreCase("teacher")) {
                fxmlPath = "/ViewsFXML/teacherDashboard.fxml";
                title = "Teacher Panel - Classroom App"; // tiêu đề cho giáo viên
            } else {
                fxmlPath = "/ViewsFXML/classroom.fxml";
                title = "Student Panel - Classroom App"; // tiêu đề cho học sinh
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Nếu muốn truyền connection + user sang Controller
//        ClassroomController controller = loader.getController();
//        controller.setConnection(connection);
//        controller.setCurrentUser(currentUser);

            Scene scene = new Scene(root, 920, 700);

            // Load CSS
            try {
                String cssPath = getClass().getResource("/css/classroom.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (Exception cssError) {
                System.err.println("⚠️ Không load được CSS: " + cssError.getMessage());
            }

            // Lấy Stage hiện tại
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);

            // -----------------------------
            // ✅ Set tiêu đề cửa sổ
            stage.setTitle(title);
            // -----------------------------

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Không load được màn hình classroom.");
        }
    }


}
