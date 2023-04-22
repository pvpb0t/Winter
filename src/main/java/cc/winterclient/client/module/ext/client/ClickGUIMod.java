package cc.winterclient.client.module.ext.client;

import cc.winterclient.client.Winter;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import cc.winterclient.client.option.ext.EnumOption;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;


public class ClickGUIMod extends Module {

    public static ClickGUIMod instance;
    public Option<Boolean> isopen = new BoolOption("open", true, this, v->false);
    private List<String> modes = Arrays.asList("Default", "Rainbow", "Color");
    public Option<String> mode = new EnumOption("Mode", "Default", modes, this);
    public Option<Double> red = new DoubleOption("Red", 255D, 0D, 255D, this, true, v-> mode.getExact().equalsIgnoreCase("color"));
    public Option<Double> green = new DoubleOption("Green", 255D, 0D, 255D, this, true,v-> mode.getExact().equalsIgnoreCase("color"));
    public Option<Double> blue = new DoubleOption("Blue", 255D, 0D, 255D, this, true,v-> mode.getExact().equalsIgnoreCase("color"));

    public Option<Double> saturatiom = new DoubleOption("Saturation", 100D, 0D, 255D, this, true, v-> mode.getExact().equalsIgnoreCase("rainbow"));
    public Option<Double> brightness = new DoubleOption("Brightness", 255D, 0D, 255D, this, true,v-> mode.getExact().equalsIgnoreCase("rainbow"));


    public ClickGUIMod() {
        super("ClickGUI", Category.CLIENT, Keyboard.KEY_INSERT);
        instance = this;
    }

    @Override
    public void onEnable(){
        mc.displayGuiScreen(Winter.instance.newClickGUI);
        this.toggle();
    }



}
