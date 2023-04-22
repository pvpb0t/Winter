package cc.winterclient.client.ui.gui;



import cc.winterclient.client.ui.gui.elements.GuiObject;
import cc.winterclient.client.ui.gui.elements.ModuleButton;
import cc.winterclient.client.ui.gui.elements.Panel;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.ext.client.ClickGUIMod;
import cc.winterclient.client.util.ColorUtil;
import cc.winterclient.client.util.animations.Animation;
import cc.winterclient.client.util.animations.ColorAnimation;
import cc.winterclient.client.util.animations.Easings;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public class NewClickGUI extends GuiScreen {
    public Animation animation = new Animation();
    public ColorAnimation colorAnimation = new ColorAnimation();


    //@TODO add like kinda shifting colors to darker/lighter
    private static Color getColor(Category c){
        switch (c){
            case COMBAT:
                return new Color(250, 32, 32);
            case EXPLOIT:
                return new Color(117, 0, 181);
            case PLAYER:
                return new Color(63, 168, 197);
            case VISUAL:
                return new Color(31, 145, 6);
            case MOVEMENT:
                return new Color( 0, 66, 181);
            case CLIENT:
                return new Color(250, 134, 32);
            case MISC:
                return new Color(232, 202, 35);

        }
   return Color.BLACK; }

    private ArrayList<cc.winterclient.client.ui.gui.elements.Panel> panels = new ArrayList<>();
    private Integer space = 0;

    public NewClickGUI(){
        for(Category category : Category.values()){
            panels.add(new cc.winterclient.client.ui.gui.elements.Panel(6+space, 6, 105, 15, category, getColor(category)));
            space += 135;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        animation.update();
        animation.setValue(height/5);
       animation.animate(height/5, 600, Easings.CUBIC_OUT);



        for(cc.winterclient.client.ui.gui.elements.Panel panel : panels){
            Color newcol = Color.black;

            switch (ClickGUIMod.instance.mode.getExact()){
                case "Default":
                    newcol = getColor(panel.getCategory());
                    break;
                case "Rainbow":
                    newcol = ColorUtil.rainbow(10);
                    break;
                case "Color":
                    newcol = new Color(ClickGUIMod.instance.red.getExact().intValue(), ClickGUIMod.instance.green.getExact().intValue(), ClickGUIMod.instance.blue.getExact().intValue());
                    break;
            }

            panel.setColor(newcol);
            panel.draw(mouseX, mouseY);
        }



        //Logger.getLogger().print(String.valueOf(animation.getValue()));

        this.drawGradient(0, (int) (height- animation.getValue()), width, height, new Color(1, 1, 1, 0).getRGB(), Color.black.getRGB());
    }

    public void drawGradient(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        this.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton > 2) return;
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            public void run() {
                for(cc.winterclient.client.ui.gui.elements.Panel p : panels){
                    p.mouse(mouseX, mouseY, MouseClick.valueOf(mouseButton), MouseClickType.CLICK);
                    for(ModuleButton m : p.getModuleButtons()){
                        m.mouse(mouseX, mouseY, MouseClick.valueOf(mouseButton), MouseClickType.CLICK);
                        for(GuiObject object : m.getSettingsButton()){
                            object.mouse(mouseX, mouseY, MouseClick.valueOf(mouseButton), MouseClickType.CLICK);
                        }
                    }
                }
            }
        });

        executorService.shutdown();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            public void run() {
                for(cc.winterclient.client.ui.gui.elements.Panel p : panels){
                    p.key(typedChar, keyCode);
                    for(ModuleButton m : p.getModuleButtons()){
                        m.key(typedChar, keyCode);
                        for(GuiObject object : m.getSettingsButton()){
                            object.key(typedChar, keyCode);
                        }
                    }
                }
            }
        });


        executorService.shutdown();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(state > 3) return;
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            public void run() {
                for(Panel p : panels){
                    p.mouse(mouseX, mouseY, MouseClick.valueOf(state), MouseClickType.RELEASE);
                    for(ModuleButton m : p.getModuleButtons()){
                        m.mouse(mouseX, mouseY, MouseClick.valueOf(state), MouseClickType.RELEASE);
                        for(GuiObject object : m.getSettingsButton()){
                            object.mouse(mouseX, mouseY, MouseClick.valueOf(state), MouseClickType.RELEASE);
                        }
                    }
                }
            }
        });

        executorService.shutdown();
    }
}



