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

public class BanCommand extends Command implements Listener {
    public BanCommand(String name) {
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

        if(BungeeManagement.getAdminController().getDatabase().getBanReason(args[0]) != null){
            player.sendMessage(new TextComponent("NMSL"));
            return;
        }

        if(args.length == 3){
            if(!args[1].toLowerCase().matches("[1-9][0-9]*([wdhmsy]|mon)")){
                player.sendMessage(new TextComponent("§c格式错误!"));
                return;
            }

            sendCommand(player.getName(), args[0], args[1], args[2]);

            player.sendMessage(new TextComponent("§a封禁成功!"));
            return;
        }

        if(args.length == 2){
            if(args[1].toLowerCase().matches("[1-9][0-9]*([wdhmsy]|mon)")){
                sendCommand(player.getName(), args[0], args[1], "无");
                player.sendMessage(new TextComponent("§a封禁成功!"));
                return;
            }else {
                sendCommand(player.getName(), args[0], args[1]);

                player.sendMessage(new TextComponent("§a封禁成功!"));
                return;
            }
        }

        if(args.length == 1){
            sendCommand(player.getName(), args[0], "无");

            player.sendMessage(new TextComponent("§a封禁成功!"));
        }
    }

    private void sendCommand(String admin, String name, String reason) {
        //操作者，封禁人，原因
        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> BungeeManagement.getInstance().getRedisBungeeAPI().sendChannelMessage("Management", "Ban|" + admin + "|" + name + "|" + reason));
    }

    private void sendCommand(String admin, String name, String date, String reason) {
        //操作者，封禁人，时间，解释
        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> BungeeManagement.getInstance().getRedisBungeeAPI().sendChannelMessage("Management", "TempBan|" + admin + "|" + name + "|" + date + "|" + reason));
    }

    @EventHandler
    public void onPartyMessage(PubSubMessageEvent evt) {
        if (!evt.getChannel().equals("Management")) {
            return;
        }

        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> {
            StringTokenizer in = new StringTokenizer(evt.getMessage(), "|");
            String action = in.nextToken();

            if (action.equals("Ban")) {
                String admin = in.nextToken();
                String name = in.nextToken();
                String reason = in.nextToken();

                BungeeManagement.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeManagement.getInstance().getProxy().getConsole(), "ban " + name + " §b" + BungeeManagement.getAdminController().getDatabase().getAdmin(admin).getDisplayName() + "§f|" + reason);


                BungeeManagement.getInstance().getProxy().broadcast(new TextComponent("§c全服封禁:§e" + name + "§c,操作者:§e" + BungeeManagement.getAdminController().getDatabase().getAdmin(admin).getDisplayName() + ",§c原因:§e" + reason));
                return;
            }

            if (action.equals("TempBan")) {
                String admin = in.nextToken();
                String name = in.nextToken();
                String date = in.nextToken();
                String reason = in.nextToken();

                BungeeManagement.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeManagement.getInstance().getProxy().getConsole(), "tempban " + name + " §b" + BungeeManagement.getAdminController().getDatabase().getAdmin(admin).getDisplayName() + "§f|" + reason + " " + date);

                BungeeManagement.getInstance().getProxy().broadcast(new TextComponent("§c全服封禁:§e" + name + "§c,操作者:§e" + BungeeManagement.getAdminController().getDatabase().getAdmin(admin).getDisplayName() + ",§c原因:§e" + reason));
            }
        });

    }
}
