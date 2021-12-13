package MangMayTinh.Chess.Model.Entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
    int id;
    String username, name;
    String email;
    Integer point;

    public UserInfo(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public UserInfo(int id, String username, String name, String email, Integer point) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
