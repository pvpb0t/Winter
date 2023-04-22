package cc.winterclient.client.ui.gui.elements;

import cc.winterclient.client.ui.gui.MouseClick;
import cc.winterclient.client.ui.gui.MouseClickType;

/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public interface IElement {

    public void draw(int mouseX, int mouseY);
    public void mouse(int mouseX, int mouseY, MouseClick mouseClick, MouseClickType mouseClickType);
    public void key(char typedChar, int keyCode);

}
