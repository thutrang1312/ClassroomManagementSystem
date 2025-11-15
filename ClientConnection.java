package org.example.client.Network;

import org.example.shared.TransferObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientConnection(String host, int port) throws IOException {
        // Kết nối tới server (máy giáo viên)
        socket = new Socket(host, port);

        // Tạo stream gửi/nhận object
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush(); // quan trọng
        in = new ObjectInputStream(socket.getInputStream());
    }

    // Gửi object (TransferObject) lên server
    public synchronized void send(TransferObject obj) throws IOException {
        out.writeObject(obj);
        out.flush();
    }

    // Nhận object (TransferObject) từ server
    public synchronized TransferObject receive() throws IOException, ClassNotFoundException {
        return (TransferObject) in.readObject();
    }

    // Đóng kết nối
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }
}
