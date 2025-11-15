package org.example.shared;



public enum MessageType {

 // Tương đương "state" cũ của bạn, nhưng rõ ràng hơn
 // state = 3 (String)
 CHAT_MESSAGE,

 // state = 1 (Image)
 SCREEN_SHARE_FRAME,

 // state = 6 (Audio)
 AUDIO_CALL_DATA,

 // state = 4 & 5 (Click & Key)
 REMOTE_CONTROL_EVENT,

 // state = 2 (File) - Sẽ xử lý đặc biệt
 FILE_TRANSFER_REQUEST,
 FILE_TRANSFER_DATA,
 FILE_TRANSFER_END,

 // Các loại tin nhắn mới cho hệ thống
 LOGIN_REQUEST,
 LOGIN_SUCCESS,
 LOGIN_FAIL,
 GET_STUDENT_LIST,
 PING
}