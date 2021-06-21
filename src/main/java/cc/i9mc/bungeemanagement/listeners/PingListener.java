package cc.i9mc.bungeemanagement.listeners;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.gameutils.BungeeGameUtils;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class PingListener implements Listener {
    public TextComponent serverMotd = null;
    private Favicon favicon;

    public PingListener(BungeeManagement bungeeManagement) {
        bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, this);

        loadMOTD();
    }

    public void loadMOTD() {
        String temp = null;
        String tempPrefix = null;

        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = BungeeGameUtils.getInstance().getConnectionPoolHandler().getConnection("bungee");
            if (connection == null || connection.isClosed()) {
                return;
            }
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM BungeeGlobals WHERE (configKey = 'motd' || configKey = 'motdPrefix')");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString("configKey").equals("motd")) {
                    temp = resultSet.getString("object");
                } else if (resultSet.getString("configKey").equals("motdPrefix")) {
                    tempPrefix = resultSet.getString("object");
                } else if (resultSet.getString("configKey").equals("favicon")) {
                    favicon = Favicon.create(resultSet.getString("object"));
                }
            }
            serverMotd = new TextComponent(tempPrefix + "\n" + centerText(temp));

            System.out.println(serverMotd);

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String centerText(String text) {
        StringBuilder builder = new StringBuilder(text);
        char space = ' ';
        int distance = (55 - text.length()) / 2;
        for (int i = 0; i < distance; ++i) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        String ip;
        try {
            ip = event.getConnection().getAddress().getAddress().getHostAddress();
        }catch (Exception e){
            return;
        }

        if(ip == null){
            return;
        }

        if (!BungeeManagement.getAntiMOTDAttackController().getCountMap().containsKey(ip)) {
            BungeeManagement.getAntiMOTDAttackController().getCountMap().put(ip , 0);
            BungeeManagement.getAntiMOTDAttackController().sendCommand(ip, 0);
        }

        if (BungeeManagement.getAntiMOTDAttackController().getCountMap().get(ip) >= 10) {
            event.getConnection().disconnect(new TextComponent());
        } else if (BungeeManagement.getAntiMOTDAttackController().getTotalCount() >= 100) {
            event.getConnection().disconnect(new TextComponent());
        } else {
            BungeeManagement.getAntiMOTDAttackController().setTotalCount(BungeeManagement.getAntiMOTDAttackController().getTotalCount() + 1);
            BungeeManagement.getAntiMOTDAttackController().getCountMap().put(ip , BungeeManagement.getAntiMOTDAttackController().getCountMap().get(ip) + 1);
            BungeeManagement.getAntiMOTDAttackController().sendCommand(ip, BungeeManagement.getAntiMOTDAttackController().getCountMap().get(ip) + 1);
            event.getResponse().setDescriptionComponent(serverMotd);
        }
    }

    @EventHandler
    public void onPartyMessage(PubSubMessageEvent evt) {
        if (!evt.getChannel().equals("Management")) {
            return;
        }

        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> {
            StringTokenizer in = new StringTokenizer(evt.getMessage(), "|");
            String action = in.nextToken();

            if (action.equals("motd")) {
                String ip = in.nextToken();
                String count = in.nextToken();

                BungeeManagement.getAntiMOTDAttackController().getCountMap().put(ip, Integer.valueOf(count));
            }
        });

    }
}
