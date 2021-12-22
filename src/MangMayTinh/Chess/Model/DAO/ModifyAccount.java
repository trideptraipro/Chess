package MangMayTinh.Chess.Model.DAO;

import MangMayTinh.Chess.Model.Entity.Account;
import MangMayTinh.Chess.Model.Entity.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ModifyAccount {
    public static int addAccount(Account ac, UserInfo ui){
        PreparedStatement statementUI=null;

        PreparedStatement statementAcc=null;
        int n=0;
        Connection con=null;
        try{
            con= SQLConnect.getConnection();
            con.setAutoCommit(false);
            String sql2= "INSERT INTO `user_info`(`username`, `name`, `email`) VALUES (?,?,?)";
            statementUI=con.prepareStatement(sql2);
            statementUI.setString(1,ac.getUsername());
            statementUI.setString(2,ui.getName());
            statementUI.setString(3,ui.getEmail());
            n=statementUI.executeUpdate();
            if(n==1){
                String sql= "INSERT INTO `account`(`username`, `password`, `role`) VALUES (?,?,?);";
                statementAcc=con.prepareStatement(sql);
                statementAcc.setString(1,ac.getUsername());
                statementAcc.setString(2,ac.getPassword());
                statementAcc.setInt(3,ac.getRole());
                int m= statementAcc.executeUpdate();
                con.commit();
            }else {
                con.rollback();
            }

        }catch (SQLException e){
                e.printStackTrace();
        }
        return n;
    }
    public static UserInfo getUserInfoByUserName(String username){
        UserInfo ui=null;
        PreparedStatement statement=null;
        try{
            Connection connection=SQLConnect.getConnection();
            String sql="SELECT * FROM user_info WHERE username=?";
            statement=connection.prepareStatement(sql);
            statement.setString(1,username);
            ResultSet rs= statement.executeQuery();
            while (rs.next()){
                ui=new UserInfo(rs.getInt("id"),rs.getString("username"),rs.getString("name"),rs.getString("email"),rs.getInt("point"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ui;
    }
    public static boolean updatePoint(UserInfo ui){
        PreparedStatement statement=null;
        try{
            Connection con=SQLConnect.getConnection();
            String sql2="UPDATE user_info SET `point`=? WHERE `username`=?";
            statement=con.prepareStatement(sql2);
            statement.setInt(1,ui.getPoint());
            statement.setString(2,ui.getUsername());
            int n =statement.executeUpdate();
            statement.close();
            statement.close();
            if(n!=0){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean  updateInfo(UserInfo ui){
        PreparedStatement statement=null;
        try{
            Connection con=SQLConnect.getConnection();
            String sql2="UPDATE user_info SET `name`=?,`email`=? WHERE `username`=?";
            statement=con.prepareStatement(sql2);
            statement.setString(1,ui.getName());
            statement.setString(2,ui.getEmail());
            statement.setString(3,ui.getUsername());
            int n =statement.executeUpdate();
            statement.close();
            statement.close();
            if(n!=0){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean updateAccount(Account ac){
        Connection con= SQLConnect.getConnection();
        String query="UPDATE `account` SET `password`=? WHERE `username`=?";
        try{
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,ac.getPassword());
            ps.setString(2,ac.getUsername());
            int rs=ps.executeUpdate();
            ps.close();
            con.close();
            if(rs!=0){
                return  true;
            }

        }catch (SQLException e){

        }
        return false;

    }
    public static boolean checkAccount(Account ac){
        Connection con= SQLConnect.getConnection();
        String query="SELECT * FROM `account` WHERE `username`=? AND `password`=?";
        try{
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,ac.getUsername());
            ps.setString(2,ac.getPassword());
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return  true;
            }
        }catch (SQLException e){

        }

        return false;

    }

    public static void main(String[] args) {
//        System.out.println(ModifyAccount.addAccount(new Account("new",
//        "123"), new UserInfo("new","tri vo", "email@email")));
//        System.out.println(ModifyAccount.getUserInfoByUserName("tridz").getEmail());
        System.out.println(ModifyAccount.checkAccount(new Account("tridz", "kodc")));
        UserInfo ui=ModifyAccount.getUserInfoByUserName("tri123");
        ui.setEmail("vu@69");
        System.out.println(ModifyAccount.updateInfo(ui));
    }
}
