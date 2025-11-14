package org.example.server;


import org.example.shared.TransferObject; // Dùng "ngôn ngữ" chung
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

 private Socket socket;
 private ObjectOutputStream out;
 private ObjectInputStream in;

 public ClientHandler(Socket socket) {
     this.socket = socket;
 }

 @Override
 public void run() {
     try {
         // Thiết lập luồng Gửi (out) và Nhận (in)
         out = new ObjectOutputStream(socket.getOutputStream());
         in = new ObjectInputStream(socket.getInputStream());

         // Vòng lặp lắng nghe tin nhắn TỪ client này
         while (true) {
             TransferObject msg = (TransferObject) in.readObject();

             // (Tạm thời) In ra để test
             System.out.println("Server nhận được: " + msg.getType());

             // Xử lý logic (sẽ làm ở giai đoạn 4)
             handleMessage(msg);
         }

     } catch (Exception e) {
         System.out.println("Client ngắt kết nối: " + socket.getInetAddress());
     } finally {
         // Đóng socket khi client thoát
         try { socket.close(); } catch (Exception e) {}
     }
 }

 // Nơi xử lý logic (sẽ làm sau)
 private void handleMessage(TransferObject msg) {
     // Dùng switch-case ở đây
 }

 // Hàm để gửi tin nhắn TỚI client này
 public void sendMessage(TransferObject msg) {
     try {
         out.writeObject(msg);
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
}