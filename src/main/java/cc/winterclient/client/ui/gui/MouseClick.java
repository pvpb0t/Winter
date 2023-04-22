package cc.winterclient.client.ui.gui;

import java.util.HashMap;
import java.util.Map;
/**
 * @Author pvpb0t
 * @Since 8/9/2022
 */
public enum MouseClick {
    LEFTCLICK(0),
    RIGHTCLICK(1),
    MIDDLECLICK(2);

    private int value;
    private static Map map = new HashMap<>();

    private MouseClick(int value) {
        this.value = value;
    }

    static {
        for (MouseClick pageType : MouseClick.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static MouseClick valueOf(int pageType) {
        return (MouseClick) map.get(pageType);
    }

    public int getValue() {
        return value;
    }

}
