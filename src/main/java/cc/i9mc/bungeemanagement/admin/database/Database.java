package cc.i9mc.bungeemanagement.admin.database;

import cc.i9mc.bungeemanagement.admin.AdminType;
import cc.i9mc.gameutils.BungeeGameUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static List<AdminData> admins;

    public Database(){
        admins = new ArrayList<>();

        getAdminFromDB();
    }

    public void reloadAdmins(){
        admins.clear();

        getAdminFromDB();
    }

    public AdminData getAdmin(String name){
        for(AdminData adminData : admins){
            if(adminData.getName().equals(name)){
                return adminData;
            }
        }

        return null;
    }

    private void getAdminFromDB() {
        int count = 0;
        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("bungee");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bungeeGroup");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                admins.add(new AdminData(resultSet.getString("Name"), resultSet.getString("DisplayName"), AdminType.valueOf(resultSet.getString("Group").toUpperCase())));
                count++;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Load " + count + " Managements from Configuration DB!");
    }

    public String getBanReason(String s) {
        String reason = null;
        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("ban");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM litebans_bans WHERE uuid=? AND active=1;");
            preparedStatement.setString(1, getBanUUID(s));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                reason = resultSet.getString("reason");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reason;
    }

    public long getBanUntil(String s) {
        long reason = 0;

        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("ban");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM litebans_bans WHERE uuid=? AND active=1;");
            preparedStatement.setString(1, getBanUUID(s));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if(resultSet.getLong("until") == -1){
                    return -1;
                }
                reason = resultSet.getLong("until") - resultSet.getLong("time");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reason;
    }

    private String getBanUUID(String s) {
        String uuid = null;
        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("ban");

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM litebans_history WHERE name=?");
            preparedStatement.setString(1, s);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                uuid = resultSet.getString("uuid");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return uuid;
    }

    /*public String getMuteReason(String name) {
        String reason = null;

        try {
            Connection connection = BanSQLUtil.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bm_mute WHERE player=?;");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                reason = resultSet.getString("reason");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reason;
    }

    public long getMuteUntil(String name) {
        long reason = -999;

        try {
            Connection connection = BanSQLUtil.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bm_mute WHERE player=?;");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                reason = resultSet.getLong("until") - resultSet.getLong("time");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reason;
    }

    public void deleteMute(String id) {
        try {
            Connection connection = BanSQLUtil.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM bm_mute WHERE player=?;");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }*/
}
