package dto;

public class MusicDto {

    private int musicId;
    private String title;
    private String artist;
    private String genre;
    private String playTime;

    public MusicDto() {
    }

    public MusicDto(int musicId, String title, String artist, String genre, String playTime) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.playTime = playTime;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    @Override
    public String toString() {
        return "MusicDto [musicId=" + musicId + ", title=" + title
                + ", artist=" + artist + ", genre=" + genre
                + ", playTime=" + playTime + "]";
    }
}