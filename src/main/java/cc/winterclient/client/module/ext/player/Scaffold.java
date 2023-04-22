package cc.winterclient.client.module.ext.player;

import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {

    public Scaffold(){
        super("Scaffold", "Autoplace blocks under you cuh", Category.PLAYER, Keyboard.KEY_NONE,false);
    }

}
