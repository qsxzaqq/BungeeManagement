package cc.i9mc.bungeemanagement.listeners;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.utils.AuthRandomizeUtil;
import cc.i9mc.bungeequeue.data.Queue;
import cc.i9mc.bungeequeue.data.QueueRequest;
import cc.i9mc.gameutils.utils.JedisUtil;
import cc.i9mc.pluginchannel.events.BungeeCommandEvent;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class ConnectListener implements Listener {

    public ConnectListener(BungeeManagement bungeeManagement) {
        bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, this);
    }

    @EventHandler
    public void onConnect(ServerConnectEvent event){
        if(event.getTarget().getName().contains("Auth")){
            event.setTarget(BungeeManagement.getInstance().getProxy().getServerInfo(AuthRandomizeUtil.get()));
          //  return;
        }

   /*     if(BungeeManagement.getLobbyController().getBypassQueue().contains(event.getPlayer().getName())){
            BungeeManagement.getLobbyController().getBypassQueue().remove(event.getPlayer().getName());
            return;
        }

        if (BungeeManagement.getGameServerController().getServers().containsKey(event.getTarget().getName())) {

        }*/

/*        for(Map.Entry<String, List<String>> e : BungeeManagement.getLobbyController().getGroupServers().entrySet()){
            if(!e.getValue().contains(event.getTarget().getName())){
                continue;
            }

            BungeeManagement.getLobbyController().getLastLobby().put(event.getPlayer().getName(), e.getKey());
            event.setTarget(BungeeManagement.getLobbyController().getServers().get(e.getKey()));
            return;
        }*/
    }

    @EventHandler
    public void onConnect(PlayerDisconnectEvent event){
        try (Jedis jedis = JedisUtil.getJedis()) {
            jedis.del("GC.LastLobby." + event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onCommand(BungeeCommandEvent event){
        if(event.getString(0).equals("BungeeHub")){
            ProxiedPlayer player = BungeeManagement.getInstance().getProxy().getPlayer(event.getString(2));

            if(event.getString(1).equals("send")){
                if (event.getString(3, null) != null) {
                    QueueRequest queueRequest = new QueueRequest();
                    queueRequest.setUuid(player.getUniqueId().toString());
                    queueRequest.setRedisName(event.getString(3, null));
                    JedisUtil.publish("QueueRequest", BungeeManagement.getLobbyController().gson.toJson(queueRequest));
                } else {
                    String queueHub;
                    try (Jedis jedis = JedisUtil.getJedis()) {
                         queueHub = jedis.get("GC.LastLobby." + player.getUniqueId());
                    }
                    if (queueHub == null) {
                        queueHub = "MainLobby";
                    }

                    QueueRequest queueRequest = new QueueRequest();
                    queueRequest.setUuid(player.getUniqueId().toString());
                    queueRequest.setRedisName(queueHub);
                    JedisUtil.publish("QueueRequest", BungeeManagement.getLobbyController().gson.toJson(queueRequest));
                }
                return;
            }

            if(event.getString(1).equals("to")){
                BungeeManagement.getLobbyController().getBypassQueue().add(player.getName());
                player.connect(BungeeManagement.getInstance().getProxy().getServerInfo(event.getString(3)));
            }
        }
    }

    @EventHandler
    public void onPubSub(PubSubMessageEvent event) {
        if (event.getChannel().equals("Send")) {
            Queue queue = BungeeManagement.getLobbyController().gson.fromJson(event.getMessage(), Queue.class);
            ProxiedPlayer player = BungeeManagement.getInstance().getProxy().getPlayer(UUID.fromString(queue.getUuid()));
            if (player != null) {
                player.connect(BungeeManagement.getInstance().getProxy().getServerInfo(queue.getServer()));
            }
        }
    }
}
