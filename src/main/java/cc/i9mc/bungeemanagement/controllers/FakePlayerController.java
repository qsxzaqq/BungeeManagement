package cc.i9mc.bungeemanagement.controllers;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.gameutils.BungeeGameUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FakePlayerController {
    public int players = 0;
    private Timer timer = new Timer();

    public FakePlayerController(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> list = getPlayers();

                int i = 0;
                for(String s : list){
                    if(BungeeManagement.getInstance().getRedisBungeeAPI().isPlayerOnline(BungeeManagement.getInstance().getRedisBungeeAPI().getUuidFromName(s))){
                        i++;
                    }
                }

                players = i;
            }
        }, 200, 200);
    }

    public void close(){
        timer.cancel();
    }

    public List<String> getPlayers() {
        List<String> list = new ArrayList<>();

        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("playerdata");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM authenticate WHERE Success='true' OR Success=1;");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("Name"));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
