package MangMayTinh.Chess.Packet;

import MangMayTinh.Chess.Model.Entity.Account;
import MangMayTinh.Chess.Model.Entity.UserInfo;

import java.io.Serializable;

public class LoginPacket implements Serializable {
    Account account;
    String rep;
    UserInfo userInfo;

    public LoginPacket(Account account, String rep) {
        this.account = account;
        this.rep = rep;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }
}
