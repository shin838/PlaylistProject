package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    private static DBUtil instance = new DBUtil();
    
    // DB 설정 (포트번호와 DB명, 계정/비번 확인 필수)
    private String url = "jdbc:mysql://localhost:3306/playlistdb?serverTimezone=UTC&useSSL=false";
    private String user = "ureca"; 
    private String pass = "ureca";

    private DBUtil() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBUtil getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public void close(PreparedStatement stmt, Connection con) {
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
        try { if (con != null) con.close(); } catch (SQLException e) { }
    }

    public void close(ResultSet rs, PreparedStatement stmt, Connection con) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { }
        close(stmt, con);
    }
}