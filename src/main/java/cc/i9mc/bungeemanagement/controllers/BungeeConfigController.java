package cc.i9mc.bungeemanagement.controllers;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.bungeeConfig.ServerHelper;
import cc.i9mc.gameutils.BungeeGameUtils;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BungeeConfigController {

    public BungeeConfigController() {
        loadServers();
    }

    public void loadServers() {
        HashMap<String, String> dbMap = getServerFromDB();

        for (Map.Entry<String, String> entry : dbMap.entrySet()) {
            if (BungeeManagement.getInstance().getProxy().getServers().containsKey(entry.getKey())) {
                String ip = BungeeManagement.getInstance().getProxy().getServers().get(entry.getKey()).getAddress().getAddress().getHostAddress() + ":" + BungeeManagement.getInstance().getProxy().getServers().get(entry.getKey()).getAddress().getPort();
                if (ip.equals(entry.getValue())) {
                    continue;
                }
            }
            InetSocketAddress address = getIp(entry.getValue());

            if (address == null) {
                System.err.println("No ip found for " + entry.getKey() + " " + entry.getValue());
                continue;
            }
            ServerInfo info = new BungeeServerInfo(entry.getKey(), address, "", false);
            ServerHelper.addServer(info, true);
        }

        System.out.println("load finished!");

    }

    private HashMap<String, String> getServerFromDB() {
        HashMap<String, String> map = new HashMap<>();
        int count = 0;

        try {
            Connection connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("bungee");
            if (connection == null || connection.isClosed()) {
                return map;
            }

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM BungeeConfiguration");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                map.put(resultSet.getString("name"), resultSet.getString("address") + ":" + resultSet.getInt("port"));
                count++;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Load " + count + "Server from Configuration DB!");
        return map;
    }

    public InetSocketAddress getIp(String input) {
        if ((!input.contains(":")) || (!input.contains("."))) {
            return null;
        }

        String[] parts = input.split(":");

        if (input.split(":").length != 2) {
            return null;
        }

        if (input.split("\\.").length != 4) {
            return null;
        }

        for (char c : parts[0].replace(".", "").toCharArray()) {
            if (!Character.isDigit(c)) {
                return null;
            }
        }

        for (char c : parts[1].toCharArray()) {
            if (!Character.isDigit(c)) {
                return null;
            }
        }

        return new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
    }
}
