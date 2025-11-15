package org.example.server;

import org.example.server.Database.DatabaseHelper;

public class ServerMain {

    public static void main(String[] args) {
        int port = 5000; // cổng cho client kết nối

        try {
            // Tạo helper DB (tự tạo database + bảng + user mẫu)
            DatabaseHelper db = new DatabaseHelper();

            // Tạo listener và start server
            ServerListener listener = new ServerListener(port, db);
            listener.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
