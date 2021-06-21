package cc.i9mc.bungeemanagement.bungeeConfig;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

public class ServerHelper {
    private static boolean serverExists(String name) {
        return getServerInfo(name) != null;
    }

    public static boolean serverExists(String name, boolean checkCase) {
        if (checkCase) {
            for (String s : getServers().keySet()) {
                if (name.equals(s)) {
                    return true;
                }
            }

            return false;
        }

        return serverExists(name);
    }

    public static ServerInfo getServerInfo(String name) {
        return getServers().get(name);
    }

    public static void addServer(ServerInfo serverInfo, boolean toConfig) {
        if (serverExists(serverInfo.getName())) {
            try {
                ServerInfo exist = getServerInfo(serverInfo.getName());

                if (serverInfo.getAddress().getHostName().equals(exist.getAddress().getHostName()) && serverInfo.getAddress().getPort() == exist.getAddress().getPort()) {
                    return;
                }

                removeServer(serverInfo.getName());
            } catch (Exception e) {
                return;
            }
        }

        getServers().put(serverInfo.getName(), serverInfo);
        if(toConfig){
            System.err.println("add config");
            ConfigHelper.addToConfig(serverInfo);
            System.err.println("add config done");
        }
    }

    public static void removeServer(String name) {
        if (!serverExists(name)) {
            return;
        }

        ServerInfo info = getServerInfo(name);

        for (ProxiedPlayer p : info.getPlayers()) {
            p.connect(getServers().get(p.getPendingConnection().getListener().getFallbackServer()));
        }

        getServers().remove(name);
        ConfigHelper.removeFromConfig(name);
    }

    public static Map<String, ServerInfo> getServers() {
        return ProxyServer.getInstance().getServers();
    }
}