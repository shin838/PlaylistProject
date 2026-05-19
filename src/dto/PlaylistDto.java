package dto;

public class PlaylistDto {
	private int playlistId;
    private int userId;
    private String playlistName;

    public PlaylistDto() {}
    public PlaylistDto(int playlistId, int userId, String playlistName) {
        this.playlistId = playlistId;
        this.userId = userId;
        this.playlistName = playlistName;
    }
    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
    @Override
    public String toString() {
        return "playlistId=" + playlistId + ", userId=" + userId + ", playlistName=" + playlistName;
    }
}
