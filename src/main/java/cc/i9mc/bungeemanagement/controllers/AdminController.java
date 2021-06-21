package cc.i9mc.bungeemanagement.controllers;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.admin.command.*;
import cc.i9mc.bungeemanagement.admin.database.Database;
import lombok.Getter;

public class AdminController {
    @Getter
    private Database database;

    public AdminController(BungeeManagement bungeeManagement){
        bungeeManagement.getProxy().getPluginManager().registerCommand(bungeeManagement, new BanCommand("eban"));
        bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, new BanCommand("eban"));
        bungeeManagement.getProxy().getPluginManager().registerCommand(bungeeManagement, new KickCommand("ekick"));
        bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, new KickCommand("ekick"));
        //bungeeManagement.getProxy().getPluginManager().registerCommand(bungeeManagement, new MuteCommand("emute"));
        //bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, new MuteCommand("emute"));
        bungeeManagement.getProxy().getPluginManager().registerCommand(bungeeManagement, new QueryCommand("equery"));
        bungeeManagement.getProxy().getPluginManager().registerCommand(bungeeManagement, new ReloadCommand("ar"));
        bungeeManagement.getProxy().getPluginManager().registerCommand(bungeeManagement, new UnBanCommand("eunban"));
        bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, new UnBanCommand("eunban"));
        //bungeeManagement.getProxy().getPluginManager().registerCommand(bungeeManagement, new UnMuteCommand("eunmute"));
        //bungeeManagement.getProxy().getPluginManager().registerListener(bungeeManagement, new UnMuteCommand("eunmute"));

        database = new Database();
    }
}
