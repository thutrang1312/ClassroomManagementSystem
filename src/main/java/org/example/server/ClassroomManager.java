package org.example.server;


import org.example.shared.TransferObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClassroomManager {
 private static ClassroomManager instance;
 
 // Key: Tên lớp (ví dụ: "DHKTPM16A")
 // Value: Danh sách các ClientHandler (học sinh/GV) đang trong lớp đó
 private Map<String, List<ClientHandler>> classrooms;

 private ClassroomManager() {
     // Dùng ConcurrentHashMap và CopyOnWriteArrayList để an toàn đa luồng
     classrooms = new ConcurrentHashMap<>();
 }

 public static synchronized ClassroomManager getInstance() {
     if (instance == null) {
         instance = new ClassroomManager();
     }
     return instance;
 }

 // Khi ClientHandler (user) tham gia một lớp
 public void joinRoom(String roomName, ClientHandler handler) {
     // Lấy danh sách handlers của phòng, nếu chưa có thì tạo mới
     classrooms.computeIfAbsent(roomName, k -> new CopyOnWriteArrayList<>())
               .add(handler);
     System.out.println(handler.getUserInfo().getUsername() + " đã vào lớp " + roomName);
 }

 // Khi ClientHandler ngắt kết nối
 public void leaveRoom(String roomName, ClientHandler handler) {
     if (classrooms.containsKey(roomName)) {
         classrooms.get(roomName).remove(handler);
         System.out.println(handler.getUserInfo().getUsername() + " đã rời lớp " + roomName);
     }
 }

 // "Phát sóng" tin nhắn cho mọi người trong lớp
 public void broadcastMessage(String roomName, TransferObject msg) {
     if (classrooms.containsKey(roomName)) {
         List<ClientHandler> handlers = classrooms.get(roomName);
         for (ClientHandler handler : handlers) {
             handler.sendMessage(msg); // Gửi tin nhắn cho từng người
         }
     }
 }
}