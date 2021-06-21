package cc.i9mc.bungeemanagement.admin.command;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.admin.database.AdminData;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

public class QueryCommand extends Command {
    public QueryCommand(String name) {
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

        if(args.length == 1){
            player.sendMessage(new TextComponent("§a原因: §e" + BungeeManagement.getAdminController().getDatabase().getBanReason(args[0])));
            long date = BungeeManagement.getAdminController().getDatabase().getBanUntil(args[0]);
            if(date == -1){
                player.sendMessage(new TextComponent("§c永久"));
            }else {
                long diff = date / 1000;

                if (diff > 60 * 60 * 24) {
                    player.sendMessage(new TextComponent("§a剩余: §e" + diff / 60 / 60 / 24 + "天 " + diff / 60 / 60 % 24 + "小时 " + diff / 60 % 60 + "分钟 " + diff % 60 + "秒"));
                } else if (diff > 60 * 60) {
                    player.sendMessage(new TextComponent("§a剩余: §e" + diff / 60 / 60 + "小时 " + diff / 60 % 60 + "分钟 " + diff % 60 + "秒"));
                } else if (diff > 60) {
                    player.sendMessage(new TextComponent("§a剩余: §e" + diff / 60 + "分钟 " + diff % 60 + "秒"));
                } else {
                    player.sendMessage(new TextComponent("§a剩余: §e" + diff + "秒"));
                }
            }
        }
    }
}
