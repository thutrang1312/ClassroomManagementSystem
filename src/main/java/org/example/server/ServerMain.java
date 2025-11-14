package org.example.server;


public class ServerMain {
 public static void main(String[] args) {
     System.out.println("Server đang khởi động...");
     ServerListener listener = new ServerListener(1234); // Dùng cổng 1234
     listener.start();
 }
}