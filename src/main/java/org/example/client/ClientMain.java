package org.example.client;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//XÓA: import sample.Network.Server;

public class ClientMain extends Application { // ĐỔI TÊN class

 private static ClientMain instance;
 public static Stage stage;

 public ClientMain() { // ĐỔI TÊN constructor
     instance = this;
 }

 public static ClientMain getInstance() {
     return instance;
 }

 // XÓA: Toàn bộ phương thức init() đã bị xóa bỏ
 // Client không còn khởi chạy Server nữa.

 @Override
 public void start(Stage primaryStage) throws Exception {
     stage = primaryStage;
     
     // SỬA: Đường dẫn FXML
     // Đường dẫn mới này sẽ tìm file từ "root" của classpath
     Parent root = FXMLLoader.load(getClass().getResource("/sample/client/resources/ViewsFXML/sample.fxml"));
     
     primaryStage.setTitle("Tuition-E");
     Scene scene = new Scene(root, 576, 476);
     primaryStage.setScene(scene);
     primaryStage.setResizable(false);
     primaryStage.setOnCloseRequest((event) -> {
         Platform.exit(); // Thoát JavaFX
         System.exit(0); // Đóng tất cả các luồng (bao gồm cả luồng mạng)
     });
     primaryStage.show();
 }

 public static void main(String[] args) {
     launch(args);
 }
}