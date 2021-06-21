package cc.i9mc.bungeemanagement.admin.command;

import cc.i9mc.bungeemanagement.BungeeManagement;
import cc.i9mc.bungeemanagement.admin.AdminType;
import cc.i9mc.bungeemanagement.admin.database.AdminData;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.StringTokenizer;

public class UnBanCommand extends Command implements Listener {
    public UnBanCommand(String name) {
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

        if(adminData.getAdminType() == AdminType.COMMONADMIN){
            return;
        }

        if(args.length == 0){
            player.sendMessage(new TextComponent("§c格式错误!"));
            return;
        }

        if(args.length == 1){
            sendCommand(player.getName(), args[0]);

            player.sendMessage(new TextComponent("§a解封成功!"));
        }
    }

    private void sendCommand(String admin, String name) {
        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> BungeeManagement.getInstance().getRedisBungeeAPI().sendChannelMessage("Management", "UnBan|" + admin + "|" + name));
    }

    @EventHandler
    public void onPartyMessage(PubSubMessageEvent evt) {
        if (!evt.getChannel().equals("Management")) {
            return;
        }

        BungeeManagement.getInstance().getProxy().getScheduler().runAsync(BungeeManagement.getInstance(), () -> {
            StringTokenizer in = new StringTokenizer(evt.getMessage(), "|");
            String action = in.nextToken();

            if (action.equals("UnBan")) {
                String admin = in.nextToken();
                String name = in.nextToken();

                BungeeManagement.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeManagement.getInstance().getProxy().getConsole(), "unban " + name);

                BungeeManagement.getInstance().getProxy().broadcast(new TextComponent("§c全服解封:§e" + name + "§c,操作者:§e" + BungeeManagement.getAdminController().getDatabase().getAdmin(admin).getDisplayName()));
                /*try {
                    sendMsg("全服解封:" + name + ",操作者:" + BungeeManagement.getAdminController().getDatabase().getAdmin(admin).getDisplayName());
                } catch (ApiException e) {
                    e.printStackTrace();
                }*/
            }
        });

    }

    /*private static String sendMsg(String content) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=ee8b2751be099169da13d275c4b8c2a983c9b9029f4b0fb53043ab4694425501");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);

        OapiRobotSendResponse response = client.execute(request);

        return response.getErrmsg();
    }*/
}
