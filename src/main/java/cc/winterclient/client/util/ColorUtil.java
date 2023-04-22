package cc.winterclient.client.util;

import cc.winterclient.client.module.ext.client.ClickGUIMod;
import cc.winterclient.client.util.animations.Animation;
import cc.winterclient.client.util.animations.Easings;

import java.awt.*;

public class ColorUtil {
    public static int toARGB(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    private static final Animation animation = new Animation();
    private static Boolean go = true;

    public static int toRGBA(int r, int g, int b) {
        return ColorUtil.toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBA(float r, float g, float b, float a) {
        return ColorUtil.toRGBA((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) ((rainbowState %= 360.0) / 360.0), (float) (ClickGUIMod.instance.saturatiom.getExact() / 255.0f), (float) (ClickGUIMod.instance.saturatiom.getExact() / 255.0f));
    }

    public static Color interpolate(float value, Color start, Color end) {
        float sr = (float) start.getRed() / 255.0f;
        float sg = (float) start.getGreen() / 255.0f;
        float sb = (float) start.getBlue() / 255.0f;
        float sa = (float) start.getAlpha() / 255.0f;
        float er = (float) end.getRed() / 255.0f;
        float eg = (float) end.getGreen() / 255.0f;
        float eb = (float) end.getBlue() / 255.0f;
        float ea = (float) end.getAlpha() / 255.0f;
        float r = sr * value + er * (1.0f - value);
        float g = sg * value + eg * (1.0f - value);
        float b = sb * value + eb * (1.0f - value);
        float a = sa * value + ea * (1.0f - value);
        return new Color(r, g, b, a);
    }

    public static void pulse(int much) {

    }

    public static int toRGBA(float[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA(colors[0], colors[1], colors[2], colors[3]);
    }

    public static int toRGBA(double[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA((float) colors[0], (float) colors[1], (float) colors[2], (float) colors[3]);
    }

    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static class Colors {
        public static final int WHITE;
        public static final int BLACK;
        public static final int RED;
        public static final int GREEN;
        public static final int BLUE;
        public static final int ORANGE;
        public static final int PURPLE;
        public static final int GRAY;
        public static final int DARK_RED;
        public static final int YELLOW;
        public static final int RAINBOW = Integer.MIN_VALUE;

        static {
            WHITE = ColorUtil.toRGBA(255, 255, 255, 255);
            BLACK = ColorUtil.toRGBA(0, 0, 0, 255);
            RED = ColorUtil.toRGBA(255, 0, 0, 255);
            GREEN = ColorUtil.toRGBA(0, 255, 0, 255);
            BLUE = ColorUtil.toRGBA(0, 0, 255, 255);
            ORANGE = ColorUtil.toRGBA(255, 128, 0, 255);
            PURPLE = ColorUtil.toRGBA(163, 73, 163, 255);
            GRAY = ColorUtil.toRGBA(127, 127, 127, 255);
            DARK_RED = ColorUtil.toRGBA(64, 0, 0, 255);
            YELLOW = ColorUtil.toRGBA(255, 255, 0, 255);
        }
    }
}
