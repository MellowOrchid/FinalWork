package ExpDevices.entity;

import java.io.Serializable;

public class Database implements Serializable {
    private String host;
    private long port;
    private String DB_name;
    private String user;
    private String pwd;

    public Database() {
    }

    public Database(String host, long port, String dB_name, String user, String pwd) {
        this.host = host;
        this.port = port;
        DB_name = dB_name;
        this.user = user;
        this.pwd = pwd;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public String getDB_name() {
        return DB_name;
    }

    public void setDB_name(String dB_name) {
        DB_name = dB_name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return host + "$" + port + "$" + DB_name + "$" + user + "$" + pwd;
    }

}
