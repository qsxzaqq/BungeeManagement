package cc.i9mc.bungeemanagement.controllers;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.bungeeConfig.ServerHelper;
import cc.i9mc.gameutils.BungeeGameUtils;
import cc.i9mc.gameutils.utils.JedisUtil;
import cc.i9mc.gameutils.utils.LoggerUtil;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import lombok.Getter;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class GameServerController implements Listener {
    @Getter
    private final ConcurrentHashMap<String, String> servers = new ConcurrentHashMap<>();
    @Getter
    private final HashMap<String, String> hubs = new HashMap<>();

    public GameServerController() {
        BungeeManagement.getInstance().getProxy().getPluginManager().registerListener(BungeeManagement.getInstance(), this);
        JedisUtil.publish("Management", "ResendGameServer|" + BungeeManagement.getInstance().getRedisBungeeAPI().getServerId());
        loadServers();
    }

    public void loadServers(){
        hubs.clear();
        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("bungee");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM GameServerHubs;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                hubs.put(resultSet.getString("game"), resultSet.getString("hub"));
            }

            System.out.println("Load Game Server Hubs " + servers.size());

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getToLobby(String name) {
        if (servers.containsKey(name)) {
            return hubs.getOrDefault(servers.get(name), null);
        }

        return null;
    }

    @EventHandler
    public void onAddServer(PubSubMessageEvent event) {
        if (!event.getChannel().equals("Management")) return;
        StringTokenizer in = new StringTokenizer(event.getMessage(), "|");
        String mode = in.nextToken();
        if(!(mode.equals("addServer") || mode.equals("removeServer"))) return;
        System.out.println(event.getMessage());
        String server = in.nextToken();
        String name = in.nextToken();

        if(!server.equals("ALL") && !server.equals(BungeeManagement.getInstance().getRedisBungeeAPI().getServerId())){
            return;
        }

        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> {
            String ip = in.nextToken();
            String gameType = in.nextToken();
            if(mode.equals("addServer")){
                InetSocketAddress inetSocketAddress = BungeeManagement.getBungeeConfigController().getIp(ip);
                LoggerUtil.info("新增游戏服: 类型" + gameType + " 名称" + name + " 地址" + inetSocketAddress);
                net.md_5.bungee.api.config.ServerInfo info = new BungeeServerInfo(name, inetSocketAddress, "", false);
                servers.put(name, gameType);
                ServerHelper.addServer(info, false);
            }else {
                servers.remove(name);
                ServerHelper.removeServer(name);
            }
        });
    }
}
