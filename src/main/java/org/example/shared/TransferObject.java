package org.example.shared;



import java.io.Serializable;

/**
 * Đây là "phong bì" chung để gửi đi.
 * Nó chỉ chứa 2 thứ:
 * 1. "Con tem" (type): cho biết đây là loại tin nhắn gì.
 * 2. "Bức thư" (payload): dữ liệu thực tế.
 */


public class TransferObject implements Serializable {
    private MessageType type;
    private Object data;

    public TransferObject(MessageType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public MessageType getType() { return type; }
    public Object getData() { return data; }
}
