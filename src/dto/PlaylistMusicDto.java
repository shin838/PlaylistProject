package dto;

public class PlaylistMusicDto {

    private int playlistId;
    private int musicId;

    public PlaylistMusicDto() {
    }

    public PlaylistMusicDto(int playlistId, int musicId) {
        this.playlistId = playlistId;
        this.musicId = musicId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }
}