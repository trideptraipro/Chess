package MangMayTinh.Chess.Model.Entity;

import java.io.Serializable;

public class Account implements Serializable {
    private String username, password;
    private Integer role;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.role=1;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
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
}
