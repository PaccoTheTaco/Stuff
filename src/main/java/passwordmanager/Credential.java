package passwordmanager;

import java.io.Serializable;

public class Credential implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String username;
    private String password;
    private String url;

    public Credential(String title, String username, String password, String url) {
        this.title = title;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
