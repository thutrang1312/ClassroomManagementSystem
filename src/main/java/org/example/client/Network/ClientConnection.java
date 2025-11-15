
package org.example.client.Network;

import javafx.application.Platform;
import org.example.client.Controllers.LoginController;
import org.example.client.Controllers.ChatController; // SỬA: Thêm import mới
import org.example.shared.TransferObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {

    private static ClientConnection instance;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Constructor (đã chuẩn)
    public ClientConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush(); 
        in = new ObjectInputStream(socket.getInputStream());
        
        startListening();
    }
    
    // Hàm connect (đã chuẩn)
    public static void connect(String host, int port) throws IOException {
        if (instance == null) {
            instance = new ClientConnection(host, port);
        }
    }

    public static ClientConnection getInstance() {
        return instance;
    }

    // Hàm Gửi (đã chuẩn)
    public synchronized void send(TransferObject obj) throws IOException {
        out.writeObject(obj);
        out.flush();
    }

    // Luồng Lắng Nghe (đã chuẩn)
    private void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    TransferObject msgFromServer = (TransferObject) in.readObject();
                    handleServerMessage(msgFromServer);
                }
            } catch (Exception e) {
                System.out.println("Mất kết nối tới server.");
            }
        }).start(); 
    }

    // Hàm Xử Lý Tin Nhắn (NÂNG CẤP)
    private void handleServerMessage(TransferObject msg) {
        
        Platform.runLater(() -> {
            try {
                switch (msg.getType()) {
                    
                    case LOGIN_SUCCESS:
                        LoginController.getInstance().onLoginSuccess(msg.getData());
                        break;
                        
                    case LOGIN_FAIL:
                        LoginController.getInstance().onLoginFail();
                        break;

                    // SỬA: THÊM CASE MỚI CHO CHAT
                    case CHAT_MESSAGE:
                        // Lấy nội dung tin nhắn (vd: "UserA: Hello")
                        String message = (String) msg.getData();
                        
                        // Kiểm tra xem ChatController có đang chạy không
                        if (ChatController.getInstance() != null) {
                            // Gọi hàm của ChatController để hiển thị
                            ChatController.getInstance().onNewMessageReceived(message);
                        }
                        break;
                    
                    // ... (các case khác sau này)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Hàm Đóng kết nối (đã chuẩn)
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
        instance = null;
    }
}