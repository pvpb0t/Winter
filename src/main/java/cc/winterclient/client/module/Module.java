package cc.winterclient.client.module;

import cc.winterclient.client.event.EventManager;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class Module {

    private String name;
    private String displayName;
    private String description;

    private int key;
    private Category category;
    
    protected static Minecraft mc;
    
    private boolean enabled;
    private boolean hidden;


    public Module(String name, Category category, int key){
        this.name = name;
        this.displayName = name;
        this.category = category;
        this.key = key;
        this.enabled = false;
        hidden = false;
        this.mc = Minecraft.getMinecraft();
    }

    public Module(String name, String description, Category category, int key, Boolean hidden){
        this.name = name;
        this.displayName = name;
        this.category = category;
        this.key = key;
        this.enabled = false;
        this.hidden = hidden;
        this.description = description;
        this.mc = Minecraft.getMinecraft();
    }


    public String HudInfo(){
        return null;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Module(String name, Category category, int key, Boolean hidden){
        this.name = name;
        this.displayName = name;
        this.category = category;
        this.key = key;
        this.enabled = false;
        this.hidden = hidden;
        this.mc = Minecraft.getMinecraft();
    }

    public void toggle(){
        enabled = !enabled;
        
        if(enabled)
            onEnable();
        else 
            onDisable();
    }
    public boolean nullCheck() {
        return mc.world == null || mc.player == null;
    }

    public void onDisable() {
        EventManager.unregister(this);
    }

    public void onEnable() {
        EventManager.register(this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if(enabled)
            onEnable();
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getKey() {
        return key;
    }

    public Category getCategory() {
        return category;
    }
}
