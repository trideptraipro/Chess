/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Model.Enum;

import java.io.Serializable;

/**
 *
 * @author thinhle
 */
public enum MessageType implements Serializable {
    string, bool, move, message, result, startGame, turn, isFirstPlayer, name, surrender, endGame, login, play,register;
}
