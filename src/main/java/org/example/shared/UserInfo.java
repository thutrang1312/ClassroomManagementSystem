package org.example.shared;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private int id;
    private String studentId;
    private String username;
    private String fullName;
    private String className;
    private String role;   // <-- thêm role

    public UserInfo(int id, String studentId, String username, String fullName, String className, String role) {
        this.id = id;
        this.studentId = studentId;
        this.username = username;
        this.fullName = fullName;
        this.className = className;
        this.role = role;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getClassName() {
        return className;
    }

    public String getRole() {
        return role;
    }
}
