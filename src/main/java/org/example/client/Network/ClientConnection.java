package org.example.client.Network;


import org.example.shared.TransferObject;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
 private static ClientConnection instance;
 private Socket socket;
 private ObjectOutputStream out;
 private ObjectInputStream in;

 // Dùng Singleton
 public static synchronized ClientConnection getInstance() {
     if (instance == null) {
         instance = new ClientConnection();
     }
     return instance;
 }

 // 1. Kết nối tới Server
 public void connect(String ip, int port) throws Exception {
     socket = new Socket(ip, port);
     out = new ObjectOutputStream(socket.getOutputStream());
     in = new ObjectInputStream(socket.getInputStream());

     // 2. Khởi chạy luồng lắng nghe (RẤT QUAN TRỌNG)
     startListening();
 }

 // 3. Luồng lắng nghe (tai của client)
 private void startListening() {
     new Thread(() -> {
         try {
             while (true) {
                 TransferObject msg = (TransferObject) in.readObject();
                 // Xử lý tin nhắn từ Server (sẽ làm ở giai đoạn 4)
                 handleServerMessage(msg);
             }
         } catch (Exception e) {
             System.out.println("Mất kết nối Server.");
         }
     }).start();
 }

 // 4. Hàm để Gửi tin nhắn (miệng của client)
 public void sendMessage(TransferObject msg) {
     try {
         out.writeObject(msg);
     } catch (Exception e) {
         e.printStackTrace();
     }
 }

 // 5. Nơi xử lý tin nhắn từ Server (sẽ làm sau)
 private void handleServerMessage(TransferObject msg) {
     // Dùng switch-case ở đây
 }
}