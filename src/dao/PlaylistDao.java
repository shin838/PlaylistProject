package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.PlaylistDto;
import util.DBUtil;

public class PlaylistDao {
    private DBUtil dbutil = DBUtil.getInstance();

    public List<PlaylistDto> getPlaylistsByEmail(String email) throws SQLException {
        List<PlaylistDto> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = dbutil.getConnection();
            String sql = " SELECT p.playlist_id, p.user_id, p.playlist_name " +
                         " FROM playlist p JOIN user u ON p.user_id = u.user_id " +
                         " WHERE u.email = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while(rs.next()) {
                PlaylistDto dto = new PlaylistDto();
                dto.setPlaylistId(rs.getInt("playlist_id"));
                dto.setUserId(rs.getInt("user_id"));
                dto.setPlaylistName(rs.getString("playlist_name"));
                list.add(dto);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return list;
    }

    public void addPlaylist(String email, String playlistName) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = dbutil.getConnection();
            String findUserSql = " SELECT user_id FROM user WHERE email = ? ";
            stmt = con.prepareStatement(findUserSql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String insertSql = " INSERT INTO playlist(user_id, playlist_name) VALUES(?, ?) ";
                PreparedStatement insertStmt = con.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, playlistName);
                insertStmt.executeUpdate();
                insertStmt.close();
            } else {
                throw new SQLException("해당 이메일을 가진 회원이 없습니다.");
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
    }

    public void deletePlaylist(int playlistId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String delMusicSql = " DELETE FROM playlist_music WHERE playlist_id = ? ";
            stmt = con.prepareStatement(delMusicSql);
            stmt.setInt(1, playlistId);
            stmt.executeUpdate();
            stmt.close();
            
            String delPlSql = " DELETE FROM playlist WHERE playlist_id = ? ";
            stmt = con.prepareStatement(delPlSql);
            stmt.setInt(1, playlistId);
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }
}