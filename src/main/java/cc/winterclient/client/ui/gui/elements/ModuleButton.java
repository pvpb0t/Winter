package cc.winterclient.client.ui.gui.elements;



import cc.winterclient.client.Winter;
import cc.winterclient.client.font.FontUtil;
import cc.winterclient.client.ui.gui.MouseClick;
import cc.winterclient.client.ui.gui.MouseClickType;
import cc.winterclient.client.ui.gui.elements.option.BoolButton;
import cc.winterclient.client.ui.gui.elements.option.EnumButton;
import cc.winterclient.client.ui.gui.elements.option.SliderButton;
import cc.winterclient.client.ui.gui.elements.option.StringButton;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.OptionManager;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import cc.winterclient.client.option.ext.EnumOption;
import cc.winterclient.client.option.ext.StringOption;
import cc.winterclient.client.util.RenderUtil;
import cc.winterclient.client.util.animations.Animation;
import cc.winterclient.client.util.animations.Easings;

import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public class ModuleButton extends GuiObject{

    private Module module;
    private Color color;
    public final Animation animation = new Animation();
    private boolean last;
    private boolean listening;

    private boolean open;
    private int OptionCount;
    public float finalHeight;

    private ArrayList<GuiObject> settingsButton = new ArrayList<>();


    public ModuleButton(float x, float y, float width, float height, Module module, Color categoryColor, boolean last) {
        super(x, y, width, height);
        this.module = module;
        this.color = categoryColor;
        this.last = last;
        listening= false;
        open= false;
        this.OptionCount = 0;
        float optionY = getY() + getHeight();
        for(Option option : OptionManager.getOptions(module)){
            float optionStartY = optionY + 18*OptionCount;
                if (option instanceof BoolOption) {
                    settingsButton.add(new BoolButton(getX() + 0.5f, optionStartY, getWidth() - 0.5f, optionStartY + getHeight(), module, color, option));

                } else if (option instanceof EnumOption) {
                    settingsButton.add(new EnumButton(getX() + 0.5f, optionStartY, getWidth() - 0.5f, optionStartY + getHeight(), module, color, option));

                } else if (option instanceof StringOption) {
                    settingsButton.add(new StringButton(getX() + 0.5f, optionStartY, getWidth() - 0.5f, optionStartY + getHeight(), module, color, option));

                } else if (option instanceof DoubleOption) {
                    settingsButton.add(new SliderButton(getX() + 0.5f, optionStartY, getWidth() - 0.5f, optionStartY + getHeight(), module, color, option));


            }
            //Logger.getLogger().print("Adding option " + option.getName() + " to module : " + module.getName() );
            OptionCount++;
        }

    }


    @Override
    public void draw(int mouseX, int mouseY) {
        animation.update();
        int count=0;
        if(module.isEnabled()){
            animation.setValue(55);
            animation.animate(55, 600, Easings.CUBIC_OUT);
        }else {
            animation.setAnimationFromValue(55);
            animation.setValue(0);
            animation.animate(0, 600, Easings.CUBIC_OUT);

        }

        RenderUtil.drawRect(getX(), getY(), getX() +getWidth(), getY() + getHeight(), color.getRGB());
        RenderUtil.drawRect(getX(),  getY()+0.5f, getX() +getWidth(), !last ? getY() + getHeight()-0.5f : getY() + getHeight() , new Color(77, 77, 77, (int) (255-animation.getValue())).darker().getRGB());
        Winter.getCustomFontRenderer().drawStringWithShadow(module.getName(), getX() + 2, getY() + FontUtil.getFontHeight()/2-2,Color.WHITE.getRGB());
        String text = (module.getKey() == Keyboard.KEY_NONE ? "None" : Keyboard.getKeyName(module.getKey()));
        String text2 = listening ? "Listening" : text;

        Winter.getCustomFontRenderer().drawStringWithShadow( "["+text2 +"]", getX() +getWidth() - FontUtil.getStringWidth(text2) - 9, getY() + FontUtil.getFontHeight()/2-2,Color.WHITE.getRGB());


        float temp = 0;
        if(open){
            //Logger.getLogger().print(module.getName() + " IS OPEN");
            for(GuiObject settingButton : settingsButton){
                if(!settingButton.getOption().isShown())
                    continue;
                settingButton.setHeight(13);
                settingButton.setColor(this.color);
                settingButton.setX(getX()+1f);
                settingButton.setY(getY() + getHeight() + (settingButton.getHeight()*count));
                settingButton.setWidth(getWidth()-1f);
                settingButton.draw(mouseX, mouseY);
                count++;

                temp = settingButton.getHeight();
            }



        }

        finalHeight = temp*count +this.getHeight();

    }

    @Override
    public void mouse(int mouseX, int mouseY, MouseClick mouseClick, MouseClickType mouseClickType) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseClick.equals(MouseClick.RIGHTCLICK)) {
                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    open = !open;
                }
            } else if (mouseClick.equals(MouseClick.LEFTCLICK)) {

                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    module.toggle();
                }
            }else if(mouseClick.equals(MouseClick.MIDDLECLICK)){
                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    listening = !listening;
                }
            }
        }


    }
    @Override
    public void key(char typedChar, int keyCode) {
        if(listening){
            if (keyCode != Keyboard.KEY_ESCAPE && keyCode != Keyboard.KEY_BACK) {
                //Client.sendChatMessage("Bound '" + mod.getName() + "'" + " to '" + Keyboard.getKeyName(keyCode) + "'");
                module.setKey(keyCode);
            } else {
                module.setKey(Keyboard.KEY_NONE);
            }
            listening = false;
        }
    }

    public ArrayList<GuiObject> getSettingsButton() {
        return settingsButton;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}