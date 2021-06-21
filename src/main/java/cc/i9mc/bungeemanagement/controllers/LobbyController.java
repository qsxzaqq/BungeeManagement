package cc.i9mc.bungeemanagement.controllers;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeequeue.BungeeQueue;
import cc.i9mc.bungeequeue.data.QueueRequest;
import cc.i9mc.bungeequeue.serverping.ServerPing;
import cc.i9mc.gameutils.BungeeGameUtils;
import cc.i9mc.gameutils.utils.JedisUtil;
import com.google.gson.Gson;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LobbyController {
    public Gson gson = new Gson();

    //private final Timer timer = new Timer();
/*    @Getter
    private HashMap<String, List<String>> groupServers = new HashMap<>();

    @Getter
    private HashMap<String, ServerInfo> servers = new HashMap<>();*/

    @Getter
    private HashMap<String, String> regexGroup = new HashMap<>();

/*    @Getter
    private HashMap<String, String> lastLobby = new HashMap<>();*/

    @Getter
    private List<String> bypassQueue = new ArrayList<>();

    @Getter
    private List<String> hubCommand = new ArrayList<>();

    public LobbyController(){
        loadRegexGroup();
    }

    public void close(){

    }

    public void loadRegexGroup(){
        regexGroup.clear();
        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("bungee");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM HubRegex;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
               regexGroup.put(resultSet.getString("regex"), resultSet.getString("hub"));
            }

            System.out.println("Load Regex Groups " + regexGroup.size());

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void lobby(ProxiedPlayer player) {
        String queueHub = BungeeManagement.getGameServerController().getToLobby(player.getServer().getInfo().getName());

        if (queueHub == null) {
            for(Map.Entry<String, String> e : BungeeManagement.getLobbyController().getRegexGroup().entrySet()){
                if(player.getServer().getInfo().getName().matches(e.getKey())){
                    queueHub = e.getValue();
                    break;
                }
            }
        }

        if (queueHub == null) {
/*            try (Jedis jedis = JedisUtil.getJedis()) {
                queueHub = jedis.get("GC.LastLobby." + player.getUniqueId());
            } finally {
                if (queueHub == null) {*/
                    queueHub = "MainLobby";
       /*         }
            }*/
        }

        QueueRequest queueRequest = new QueueRequest();
        queueRequest.setUuid(player.getUniqueId().toString());
        queueRequest.setRedisName(queueHub);
        JedisUtil.publish("QueueRequest", gson.toJson(queueRequest));
    }
}
