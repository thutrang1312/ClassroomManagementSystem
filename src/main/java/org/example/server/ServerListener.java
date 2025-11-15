package org.example.server;

import org.example.server.Database.DatabaseHelper;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {

    private int port;
    private DatabaseHelper db;

    public ServerListener(int port, DatabaseHelper db) {
        this.port = port;
        this.db = db;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang lắng nghe trên cổng " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Chờ kết nối mới
                System.out.println("Client đã kết nối: " + clientSocket.getInetAddress());

                // Với mỗi client, tạo một luồng xử lý riêng, truyền luôn db vào
                ClientHandler clientHandler = new ClientHandler(clientSocket, db);
                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
