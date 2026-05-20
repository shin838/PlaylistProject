package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.UserDto;
import util.DBUtil;

public class UserDao {
	private DBUtil dbutil = DBUtil.getInstance();

    // 회원등록
    public void addUser(UserDto user) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = dbutil.getConnection();

            String sql = " INSERT INTO user(email, name) VALUES(?, ?) ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());

            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    // 이메일로 회원조회
    public UserDto searchUserByEmail(String email) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = dbutil.getConnection();

            String sql = " SELECT user_id, email, name FROM user WHERE email = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                UserDto user = new UserDto();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));

                return user;
            }

            return null;
        } finally {
            dbutil.close(rs, stmt, con);
        }
    }

    // 회원수정
    public void updateUser(UserDto user) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = dbutil.getConnection();

            String sql = " UPDATE user SET email = ?, name = ? WHERE user_id = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setInt(3, user.getUserId());

            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    // 회원삭제
    public void removeUser(int userId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = dbutil.getConnection();

            String sql = " DELETE FROM user WHERE user_id = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);

            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }
}
