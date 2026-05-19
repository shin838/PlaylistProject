package dto;

public class MusicDto {
    private int musicId;
    private String title;
    private String artist;
    private String genre;
    private String playTime; // DB의 varchar(10)에 맞춰 String으로 설정

    // Getter & Setter
    public int getMusicId() { return musicId; }
    public void setMusicId(int musicId) { this.musicId = musicId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getPlayTime() { return playTime; }
    public void setPlayTime(String playTime) { this.playTime = playTime; }
}