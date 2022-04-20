package me.maxouxax.raymond.config;

public class UnivConfig {

    private String authUrl;
    private String dataUrl;
    private String username;
    private String password;
    private String clockEmoji;

    public UnivConfig() {
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClockEmoji() {
        return clockEmoji;
    }

    public void setClockEmoji(String clockEmoji) {
        this.clockEmoji = clockEmoji;
    }

}
