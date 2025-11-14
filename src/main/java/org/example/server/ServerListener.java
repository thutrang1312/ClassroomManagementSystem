package org.example.server;


import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
 private int port;

 public ServerListener(int port) {
     this.port = port;
 }

 public void start() {
     try (ServerSocket serverSocket = new ServerSocket(port)) {
         System.out.println("Server đang lắng nghe trên cổng " + port + "...");

         while (true) {
             Socket clientSocket = serverSocket.accept(); // Chờ kết nối mới
             System.out.println("Client đã kết nối: " + clientSocket.getInetAddress());

             // Với mỗi client, tạo một luồng xử lý riêng
             ClientHandler clientHandler = new ClientHandler(clientSocket);
             new Thread(clientHandler).start();
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
}
