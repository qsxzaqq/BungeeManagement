package cc.i9mc.bungeemanagement.listeners;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.utils.HanyuPinyinHelperUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatListener implements Listener {
    private final HashMap<ProxiedPlayer, Long> chatCD = new HashMap<>();
    private final HashMap<String, String> chatUp = new HashMap<>();

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();

        if (player.getServer().getInfo().getName().contains("Auth")) {
            if (e.getMessage().toLowerCase().startsWith("/me") || e.getMessage().toLowerCase().startsWith("/minecraft:me") || e.getMessage().toLowerCase().startsWith("/zd") || e.getMessage().toLowerCase().startsWith(".me") || e.getMessage().toLowerCase().startsWith(".minecraft:me")) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getMessage().toLowerCase().startsWith(".l") || e.getMessage().toLowerCase().startsWith(".reg") || e.getMessage().toLowerCase().startsWith(".login") || e.getMessage().toLowerCase().startsWith(".register")) {
            if (!player.getServer().getInfo().getName().contains("Auth")) {
                e.setCancelled(true);
                player.sendMessage(new TextComponent("§c为了您的游戏账号安全,禁止使用带登陆命令的字符."));
                return;
            }
        }

        if (!player.getServer().getInfo().getName().contains("Auth")) {
            if (e.getMessage().toLowerCase().contains(" setowner ")) {
                e.setCancelled(true);
                return;
            }

            if (e.getMessage().toLowerCase().contains("sw leave") || e.getMessage().toLowerCase().contains("skywars leave")) {
                e.setCancelled(true);
                return;
            }

            if (e.getMessage().contains("pex promote 1 1")) {
                e.setCancelled(true);
                return;
            }


            if(e.getMessage().toLowerCase().startsWith("//calc")){
                e.setCancelled(true);
                BungeeManagement.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeManagement.getInstance().getProxy().getConsole(), "kick " + ((ProxiedPlayer) e.getSender()).getName() + " NMSL");
                return;
            }

            if (BungeeManagement.getAdminController().getDatabase().getAdmin(player.getName()) != null) {
                return;
            }

            if (e.getMessage().toLowerCase().equalsIgnoreCase("/aac")) {
                player.sendMessage(new TextComponent("§6[AAC] §7> §6AAC 3.3.14: Haxor rekker (~konsolas)"));
                player.sendMessage(new TextComponent("§6[AAC] §7> §6ID: aace31b2440c4c48b84943cdbc973686f5b/9255653"));
                e.setCancelled(true);
                return;
            }

            if (e.getMessage().startsWith(".say ")
                    || e.getMessage().startsWith("/help")
                    || e.getMessage().startsWith("/?")
                    || e.getMessage().startsWith("/minecraft:me")
                    || e.getMessage().startsWith("/me")) {
                e.setCancelled(true);
                return;
            }

            if(e.getMessage().startsWith("/") && !e.getMessage().startsWith("/lb") && !e.getMessage().startsWith("/zd cj") && !e.getMessage().startsWith("/nc") && !e.getMessage().startsWith("/vipname")){
                return;
            }

            boolean edit = false;
            String message = e.getMessage();
            for(String key : BungeeManagement.getChatController().getKeys()){
                if(e.getMessage().contains(key)){
                    edit = true;
                    StringBuilder rKey = new StringBuilder();
                    for(int i = 0; i < (key.length() + 1); i++){
                        rKey.append("*");
                    }

                    message = message.replace(key, rKey.toString());
                }
            }

            if(edit){
                e.setMessage(message);
            }else {
                String after = HanyuPinyinHelperUtil.toHanyuPinyin(e.getMessage().replace(" ", ""));
                for (String key : BungeeManagement.getChatController().getKeys()) {
                    String value = HanyuPinyinHelperUtil.toHanyuPinyin(key);
                    value = value.toLowerCase();
                    after = after.toLowerCase();
                    if (after.contains(value)) {
                        player.sendMessage(new TextComponent("§4§l禁止发送关键词*"));
                        e.setCancelled(true);
                        return;
                    }
                }

                after = HanyuPinyinHelperUtil.toHanyuPinyinE(e.getMessage().replace(" ", ""));
                for (String key : BungeeManagement.getChatController().getKeys()) {
                    String value = HanyuPinyinHelperUtil.toHanyuPinyin(key);
                    value = value.toLowerCase();
                    after = after.toLowerCase();
                    if (after.contains(value)) {
                        player.sendMessage(new TextComponent("§4§l禁止发送关键词*"));
                        e.setCancelled(true);
                        return;
                    }
                }
            }

            if (System.currentTimeMillis() - chatCD.getOrDefault(player, 0L) < 1500) {
                e.setCancelled(true);
                player.sendMessage(new TextComponent("§4§l请您输入慢一点辣~"));
                return;
            } else {
                this.chatCD.put(player, System.currentTimeMillis());
            }

            if(e.getMessage().startsWith("/")){
                return;
            }

            if (chatUp.getOrDefault(player.getName(), "").contains(e.getMessage()) || chatUp.containsKey(player.getName()) && e.getMessage().contains(chatUp.get(player.getName()))) {
                player.sendMessage(new TextComponent("§e您的近几次发言具有重复性,请勿重复刷屏!"));
                e.setCancelled(true);
                return;
            }
            chatUp.put(player.getName(), e.getMessage());
        }
    }
}
