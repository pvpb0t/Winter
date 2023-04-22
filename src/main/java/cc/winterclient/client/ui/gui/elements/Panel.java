package cc.winterclient.client.ui.gui.elements;


import cc.winterclient.client.Winter;
import cc.winterclient.client.font.FontUtil;
import cc.winterclient.client.ui.gui.MouseClick;
import cc.winterclient.client.ui.gui.MouseClickType;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.module.ext.client.ClickGUIMod;
import cc.winterclient.client.util.RenderUtil;
import cc.winterclient.client.util.animations.Animation;


import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public class Panel extends GuiObject{

    private Category category;
    private Color color;
    private boolean open;
    private boolean drag;
    private boolean stopFirstAnim;

    public Color getColor() {
        return color;
    }

    public Animation animation = new Animation();

    public boolean isDrag() {
        return drag;
    }

    private Integer moduleCount;
    private float lastY;
    int yVal = 0;


    private float  m1, m2;
    public boolean isOpen() {
        return open;
    }

    public Category getCategory() {
        return category;
    }

    private ArrayList<Module> modules = new ArrayList<>();

    public ArrayList<ModuleButton> getModuleButtons() {
        return moduleButtons;
    }

    private ArrayList<ModuleButton> moduleButtons = new ArrayList<>();


    public Panel(float x, float y, float width, float height, Category category, Color color) {
        super(x, y, width, height);
        this.category = category;
        this.color = color;
        this.moduleCount = 0;
        m1 = m2 = 0;
        lastY=0;
        stopFirstAnim=false;
        if(ClickGUIMod.instance.isopen.getExact()){
            open = true;
        }


        //  modules = ModuleManager.INSTANCE.getModules(category).stream().sorted(Comparator.comparing(module -> Callisto.getCustomFontRenderer().getStringWidth(module.getName()))).collect(Collectors.toCollection(ArrayList::new));
        modules = Winter.moduleManager.getModules(category)
                .stream()
                .sorted(Comparator.comparing(Objects::toString))
                .collect(Collectors.toCollection(ArrayList::new));
        float moduleY = getY() + getHeight();
        for(Module m : modules){
            float moduleStartY = moduleY + getHeight()*moduleCount;
            moduleButtons.add(new ModuleButton(getX() + 0.5f, moduleStartY, getWidth()-0.5f, moduleStartY + getHeight(), m, this.color,modules.get(modules.size()-1) == m));
            moduleCount++;
        }
    }


    @Override
    public void draw(int mouseX, int mouseY) {

     /*   if(!(yVal >= getY()) && !stopFirstAnim) {
            animation.update();
            animation.setValue(getY());
            animation.animate(getY(), 600, Easings.CUBIC_OUT);
            yVal = (int) animation.getValue();
        }else{
            stopFirstAnim=true;
            yVal = (int) getY();
        }

        if()*/
        int count=0;
        if(drag){
            setX(m1 + mouseX);
            setY(m2+mouseY);
        }

       /* if(this.isOpen()){
            RenderUtil.drawRect(getX()-1, getY()-1, getX() + getWidth()+1, getY()+ getHeight() + (getHeight()*moduleCount)+1, color.getRGB());
           // RenderUtil.drawRect(getX()-0.3f, getY()-0.3f, getX() + getWidth()+0.3f, getY()+ getHeight() + (getHeight()*moduleCount)+0.3f, color.darker().darker().getRGB());

        }else{
            RenderUtil.drawRect(getX()-1, getY()-1, getX() + getWidth()+1, getY()+getHeight()+1, color.getRGB());
            //RenderUtil.drawRect(getX()-0.3f, getY()-0.3f, getX() + getWidth()+0.3f, getY() +getHeight()+0.3f, color.darker().darker().getRGB());

        }*/
        if(this.isOpen()){

            for(ModuleButton moduleButton : moduleButtons){
                moduleButton.setX(getX());
                moduleButton.setColor(this.color);
                moduleButton.setY(getY() + getHeight() + count);
                moduleButton.setWidth(getWidth());
                moduleButton.setHeight(getHeight());
                //moduleButton.draw(mouseX, mouseY);
                count += moduleButton.finalHeight;

            }

        }

        RenderUtil.drawRect(getX()-1, getY()-1, getX() + getWidth()+1, getY()+getHeight()+1+count, color.getRGB());

        //54, 54, 54
        RenderUtil.drawRect(getX(), getY(), getX() + getWidth(),  isOpen() ? getY() +getHeight()-0.5f : getY() +getHeight(), new Color(54, 54, 54).darker().getRGB());
       // Callisto.getCustomFontRenderer().drawStringWithShadow(category.getName() + " - ["+ModuleManager.INSTANCE.getAmmount(category)+"]", getX() + 2, getY() + FontUtil.getFontHeight()/2 - 1, Color.WHITE.getRGB());
        Winter.getCustomFontRenderer().drawStringWithShadow(category.name(), getX() + 2, getY() + FontUtil.getFontHeight()/2-2,Color.WHITE.getRGB());
        Winter.getCustomFontRenderer().drawStringWithShadow( "["+Winter.moduleManager.getAmmount(category)+"]", getX() +getWidth() - FontUtil.getStringWidth("["+Winter.moduleManager.getAmmount(category)+"]") -2, getY() + FontUtil.getFontHeight()/2-2,Color.WHITE.getRGB());
        //Minecraft.getMinecraft().fontRenderer.drawStringWithShadow( "din1", 2, 2,Color.CYAN.getRGB());


        if(this.isOpen()){
            ClickGUIMod.instance.isopen.setExact(true);
            for(ModuleButton moduleButton : moduleButtons){
                moduleButton.draw(mouseX, mouseY);

            }

        }


    }

    @Override
    public void mouse(int mouseX, int mouseY, MouseClick mouseClick, MouseClickType mouseClickType) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseClick.equals(MouseClick.RIGHTCLICK)) {
                //OPENING
                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    open = !open;
                }

            } else if (mouseClick.equals(MouseClick.LEFTCLICK)) {
                //DRAGGING
                if (mouseClickType.equals(MouseClickType.CLICK)) {
                    drag = true;
                    m1 = getX()-mouseX;
                    m2 = getY()-mouseY;

                } else if (mouseClickType.equals(MouseClickType.RELEASE)){

                    drag = false;

                }
            }
        }else {
            drag = false;
        }

    }


    public void setColor(Color color) {
        this.color = color;
    }
}
