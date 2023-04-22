package cc.winterclient.client.ui.gui.elements.option;


import cc.winterclient.client.Winter;
import cc.winterclient.client.font.FontUtil;
import cc.winterclient.client.ui.gui.MouseClick;
import cc.winterclient.client.ui.gui.MouseClickType;
import cc.winterclient.client.ui.gui.elements.GuiObject;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.StringOption;
import cc.winterclient.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ChatAllowedCharacters;

import java.awt.*;
/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public class StringButton extends GuiObject {
    private StringOption option;

    private Module module;
    private boolean listening;
    private String value;
    private String oldval;


    public StringButton(float x, float y, float width, float height, Module module, Color color, Option option) {
        super(x, y, width, height, option, color);
        this.module = module;
        this.option = (StringOption) option;
        oldval = ((StringOption) option).getExact();
        value = oldval;
    }

    @Override
    public void draw(int mouseX, int mouseY){
        RenderUtil.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(),  listening? new Color(24, 26, 23).brighter().getRGB() : new Color(24, 26, 23).getRGB());

        Winter.getCustomFontRenderer().drawStringWithShadow(option.getName() + ":  {" + (listening ? value : option.getExact()) + "}", getX()+4, getY()+ FontUtil.getFontHeight()/2-2, 0xffffffff);


    }


    @Override
    public void mouse(int mouseX, int mouseY, MouseClick mouseClick, MouseClickType mouseClickType) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseClick.equals(MouseClick.LEFTCLICK)) {
                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 0.5f));
                    listening = !listening;
                }
            }
        }
    }


    @Override
    public void key(char typedChar, int keyCode) {
        if(listening){
            switch (keyCode) {
                case 1:
                case 28: {
                    if(value.isEmpty()){
                        option.setExact(oldval);
                    }else {
                        option.setExact(value);
                    }
                    listening = false;
                    return;
                }//ENTER
                case 14: {
                    //DELETE
                    value = StringButton.removeLastChar(value);
                }
            }
            if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                value += typedChar;
            }
        }
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

}
