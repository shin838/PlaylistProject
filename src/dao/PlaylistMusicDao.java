package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.MusicDto;
import util.DBUtil;

public class PlaylistMusicDao {

    private DBUtil dbUtil = DBUtil.getInstance();

    // 플레이리스트에 음악 추가
    public void addMusicToPlaylist(int playlistId, int musicId) throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {

            con = dbUtil.getConnection();

            String sql = """
                    insert into playlist_music(playlist_id, music_id)
                    values (?, ?)
                    """;

            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, musicId);

            pstmt.executeUpdate();

        } finally {
            dbUtil.close(pstmt, con);
        }
    }

    // 플레이리스트 음악 조회
    public List<MusicDto> getPlaylistMusic(int playlistId) throws SQLException {

        List<MusicDto> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbUtil.getConnection();

            String sql = """
                    select m.*
                    from playlist_music pm
                    join music m
                    on pm.music_id = m.music_id
                    where pm.playlist_id = ?
                    """;

            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, playlistId);

            rs = pstmt.executeQuery();

            while(rs.next()) {

                MusicDto music = new MusicDto();

                music.setMusicId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setArtist(rs.getString("artist"));
                music.setGenre(rs.getString("genre"));
                music.setPlayTime(rs.getString("play_time"));

                list.add(music);
            }

        } finally {
            dbUtil.close(rs, pstmt, con);
        }

        return list;
    }

    // 플레이리스트에서 음악 삭제
    public void removeMusicFromPlaylist(int playlistId, int musicId) throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {

            con = dbUtil.getConnection();

            String sql = """
                    delete from playlist_music
                    where playlist_id = ?
                    and music_id = ?
                    """;

            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, musicId);

            pstmt.executeUpdate();

        } finally {
            dbUtil.close(pstmt, con);
        }
    }
}