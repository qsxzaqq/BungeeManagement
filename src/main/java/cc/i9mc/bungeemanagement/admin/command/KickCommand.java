package cc.i9mc.bungeemanagement.admin.command;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.admin.database.AdminData;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.StringTokenizer;

public class KickCommand extends Command implements Listener {
    public KickCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (player.getServer().getInfo().getName().contains("Auth")) {
            return;
        }

        AdminData adminData = BungeeManagement.getAdminController().getDatabase().getAdmin(player.getName());

        if (adminData == null) {
            return;
        }

        if(args.length == 0){
            player.sendMessage(new TextComponent("§c格式错误!"));
            return;
        }

        if(args.length == 2){
            sendCommand(player.getName(), args[0], args[1]);

            player.sendMessage(new TextComponent("§a踢出成功!"));
            return;
        }

        if(args.length == 1){
            sendCommand(player.getName(), args[0], "无");

            player.sendMessage(new TextComponent("§a踢出成功!"));
        }
    }

    private void sendCommand(String admin, String name, String reason) {
        //操作者，封禁人，原因
        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> BungeeManagement.getInstance().getRedisBungeeAPI().sendChannelMessage("Management", "Kick|" + admin + "|" + name + "|" + reason));
    }

    @EventHandler
    public void onPartyMessage(PubSubMessageEvent evt) {
        if (!evt.getChannel().equals("Management")) {
            return;
        }

        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> {
            StringTokenizer in = new StringTokenizer(evt.getMessage(), "|");
            String action = in.nextToken();

            if (action.equals("Kick")) {
                String admin = in.nextToken();
                String name = in.nextToken();
                String reason = in.nextToken();

                if(BungeeManagement.getInstance().getProxy().getPlayer(name) != null){
                    BungeeManagement.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeManagement.getInstance().getProxy().getConsole(), "kick " + name + " §b" + BungeeManagement.getAdminController().getDatabase().getAdmin(admin).getDisplayName() + "§f|" + reason);
                }

                BungeeManagement.getInstance().getProxy().broadcast(new TextComponent("§c全服踢出:§e" + name + "§c,操作者:§e" + BungeeManagement.getAdminController().getDatabase().getAdmin(admin).getDisplayName() + ",§c原因:§e" + reason));
            }
        });

    }
}
