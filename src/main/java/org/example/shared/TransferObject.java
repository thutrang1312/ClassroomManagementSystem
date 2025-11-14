package org.example.shared;



import java.io.Serializable;

/**
* Đây là "phong bì" chung để gửi đi.
* Nó chỉ chứa 2 thứ:
* 1. "Con tem" (type): cho biết đây là loại tin nhắn gì.
* 2. "Bức thư" (payload): dữ liệu thực tế.
*/
public class TransferObject implements Serializable {
 private static final long serialVersionUID = 1L; // Rất quan trọng

 private MessageType type;   // "Con tem"
 private Object payload;   // "Bức thư" (dữ liệu)

 public TransferObject(MessageType type, Object payload) {
     this.type = type;
     this.payload = payload;
 }

 public MessageType getType() {
     return type;
 }

 public Object getPayload() {
     return payload;
 }
}

