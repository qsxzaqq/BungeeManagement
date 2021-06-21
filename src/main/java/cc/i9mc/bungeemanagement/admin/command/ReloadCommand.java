package cc.i9mc.bungeemanagement.admin.command;

import cc.i9mc.bungeemanagement.BungeeManagement;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {
    public ReloadCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)){
            BungeeManagement.getAdminController().getDatabase().reloadAdmins();
            sender.sendMessage(new TextComponent("OK"));
        }
    }
}
