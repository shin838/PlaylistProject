package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.MusicDto;
import util.DBUtil;

public class MusicDao {
    private DBUtil dbutil = DBUtil.getInstance();

    public void addMusic(MusicDto music) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = " INSERT INTO music(title, artist, genre, play_time) VALUES(?,?,?,?) ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, music.getTitle());
            stmt.setString(2, music.getArtist());
            stmt.setString(3, music.getGenre());
            stmt.setString(4, music.getPlayTime());
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    public List<MusicDto> searchAllMusic() throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<MusicDto> list = new ArrayList<>();
        try {
            con = dbutil.getConnection();
            String sql = " SELECT music_id, title, artist, genre, play_time FROM music ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                MusicDto music = new MusicDto();
                music.setMusicId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setArtist(rs.getString("artist"));
                music.setGenre(rs.getString("genre"));
                music.setPlayTime(rs.getString("play_time"));
                list.add(music);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return list;
    }

    public void updateMusic(MusicDto music) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = " UPDATE music SET title=?, artist=?, genre=?, play_time=? WHERE music_id=? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, music.getTitle());
            stmt.setString(2, music.getArtist());
            stmt.setString(3, music.getGenre());
            stmt.setString(4, music.getPlayTime());
            stmt.setInt(5, music.getMusicId());
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    public void removeMusic(int musicId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            
            // 1. 연결된 플레이리스트 기록 먼저 삭제
            String delPmSql = " DELETE FROM playlist_music WHERE music_id = ? ";
            stmt = con.prepareStatement(delPmSql);
            stmt.setInt(1, musicId);
            stmt.executeUpdate();
            stmt.close();

            // 2. 음악 삭제
            String sql = " DELETE FROM music WHERE music_id = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, musicId);
            stmt.executeUpdate();
            stmt.close();

            // 3. AUTO_INCREMENT 재정렬 (가장 큰 ID 값 + 1로 카운터 초기화)
            String resetSql = " ALTER TABLE music AUTO_INCREMENT = 1 ";
            stmt = con.prepareStatement(resetSql);
            stmt.executeUpdate();
            
        } finally {
            dbutil.close(stmt, con);
        }
    }
}