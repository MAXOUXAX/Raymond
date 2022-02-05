package me.maxouxax.raymond.database;

public class DatabaseCredentials {

    private String type;
    private String host;
    private String user;
    private String password;
    private String databaseName;
    private String uri;
    private int port;

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUri() {
        return uri;
    }

    public int getPort() {
        return port;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isValid() {
        return !host.equals("null");
    }

    public String toURI(){
        return uri.replace("{host}", host).replace("{port}", port + "").replace("{databaseName}", databaseName).replace("{user}", user).replace("{password}", password);
    }

}
