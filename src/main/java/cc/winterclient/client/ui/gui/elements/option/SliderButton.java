package cc.winterclient.client.ui.gui.elements.option;

import cc.winterclient.client.font.FontUtil;
import cc.winterclient.client.ui.gui.elements.GuiObject;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.ui.gui.MouseClick;
import cc.winterclient.client.ui.gui.MouseClickType;
import cc.winterclient.client.Winter;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.DoubleOption;
import cc.winterclient.client.util.RenderUtil;
import cc.winterclient.client.util.math.MathUtil;

import java.awt.*;
/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public class SliderButton extends GuiObject {

    private Module module;private DoubleOption Doubleoption;


    private boolean drag;

    public SliderButton(float x, float y, float width, float height, Module module, Color color, Option option) {
        super(x, y, width, height, option, color);
        this.module = module;if(option instanceof DoubleOption){
            this.Doubleoption = (DoubleOption) option;
        }
    }

    @Override
    public void draw(int mouseX, int mouseY){
        RenderUtil.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(),0xff181A17 );

        Double radius= Double.valueOf(-99);
        Double percent = Double.valueOf(-99);
        Double min = Double.valueOf(-99);

        if(Doubleoption != null){
            radius = Doubleoption.getMax() - Doubleoption.getMin();
            percent = (Doubleoption.getExact() - Doubleoption.getMin()) / Doubleoption.getMax() - Doubleoption.getMin();
            min = Doubleoption.getMin();

        }

        if(radius != -99 && percent != -99){
            if(drag){
                double val = min + (MathUtil.clamp_double((double) (mouseX - getX()) / getWidth(), 0, 1)) * radius;
                if(Doubleoption.getRounded()){
                    Doubleoption.setExact((double) Math.round( Math.round(val * 100D)/100D));
                }else{
                    Doubleoption.setExact(Math.round(val * 100D)/ 100D);

                }
            }
            // Logger.getLogger().print(String.valueOf(Doubleoption.getExact()));
            RenderUtil.drawRect(getX()+3, getY()+1f, (float) (getX()+ (getWidth()*percent)-3), getY()+ getHeight()-1f, getColor().getRGB());
            Winter.getCustomFontRenderer().drawStringWithShadow(Doubleoption.getName() + " [" + Doubleoption.getExact() + "]", getX()+4, getY()+ FontUtil.getFontHeight()/2-2, 0xffffffff);

        }

    }


    @Override
    public void mouse(int mouseX, int mouseY, MouseClick mouseClick, MouseClickType mouseClickType) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseClick.equals(MouseClick.LEFTCLICK)) {
                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    drag = true;
                    //   m1 = getX()-mouseX;
                    //    m2 = getY()-mouseY;

                } else if (mouseClickType.equals(MouseClickType.RELEASE)){

                    drag = false;

                }
            }
        }else {
            drag = false;
        }
    }
}