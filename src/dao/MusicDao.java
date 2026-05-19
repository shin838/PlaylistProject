package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.MusicDto;
import util.DBUtil;

public class MusicDao {

    private DBUtil dbUtil = DBUtil.getInstance();

    // 음악 추가
    public void addMusic(MusicDto music) throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dbUtil.getConnection();

            String sql = """
                    insert into music(title, artist, genre, play_time)
                    values (?, ?, ?, ?)
                    """;

            pstmt = con.prepareStatement(sql);

            int idx = 1;
            pstmt.setString(idx++, music.getTitle());
            pstmt.setString(idx++, music.getArtist());
            pstmt.setString(idx++, music.getGenre());
            pstmt.setInt(idx++, music.getPlayTime());

            pstmt.executeUpdate();

        } finally {
            dbUtil.close(pstmt, con);
        }
    }

    // 음악 전체 조회
    public List<MusicDto> getAllMusic() throws SQLException {

        List<MusicDto> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbUtil.getConnection();

            String sql = "select * from music";

            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while(rs.next()) {

                MusicDto music = new MusicDto();

                music.setMusicId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setArtist(rs.getString("artist"));
                music.setGenre(rs.getString("genre"));
                music.setPlayTime(rs.getInt("play_time"));

                list.add(music);
            }

        } finally {
            dbUtil.close(rs, pstmt, con);
        }

        return list;
    }

    // 음악 단일 조회
    public MusicDto searchMusic(int musicId) throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbUtil.getConnection();

            String sql = """
                    select *
                    from music
                    where music_id = ?
                    """;

            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, musicId);

            rs = pstmt.executeQuery();

            if(rs.next()) {

                MusicDto music = new MusicDto();

                music.setMusicId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setArtist(rs.getString("artist"));
                music.setGenre(rs.getString("genre"));
                music.setPlayTime(rs.getInt("play_time"));

                return music;
            }

        } finally {
            dbUtil.close(rs, pstmt, con);
        }

        return null;
    }

    // 음악 수정
    public void updateMusic(MusicDto music) throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {

            con = dbUtil.getConnection();

            String sql = """
                    update music
                    set title = ?, artist = ?, genre = ?, play_time = ?
                    where music_id = ?
                    """;

            pstmt = con.prepareStatement(sql);

            int idx = 1;

            pstmt.setString(idx++, music.getTitle());
            pstmt.setString(idx++, music.getArtist());
            pstmt.setString(idx++, music.getGenre());
            pstmt.setInt(idx++, music.getPlayTime());
            pstmt.setInt(idx++, music.getMusicId());

            pstmt.executeUpdate();

        } finally {
            dbUtil.close(pstmt, con);
        }
    }

    // 음악 삭제
    public void deleteMusic(int musicId) throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {

            con = dbUtil.getConnection();

            String sql = """
                    delete from music
                    where music_id = ?
                    """;

            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, musicId);

            pstmt.executeUpdate();

        } finally {
            dbUtil.close(pstmt, con);
        }
    }
}