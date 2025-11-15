package org.example.server.Database;

import org.example.shared.UserInfo;

import java.sql.*;

public class DatabaseHelper {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME   = "classroom_db";
    private static final String DB_URL    = MYSQL_URL + DB_NAME + "?useSSL=false&serverTimezone=UTC";

    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private Connection conn;

    public DatabaseHelper() throws SQLException {
        createDatabaseIfNotExists();   // 1. Tạo database nếu chưa có
        connect();                     // 2. Kết nối vào database
        createUsersTable();            // 3. Tạo bảng users nếu chưa có
        insertDemoUsers();             // 4. Thêm user mẫu nếu chưa tồn tại
    }

    // ==========================================================
    // 1) Tạo DATABASE nếu chưa có
    // ==========================================================
    private void createDatabaseIfNotExists() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection tempConn = DriverManager.getConnection(MYSQL_URL, DB_USER, DB_PASS);
                 Statement st = tempConn.createStatement()) {

                st.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                System.out.println("Database OK (đã tồn tại hoặc tự tạo mới).");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy MySQL JDBC Driver.");
            throw new SQLException(e);
        }
    }

    // ==========================================================
    // 2) Kết nối vào database classroom_db
    // ==========================================================
    private void connect() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        System.out.println("Đã kết nối tới MySQL: " + DB_URL);
    }

    // ==========================================================
    // 3) Tạo bảng users nếu chưa có
    // ==========================================================
    private void createUsersTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id         INT AUTO_INCREMENT PRIMARY KEY,
                    student_id VARCHAR(50),
                    username   VARCHAR(50) UNIQUE,
                    password   VARCHAR(100),
                    full_name  VARCHAR(100),
                    class_name VARCHAR(50)
                );
                """;

        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }

        System.out.println("Bảng users OK (đã tồn tại hoặc tự tạo mới).");
    }

    // ==========================================================
    // 4) Thêm 2 user mẫu nếu chưa có
    // ==========================================================
    private void insertDemoUsers() throws SQLException {
        String sql = """
                INSERT IGNORE INTO users (student_id, username, password, full_name, class_name)
                VALUES 
                    ('20123456', 'sv01', '123456', 'Nguyen Van A', 'DHKTPM16A'),
                    ('20123457', 'sv02', '123456', 'Tran Thi B',  'DHKTPM16A');
                """;

        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        }

        System.out.println("User mẫu đã sẵn sàng.");
    }

    // ==========================================================
    // 5) Hàm kiểm tra login
    // ==========================================================
    public UserInfo checkLogin(String username, String password) {
        String sql = """
                SELECT id, student_id, username, full_name, class_name
                FROM users
                WHERE username = ? AND password = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UserInfo(
                        rs.getInt("id"),
                        rs.getString("student_id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("class_name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void close() {
        try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
    }
}
