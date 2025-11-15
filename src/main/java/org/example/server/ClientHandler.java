
package org.example.server;

import org.example.server.Database.DatabaseHelper;
import org.example.shared.LoginRequest;
import org.example.shared.MessageType;
import org.example.shared.TransferObject;
import org.example.shared.UserInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DatabaseHelper db;
    private UserInfo userInfo; // Giữ thông tin user sau khi login

    public ClientHandler(Socket socket, DatabaseHelper db) {
        this.socket = socket;
        this.db = db;
    }

    // Cần hàm này để ClassroomManager biết client này là ai
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in  = new ObjectInputStream(socket.getInputStream());

            System.out.println("Client connected: " + socket.getInetAddress());

            // Vòng lặp chính: lắng nghe mọi tin nhắn từ client này
            while (true) {
                TransferObject msg = (TransferObject) in.readObject();
                System.out.println("Server nhận (" + (userInfo != null ? userInfo.getUsername() : "Guest") + "): " + msg.getType());
                handleMessage(msg);
            }

        } catch (Exception e) {
            // Lỗi này thường xảy ra khi client đóng ứng dụng đột ngột
            System.out.println("Client ngắt kết nối: " + socket.getInetAddress());
        } finally {
            // SỬA: Dọn dẹp client khỏi phòng học khi họ thoát
            if (userInfo != null) {
                ClassroomManager.getInstance().leaveRoom(userInfo.getClassName(), this);
            }
            
            // Đóng socket
            try { 
                if (socket != null && !socket.isClosed()) socket.close(); 
            } catch (Exception ignored) {}
        }
    }

    // Hàm "điều phối" tin nhắn
    private void handleMessage(TransferObject msg) {
        // Chỉ xử lý nếu đã login, ngoại trừ tin nhắn LOGIN_REQUEST
        if (userInfo == null && msg.getType() != MessageType.LOGIN_REQUEST) {
            System.out.println("Lỗi: Client chưa đăng nhập nhưng gửi tin nhắn " + msg.getType());
            return; // Bỏ qua, không xử lý
        }
        
        switch (msg.getType()) {
            case LOGIN_REQUEST:
                handleLogin(msg);
                break;

            // SỬA: Thêm case xử lý chat
            case CHAT_MESSAGE:
                handleChatMessage(msg);
                break;
                
            // case DRAW_EVENT:
            //    handleDrawEvent(msg);
            //    break;
        }
    }

    // Xử lý khi client yêu cầu đăng nhập
    private void handleLogin(TransferObject msg) {
        LoginRequest req = (LoginRequest) msg.getData();
        UserInfo user = db.checkLogin(req.getUsername(), req.getPassword());

        if (user != null) {
            this.userInfo = user; // 1. Lưu lại thông tin user
            
            System.out.println("Login OK: " + user.getFullName());
            sendMessage(new TransferObject(MessageType.LOGIN_SUCCESS, user));
            
            // SỬA: 2. Thêm user vào phòng học
            ClassroomManager.getInstance().joinRoom(user.getClassName(), this);
            
        } else {
            System.out.println("Login FAILED: " + req.getUsername());
            sendMessage(new TransferObject(MessageType.LOGIN_FAIL, null));
        }
    }
    
    // SỬA: Thêm hàm xử lý tin nhắn chat
    private void handleChatMessage(TransferObject msg) {
        // Payload của tin nhắn chat chỉ là một String (nội dung)
        String chatContent = (String) msg.getData();
        
        // Tạo một tin nhắn hoàn chỉnh (bao gồm cả tên người gửi)
        String fullMessage = userInfo.getFullName() + ": " + chatContent;
        
        // Tạo một TransferObject mới để "phát sóng" (broadcast)
        TransferObject broadcastMsg = new TransferObject(MessageType.CHAT_MESSAGE, fullMessage);
        
        // Gọi ClassroomManager để gửi tin nhắn này cho CẢ LỚP
        ClassroomManager.getInstance().broadcastMessage(userInfo.getClassName(), broadcastMsg);
    }


    // Hàm gửi tin nhắn (chỉ gửi cho client này)
    public synchronized void sendMessage(TransferObject msg) {
        // Thêm kiểm tra out != null để tránh lỗi nếu gửi trước khi luồng sẵn sàng
        if (out == null) {
            System.err.println("Lỗi: ObjectOutputStream chưa sẵn sàng.");
            return;
        }
        
        try {
            out.writeObject(msg);
            out.flush();
        } catch (Exception e) {
            // Lỗi này thường xảy ra nếu client đã ngắt kết nối
            // e.printStackTrace();
            System.err.println("Không thể gửi tin nhắn tới " + (userInfo != null ? userInfo.getUsername() : "Guest"));
        }
    }
}