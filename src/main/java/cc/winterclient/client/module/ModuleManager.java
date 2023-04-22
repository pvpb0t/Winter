package cc.winterclient.client.module;


import cc.winterclient.client.event.EventTarget;

import cc.winterclient.client.event.ext.scenarios.EventKey;
import cc.winterclient.client.event.ext.scenarios.EventLogin;
import cc.winterclient.client.module.ext.client.ClickGUIMod;
import cc.winterclient.client.module.ext.client.HUD;
import cc.winterclient.client.module.ext.combat.CrystalAura;
import cc.winterclient.client.module.ext.combat.KillAura;
import cc.winterclient.client.module.ext.exploit.*;
import cc.winterclient.client.module.ext.misc.AntiChinese;
import cc.winterclient.client.module.ext.misc.FakePlayer;
import cc.winterclient.client.module.ext.misc.Test;
import cc.winterclient.client.module.ext.movement.*;
import cc.winterclient.client.module.ext.player.AntiAim;
import cc.winterclient.client.module.ext.player.Freecam;
import cc.winterclient.client.module.ext.visual.Fullbright;
import cc.winterclient.client.module.ext.visual.Visuals;
import org.lwjgl.input.Keyboard;


import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> moduleList = new ArrayList<>();

    private final List<Module> toEnable = new ArrayList<>();


    public ModuleManager()
    {
        moduleList.add(new ClickGUIMod());
        moduleList.add(new Sprint());
        moduleList.add(new HUD());
        moduleList.add(new MountBypass());
        moduleList.add(new KillAura());
        moduleList.add(new Velocity());
        moduleList.add(new CrystalAura());
        moduleList.add(new FakeVanilla());
        moduleList.add(new Freecam());
        moduleList.add(new Fullbright());
        moduleList.add(new EntityDesync());
        moduleList.add(new Timer());
        moduleList.add(new Flight());
        moduleList.add(new Disabler());
        moduleList.add(new Test());
        moduleList.add(new FakePlayer());
        moduleList.add(new Fakelag());
        moduleList.add(new Ragebot());
        moduleList.add(new AntiChinese("AntiChinese", "Cancels when trying to proccess chinese lag string", Category.MISC, Keyboard.KEY_NONE, false));
        moduleList.add(new Visuals());
        moduleList.add(new Speed());
        moduleList.add(new AntiAim());
        moduleList.add(new IdealTick());
    }

    @EventTarget
    public void onKey(EventKey e)
    {
        for(Module module: moduleList){
            if(module.getKey() == e.getKey())
                module.toggle();
        }
    }

    //Made this, cuz when enabling them when not in world can cause crashes
    //It will enable the modules on login and not on client startup
    @EventTarget
    public void onLogin(EventLogin e){
        for(Module module: moduleList){
            if(toEnable.contains(module)){
                module.setEnabled(true);
            }
        }
        toEnable.clear();
    }

    public ArrayList<Module> getModules(Category category)
    {
        ArrayList<Module> mods = new ArrayList<>();

        for (Module module : moduleList)
        {
            if (module.getCategory().equals(category)) mods.add(module);
        }

        return mods;
    }

    public int getAmmount(Category category)
    {
        int count = 0;
        for(Module module : getModules(category)){
            count++;
        }
        return count;
    }




    public List<Module> getModuleList() {
        return moduleList;
    }

    public List<Module> getToEnable() {
        return toEnable;
    }

    public Module getModuleByName(String name) {
        for(Module module: moduleList){
            if(module.getName().equalsIgnoreCase(name)){
                return module;
            }
        }
        return null;
    }
}
