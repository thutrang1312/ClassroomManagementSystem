// Nơi: org.example.client.Controllers/ChatController.java
// SỬA: Đổi tên package và tên class
package org.example.client.Controllers;

// SỬA: Import các thư viện JavaFX và layout (Tái sử dụng từ file cũ)
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

// SỬA: Import các thư viện mạng MỚI
import org.example.client.Network.ClientConnection;
import org.example.shared.MessageType;
import org.example.shared.TransferObject;

// SỬA: Import các thư viện để ĐÓNG CỬA SỔ
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

// SỬA: Đổi tên class (khớp với fx:controller)
public class ChatController {

    // --- TÁI SỬ DỤNG: Các biến @FXML (Giữ nguyên) ---
    @FXML private ListView<HBox> chatListView; // Sửa: Dùng HBox cho rõ ràng
    @FXML private TextField messageTextFiled;

    // --- TÁI SỬ DỤNG: Singleton Pattern (Giữ nguyên) ---
    private static ChatController instance;
    public ChatController() {
        instance = this;
    }
    public static ChatController getInstance() {
        return instance;
    }

    // SỬA: Xóa bỏ hàm initialize() cũ.
    // Chúng ta không cần load lịch sử chat từ 'ChatMessages' nữa.
    public void initialize() {
        // (Để trống, hoặc bạn có thể yêu cầu server gửi 10 tin nhắn cuối)
    }

    // SỬA: Đây là hàm gửi tin nhắn (thay thế cho `addToChat()`)
    @FXML
    private void onSendClicked() { // Sửa: Đổi tên hàm cho khớp FXML mới (nếu cần)
        String message = messageTextFiled.getText().trim();
        if (message.isEmpty()) {
            return;
        }

        try {
            // 1. Tạo gói tin
            TransferObject msg = new TransferObject(MessageType.CHAT_MESSAGE, message);
            
            // 2. SỬA: Gửi bằng ClientConnection (thay vì Server.getInstance())
            ClientConnection.getInstance().send(msg);
            
            // 3. Xóa ô nhập liệu
            messageTextFiled.clear();
            
        } catch (Exception e) {
            e.printStackTrace();
            // (Hiển thị lỗi không gửi được)
        }
    }
    
    // SỬA: Đây là hàm NHẬN tin nhắn (thay thế cho `addToChat(type, msg)`)
    // Hàm này sẽ được GỌI BỞI ClientConnection
    public void onNewMessageReceived(String fullMessage) {
        // `fullMessage` sẽ là "TenNguoiGui: Noi dung..."
        
        // TÁI SỬ DỤNG: Logic hiển thị bong bóng chat (từ `addToChat(type, msg)` cũ)
        // Chúng ta sẽ hiển thị MỌI TIN NHẮN (kể cả của mình)
        // với bong bóng màu XÁM, căn TRÁI.
        // (Chúng ta sẽ cải tiến màu xanh/xám sau khi nó chạy)
        
        Label label = new Label(fullMessage); // Hiển thị "User: Msg"
        label.setMaxWidth(300); // Tái sử dụng: Giới hạn chiều rộng
        label.setTextAlignment(TextAlignment.JUSTIFY);
        label.setWrapText(true);
        label.setPadding(new Insets(5));
        label.setTextFill(Color.web("#ffffff", 1)); // Tái sử dụng: Chữ trắng

        // Tái sử dụng: Bong bóng màu xám
        VBox bubble = new VBox(label);
        bubble.setBackground(new Background(new BackgroundFill(Color.GREY, new CornerRadii(15), null)));
        bubble.setMaxWidth(300); // Giới hạn chiều rộng bong bóng

        // Tái sử dụng: Hàng (row)
        HBox row = new HBox(bubble);
        row.setAlignment(Pos.CENTER_LEFT); // Tái sử dụng: Căn trái
        
        chatListView.getItems().add(row);
        chatListView.scrollTo(chatListView.getItems().size() - 1);
    }

    // SỬA: Hàm "Back" (thay thế `backtoHome()`)
    // Hàm này giờ sẽ ĐÓNG cửa sổ chat, thay vì chuyển scene
    @FXML
    private void onBackClicked(ActionEvent event) {
        // Lấy Stage (cửa sổ) hiện tại từ nút "Back"
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Đóng cửa sổ đó lại
        stage.close();
    }
}