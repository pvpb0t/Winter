package cc.winterclient.client.command.ext;

import cc.winterclient.client.Winter;
import cc.winterclient.client.command.Command;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.util.ChatUtil;

public class Toggle extends Command {

    public Toggle() {
        super("toggle", "Toggle a module", "t");
    }

    @Override
    public void execute(String[] args) {

        if (args.length > 0)
        {
            Module module = Winter.instance.moduleManager.getModuleByName(args[0]);

            if (module == null) {
                ChatUtil.sendMessage("Module not found!");
            } else {
                module.toggle();
                ChatUtil.sendMessage(module.getName() + " was " + (module.isEnabled() ? "enabled" : "disabled") + ".");
            }
        }
        else
        {
            ChatUtil.sendMessage("Usage: " + Winter.clientPrefix + "toggle <Module>");
        }
    }
}
