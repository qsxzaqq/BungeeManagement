package cc.i9mc.bungeemanagement.listeners;

import cc.i9mc.bungeemanagement.BungeeManagement;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CheatListener implements Listener {

    private List<String> code = Collections.synchronizedList(new ArrayList<>());

    public CheatListener(BungeeManagement bungeeManagement) {
        register();

        bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, this);
    }

    private void register() {
        // Rex miniMap
        code.add("§3 §6 §3 §6 §3 §6 §e");
        //miniMap
        code.add("§0§0§1§2§3§5§e§f");
        //Damage Indictor
        code.add("§0§0§c§d§e§f");
        // CJB Xray 防御CJB透视
        code.add("§3 §9 §2 §0 §0 §2");
        //CJB Fly 防御CJB作弊飞行
        code.add("§3 §9 §2 §0 §0 §1");
        //CJB Radar 防御CJB雷达
        code.add("§3 §9 §2 §0 §0 §3");

        //Rei's Minimap 防御Rei小地图
        code.add("§0§0§1§e§f");
        code.add("§0§0§2§3§4§5§6§7§e§f");
        //Zan MiniMap 防御Zan小地图
        code.add("§3 §6 §3 §6 §3 §6 §d");

        //Automap 防御AutoMap
        code.add("§0§0§1§f§e");
        code.add("§0§0§1§f§e");
        code.add("§0§0§3§4§5§6§7§8§f§e");

        //SmartMove 防御灵活动作作弊
        code.add("§0§1§0§1§2§f§f");
        code.add("§0§1§3§4§f§f");
        code.add("§0§1§5§f§f");
        code.add("§0§1§6§f§f");
        code.add("§0§1§7§f§f");
        code.add("§0§1§8§9§a§b§f§f");

        //Schematica 防御Schematica模组
        code.add("§0§2§0§0§e§f");
        code.add("§0§2§1§0§e§f");
        code.add("§0§2§1§1§e§f");

        // Zombe 防御Zombe秒破坏和飞行等作弊
        code.add("§f §f §2 §0 §4 §8");
        code.add("§f §f §4 §0 §9 §6");
        code.add("§f §f §1 §0 §2 §4");

        BungeeManagement.getInstance().getProxy().registerChannel("WDL|INIT");
        BungeeManagement.getInstance().getProxy().registerChannel("PERMISSIONREPL");
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (("WDL|INIT".equalsIgnoreCase(event.getTag())) &&
                ((event.getSender() instanceof ProxiedPlayer))) {
            event.getSender().disconnect(new TextComponent("§c请尊重我们的劳动成果.\n §c no World Donwloader is allowed"));
        }

        if (("PERMISSIONSREPL".equalsIgnoreCase(event.getTag())) &&
                (new String(event.getData()).contains("mod.worlddownloader")))
            event.getSender().disconnect(new TextComponent("§c请尊重我们的劳动成果.\n §c no World Donwloader is allowed!"));

    }

    @EventHandler
    public void onJoinAntiCheatLis(PostLoginEvent evt) {
        final ProxiedPlayer p = evt.getPlayer();
        BungeeManagement.getInstance().getProxy().getScheduler().schedule(BungeeManagement.getInstance(), () -> {
            for (String s : code) {
                p.sendMessage(new TextComponent(s));
            }
        }, 500L, TimeUnit.MILLISECONDS);
    }
}
