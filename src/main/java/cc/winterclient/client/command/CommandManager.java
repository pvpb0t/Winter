package cc.winterclient.client.command;

import cc.winterclient.client.command.ext.Toggle;
import cc.winterclient.client.event.ext.scenarios.EventSendChat;
import cc.winterclient.client.Winter;
import cc.winterclient.client.command.ext.Friend;
import cc.winterclient.client.command.ext.Hclip;
import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.util.ChatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private List<Command> commandList = new ArrayList<>();


    public CommandManager(){
        commandList.add(new Toggle());
        commandList.add(new Friend());
        commandList.add(new Hclip());
    }


    @EventTarget
    public void onChat(EventSendChat event)
    {
        String[] args = event.getMessage().split(" ");
        if(args[0].startsWith(Winter.clientPrefix)){
            event.setCancelled(true);
            String raw = event.getMessage();
            int length = 1;
            args[0] = args[0].substring(Winter.clientPrefix.length());
            if(args.length > 1){
                length = 2;
            }
            raw = raw.substring(args[0].length() + length);
            String[] arg = raw.split(" ");
            System.out.println(raw);
            for(Command command: commandList) {
                if(Arrays.asList(command.getAliases()).contains(args[0].toLowerCase()) || command.getName().equalsIgnoreCase(args[0])){
                    command.execute(arg);
                    return;
                }
            }
            ChatUtil.sendMessage("Command not found.");
        }
    }

}
