package cc.winterclient.client.ui.gui.elements.option;

import cc.winterclient.client.font.FontUtil;
import cc.winterclient.client.ui.gui.MouseClickType;
import cc.winterclient.client.ui.gui.elements.GuiObject;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.Winter;
import cc.winterclient.client.ui.gui.MouseClick;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.util.RenderUtil;
import cc.winterclient.client.util.animations.Animation;
import cc.winterclient.client.util.animations.Easings;

import java.awt.*;
/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public class BoolButton extends GuiObject {

    private Module module;
    private Option option;

    public BoolButton(float x, float y, float width, float height, Module module, Color color, Option option) {
        super(x, y, width, height, option, color);
        this.module = module;
        this.option = option;
    }


    @Override
    public void draw(int mouseX, int mouseY){
        RenderUtil.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(),0xff181A17 );
        float xValue=0;
        if(option.getExact().equals(true)){
            xValue = getX()+ getWidth()-3;
        }else{
            xValue=0;
        }
        RenderUtil.drawRect(getX()+3, getY()+1f, xValue, getY()+ getHeight()-1f, getColor().getRGB());

        Winter.getCustomFontRenderer().drawStringWithShadow(option.getName(), getX()+4, getY()+ FontUtil.getFontHeight()/2-2, 0xffffffff);
        //FontUtil.drawString(option.getName(), 3 ,3, 0xffffffff);

    }


    @Override
    public void mouse(int mouseX, int mouseY, MouseClick mouseClick, MouseClickType mouseClickType) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseClick.equals(MouseClick.LEFTCLICK)) {
                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    boolean ison = (boolean) option.getExact();
                    option.setExact(!ison);
                }
            }
        }
        }

}
