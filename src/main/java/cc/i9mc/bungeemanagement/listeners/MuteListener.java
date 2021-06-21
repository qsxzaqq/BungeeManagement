/*
package cc.i9mc.bungeemanagement.listeners;

import cc.i9mc.bungeemanagement.BungeeManagement;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MuteListener implements Listener {

    public MuteListener(BungeeManagement bungeeManagement) {
        bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, this);
    }

    @EventHandler
    public void onChat(ChatEvent event){
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        long date = BungeeManagement.getAdminController().getDatabase().getMuteUntil(player.getName());

        if(date == -999){
            return;
        }

        long diff = date / 1000;

        if(date < 0){
            BungeeManagement.getAdminController().getDatabase().deleteMute(player.getName());
        }else {
            if (diff > 60 * 60 * 24) {
                player.sendMessage(new TextComponent("§c你被禁言了! §a理由: §e" + BungeeManagement.getAdminController().getDatabase().getMuteReason(player.getName()) + " §a剩余: §e" + diff / 60 / 60 / 24 + "天 " + diff / 60 / 60 % 24 + "小时 " + diff / 60 % 60 + "分钟 " + diff % 60 + "秒"));
            } else if (diff > 60 * 60) {
                player.sendMessage(new TextComponent("§c你被禁言了! §a理由: §e" + BungeeManagement.getAdminController().getDatabase().getMuteReason(player.getName()) + " §a剩余: §e" + diff / 60 / 60 + "小时 " + diff / 60 % 60 + "分钟 " + diff % 60 + "秒"));
            } else if (diff > 60) {
                player.sendMessage(new TextComponent("§c你被禁言了! §a理由: §e" + BungeeManagement.getAdminController().getDatabase().getMuteReason(player.getName()) + " §a剩余: §e" + diff / 60 + "分钟 " + diff % 60 + "秒"));
            } else {
                player.sendMessage(new TextComponent("§c你被禁言了! §a理由: §e" + BungeeManagement.getAdminController().getDatabase().getMuteReason(player.getName()) + " §a剩余: §e" + diff + "秒"));
            }
        }
    }
}
*/
