package MangMayTinh.Chess.Packet;

import MangMayTinh.Chess.Model.Entity.Account;
import MangMayTinh.Chess.Model.Entity.UserInfo;

import java.io.Serializable;

public class RegisterPacket implements Serializable {
    Account account;
    UserInfo userInfo;
}
