package cc.i9mc.bungeemanagement;

import cc.i9mc.bungeemanagement.commands.DtCommand;
import cc.i9mc.bungeemanagement.commands.HubCommand;
import cc.i9mc.bungeemanagement.commands.LobbyCommand;
import cc.i9mc.bungeemanagement.commands.ServerLoadCommand;
import cc.i9mc.bungeemanagement.controllers.*;
import cc.i9mc.bungeemanagement.listeners.*;
import cc.i9mc.gameutils.BungeeGameUtils;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeManagement extends Plugin {
    @Getter
    private RedisBungeeAPI redisBungeeAPI;

    @Getter
    private static BungeeManagement instance;

    @Getter
    private static AdminController adminController;

    @Getter
    private static BungeeConfigController bungeeConfigController;

    @Getter
    private static AntiMOTDAttackController antiMOTDAttackController;

    @Getter
    private static FakePlayerController fakePlayerController;

    @Getter
    private static LobbyController lobbyController;

    @Getter
    private static GameServerController gameServerController;

    @Getter
    private static ChatController chatController;

    @Getter
    private static PingListener pingListener;

    @Override
    public void onEnable() {
        instance = this;
        redisBungeeAPI = RedisBungee.getApi();

        redisBungeeAPI.registerPubSubChannels("Management");

        redisBungeeAPI.registerPubSubChannels("ServerManage.GameServerNameQuery");
        redisBungeeAPI.registerPubSubChannels("GameServerACK");
        redisBungeeAPI.registerPubSubChannels("Send");

        BungeeGameUtils.getInstance().getConnectionPoolHandler().registerDatabase("bungee");
        BungeeGameUtils.getInstance().getConnectionPoolHandler().registerDatabase("ban");
        BungeeGameUtils.getInstance().getConnectionPoolHandler().registerDatabase("playerdata");

        getProxy().getPluginManager().registerCommand(this, new ServerLoadCommand());
        getProxy().getPluginManager().registerCommand(this, new DtCommand());
        getProxy().getPluginManager().registerCommand(this, new HubCommand());
        getProxy().getPluginManager().registerCommand(this, new LobbyCommand());

        pingListener = new PingListener(this);
        new CheatListener(this);
        //new MuteListener(this);
        new ConnectListener(this);

        adminController = new AdminController(this);
        bungeeConfigController = new BungeeConfigController();
        antiMOTDAttackController = new AntiMOTDAttackController();
        lobbyController = new LobbyController();

        gameServerController = new GameServerController();
        //fakePlayerController = new FakePlayerController();

        chatController = new ChatController();

        new ServerRestartController();
    }

    @Override
    public void onDisable() {
        lobbyController.close();
        fakePlayerController.close();
        // Plugin shutdown logic
    }
}
