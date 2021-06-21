package cc.i9mc.bungeemanagement.controllers;

import cc.i9mc.bungeemanagement.BungeeManagement;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AntiMOTDAttackController {
    @Getter
    private ConcurrentHashMap<String, Integer> countMap = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private int totalCount;

    public AntiMOTDAttackController(){
        BungeeManagement.getInstance().getProxy().getScheduler().schedule(BungeeManagement.getInstance(), () -> {
            countMap.clear();
            totalCount = 0;
        }, 5L, 5L, TimeUnit.SECONDS);
    }

    public void sendCommand(String ip, int count) {
        //IP,次数
        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> BungeeManagement.getInstance().getRedisBungeeAPI().sendChannelMessage("Management", "motd|" + ip + "|" + count));
    }
}
