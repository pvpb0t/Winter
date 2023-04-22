package cc.winterclient.client.ui.gui.elements.option;

import cc.winterclient.client.font.FontUtil;
import cc.winterclient.client.ui.gui.elements.GuiObject;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.Winter;
import cc.winterclient.client.ui.gui.MouseClick;
import cc.winterclient.client.ui.gui.MouseClickType;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.EnumOption;
import cc.winterclient.client.util.RenderUtil;

import java.awt.*;
/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public class EnumButton extends GuiObject {

    private EnumOption option;
    private int selected = 0;
    public EnumButton(float x, float y, float width, float height, Module module, Color color, Option option) {
        super(x, y, width, height, option, color);
        this.option = (EnumOption) option;
    }

    //pvpb0t you might change this rendering, cuz I literally just pasted it from BoolButton
    //and you know I'm shit with making anything look good.
    @Override
    public void draw(int mouseX, int mouseY){
        RenderUtil.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(),0xff181A17 );
        Winter.getCustomFontRenderer().drawStringWithShadow(option.getName() + ":  [" + option.getExact() + "]", getX()+4, getY()+ FontUtil.getFontHeight()/2-2, 0xffffffff);
    }

    @Override
    public void mouse(int mouseX, int mouseY, MouseClick mouseClick, MouseClickType mouseClickType) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseClick.equals(MouseClick.LEFTCLICK)) {
                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    selected++;
                    if(selected > option.getEnums().size() - 1){
                        selected = 0;
                    }

                    option.setExact(option.getEnums().get(selected));

                }
            }
        }
    }

}
