package cc.i9mc.bungeemanagement.commands;

import cc.i9mc.bungeemanagement.BungeeManagement;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerLoadCommand extends Command {

    public ServerLoadCommand() {
        super("serverload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            if(args.length < 1){
                //GAMESERVER
                sender.sendMessage(new TextComponent("/serverload MOTD/HUB/SERVER/ALL！！！"));
                return;
            }

            reloadType(ReloadType.valueOf(args[0].toUpperCase()));
            sender.sendMessage(new TextComponent("Load request sent!"));
        }
    }

    private void reloadType(ReloadType type){
        switch (type){
            case MOTD:
                BungeeManagement.getPingListener().loadMOTD();
                return;
            case HUB:
                //BungeeManagement.getLobbyController().loadGroupServers();
                BungeeManagement.getLobbyController().loadRegexGroup();
                return;
            case SERVER:
                BungeeManagement.getBungeeConfigController().loadServers();
                return;
            case GAMESERVER:
                BungeeManagement.getGameServerController().loadServers();
                return;
            case CHAT:
                BungeeManagement.getChatController().loadChats();
                return;
            case ALL:
                for(ReloadType reloadType : ReloadType.values()){
                    reloadType(reloadType);
                }
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private enum ReloadType {
        MOTD, HUB, SERVER, GAMESERVER, CHAT, ALL
    }
}
