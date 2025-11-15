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

    public ClientHandler(Socket socket, DatabaseHelper db) {
        this.socket = socket;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in  = new ObjectInputStream(socket.getInputStream());

            System.out.println("Client connected: " + socket.getInetAddress());

            while (true) {
                TransferObject msg = (TransferObject) in.readObject();
                System.out.println("Server nhận: " + msg.getType());
                handleMessage(msg);
            }

        } catch (Exception e) {
            System.out.println("Client ngắt kết nối: " + socket.getInetAddress());
        } finally {
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    private void handleMessage(TransferObject msg) {
        switch (msg.getType()) {
            case LOGIN_REQUEST:
                handleLogin(msg);
                break;

            // sau này bạn thêm mấy case khác
            // case CHAT_MESSAGE:
            // case DRAW_EVENT:
        }
    }

    private void handleLogin(TransferObject msg) {
        LoginRequest req = (LoginRequest) msg.getData();
        UserInfo user = db.checkLogin(req.getUsername(), req.getPassword());

        if (user != null) {
            System.out.println("Login OK: " + user.getFullName());
            sendMessage(new TransferObject(MessageType.LOGIN_SUCCESS, user));
        } else {
            System.out.println("Login FAILED: " + req.getUsername());
            sendMessage(new TransferObject(MessageType.LOGIN_FAIL, null));
        }
    }

    public void sendMessage(TransferObject msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
