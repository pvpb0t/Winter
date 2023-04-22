package cc.winterclient.client.module.ext.misc;

import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.scenarios.EventLogout;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.util.ChatUtil;
import cc.winterclient.client.event.ext.scenarios.EventLogin;

public class Test extends Module {
    public Test() {
        super("Test", Category.MISC, 0);
    }

    @EventTarget
    public void onLogin(EventLogin eventLogin){
        System.out.println("Login event works! Bonkers!");
    }
    @EventTarget
    public void onLogout(EventLogout eventLogin){
        ChatUtil.sendMessage("Logout event works! Bonkers!");
        System.out.println("Logout event works! Bonkers!");
    }

}
