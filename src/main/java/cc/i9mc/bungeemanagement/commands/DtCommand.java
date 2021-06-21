package cc.i9mc.bungeemanagement.commands;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeequeue.data.QueueRequest;
import cc.i9mc.gameutils.utils.JedisUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Map;

public class DtCommand extends Command {
    public DtCommand() {
        super("dt");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (player.getServer().getInfo().getName().contains("Auth")) {
            return;
        }

        if(BungeeManagement.getLobbyController().getHubCommand().contains(player.getName())){
            BungeeManagement.getLobbyController().getHubCommand().remove(player.getName());
            return;
        }

        player.sendMessage(new TextComponent("§e您将被传送至大厅，如需取消请在3秒内再次输入 /dt"));
        BungeeManagement.getLobbyController().getHubCommand().add(player.getName());
        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!BungeeManagement.getLobbyController().getHubCommand().contains(player.getName())){
                return;
            }

            BungeeManagement.getLobbyController().getHubCommand().remove(player.getName());

            BungeeManagement.getLobbyController().lobby(player);
        });
    }
}
