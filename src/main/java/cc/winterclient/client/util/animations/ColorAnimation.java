package cc.winterclient.client.util.animations;

import java.awt.*;

public class ColorAnimation {

    private final Animation redPart = new Animation();
    private final Animation greenPart = new Animation();
    private final Animation bluePart = new Animation();
    private final Animation alphaPart = new Animation();

    public void update() {
        redPart.update();
        greenPart.update();
        bluePart.update();
        alphaPart.update();
    }

    public boolean isAlive() {
        return (redPart.isAlive() &&
                greenPart.isAlive() &&
                bluePart.isAlive());
    }

    public void animate(Color color, double duration) {
        animate(color, duration, false);
    }

    public void animate(Color color, double duration, boolean safe) {
        redPart.animate(color.getRed(), duration, safe);
        greenPart.animate(color.getGreen(), duration, safe);
        bluePart.animate(color.getBlue(), duration, safe);
        alphaPart.animate(color.getAlpha(), duration, safe);
    }

    public Color getColor() {
        return new Color(((int) redPart.getValue()), ((int) greenPart.getValue()), ((int) bluePart.getValue()), (int) alphaPart.getValue());
    }

}
