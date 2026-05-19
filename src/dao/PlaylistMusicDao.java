package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.MusicDto;
import util.DBUtil;

public class PlaylistMusicDao {
    private DBUtil dbutil = DBUtil.getInstance();

    public List<MusicDto> getMusicInPlaylist(int playlistId) throws SQLException {
        List<MusicDto> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = dbutil.getConnection();
            String sql = " SELECT m.music_id, m.title, m.artist, m.genre, m.play_time " +
                         " FROM playlist_music pm JOIN music m ON pm.music_id = m.music_id " +
                         " WHERE pm.playlist_id = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, playlistId);
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

    public void addMusicToPlaylist(int playlistId, int musicId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = " INSERT INTO playlist_music(playlist_id, music_id) VALUES(?, ?) ";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, playlistId);
            stmt.setInt(2, musicId);
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    public void removeMusicFromPlaylist(int playlistId, int musicId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = " DELETE FROM playlist_music WHERE playlist_id = ? AND music_id = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, playlistId);
            stmt.setInt(2, musicId);
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }
}