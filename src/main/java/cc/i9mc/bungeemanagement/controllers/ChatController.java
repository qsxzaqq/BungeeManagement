package cc.i9mc.bungeemanagement.controllers;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.listeners.ChatListener;
import cc.i9mc.gameutils.BungeeGameUtils;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class ChatController {
    @Getter
    public LinkedList<String> keys = new LinkedList<>();

    public ChatController(){
        BungeeManagement.getInstance().getProxy().getPluginManager().registerListener(BungeeManagement.getInstance(), new ChatListener());
        loadChats();
    }

    public void loadChats(){
        keys.clear();
        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("bungee");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chats;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                keys.add(resultSet.getString("Chat"));
            }

            System.out.println("Load Game Servers " + keys.size());

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
