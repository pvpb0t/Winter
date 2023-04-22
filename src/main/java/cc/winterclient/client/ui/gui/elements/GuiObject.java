package cc.winterclient.client.ui.gui.elements;


import cc.winterclient.client.ui.gui.MouseClick;
import cc.winterclient.client.ui.gui.MouseClickType;
import cc.winterclient.client.option.Option;

import java.awt.*;

/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public abstract class GuiObject implements IElement{
    private Option option;
    private Color color;

    private float x, y, width, height;

    public GuiObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Option getOption() {
        return option;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public GuiObject(float x, float y, float width, float height, Option option, Color color1) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.option = option;
        this.color = color1;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    public void draw(int mouseX, int mouseY) {

    }


    @Override
    public void key(char typedChar, int keyCode) {

    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public void mouse(int mouseX, int mouseY, MouseClick mouseClick, MouseClickType mouseClickType) {

    }

}
