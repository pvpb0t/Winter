package cc.winterclient.client.command.ext;

import cc.winterclient.client.command.Command;
import cc.winterclient.client.Winter;
import cc.winterclient.client.util.ChatUtil;

public class Friend extends Command {
    public Friend() {
        super("friend", "Add someone as friend. There is a problem: You have no friends", "f");
    }

    @Override
    public void execute(String[] args) {
        if(args.length > 0){
            switch (args[0].toLowerCase()){
                case "add":{
                    if(args.length >1){
                        if(Winter.instance.friendManager.addFriend(args[1])){
                            ChatUtil.sendMessage("Added " + args[1] + " as a friend!");
                        }
                        else {
                            ChatUtil.sendMessage("Error adding friend!");
                        }
                    }
                }break;
                case "remove":{
                    if(args.length >1){
                        if(Winter.instance.friendManager.removeFriend(args[1])){
                            ChatUtil.sendMessage("Removed " + args[1] + " from friends");
                        }
                        else {
                            ChatUtil.sendMessage("Error removing friend!");
                        }
                    }
                }break;
                case "list":{
                        for(String s: Winter.instance.friendManager.getFriends()){
                            ChatUtil.sendMessage(s);
                        }
                }break;
            }
        }
    }
}
