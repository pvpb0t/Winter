package cc.winterclient.client;


import cc.winterclient.client.command.CommandManager;
import cc.winterclient.client.event.EventManager;
import cc.winterclient.client.font.CustomFontRenderer;
import cc.winterclient.client.forge.ForgeEventInjector;
import cc.winterclient.client.friend.FriendManager;
import cc.winterclient.client.ui.gui.NewClickGUI;
import cc.winterclient.client.module.ModuleManager;
import cc.winterclient.client.option.OptionConfig;
import cc.winterclient.client.option.OptionManager;
import cc.winterclient.client.security.HWID;
import cc.winterclient.client.util.logger.Logger;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Winter {

    public static Winter instance;

    public final static String modid = "winter";
    public final static String clientName = "Winter";
    public final static String clientVersion = "1.0.1";
    public final static String clientPrefix = "-";

    public static ModuleManager moduleManager;
    public CommandManager commandManager;
    public NewClickGUI newClickGUI;
    public FriendManager friendManager;
    public static OptionManager optionManager;
    public static CustomFontRenderer customFontRenderer;
    public static RotationAnimator rotationAnimator;
    public File path;

    public void startClient(){
        instance = this;

        try{
            Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Winter.class.getResourceAsStream("/font/ebic.ttf")));
            customFontRenderer = new CustomFontRenderer(font.deriveFont(19.0f), true, true);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        path = new File("Winter");

        if(!path.isDirectory()){
            path.mkdir();
        }
        MinecraftForge.EVENT_BUS.register(new ForgeEventInjector());

        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        newClickGUI = new NewClickGUI();
        friendManager = new FriendManager();
        optionManager = new OptionManager();
        rotationAnimator = new RotationAnimator();

        OptionConfig.loadOptionConfig();

        EventManager.register(moduleManager);
        EventManager.register(commandManager);
        EventManager.register(rotationAnimator);

        Runtime.getRuntime().addShutdownHook(new OptionConfig());
        Logger.getLogger().print(HWID.EncryptedByteArray(HWID.generateBytes()));
        Display.setTitle(clientName + " [" + clientVersion + "]" + "☆");
    }

    public void stopClient(){
        friendManager.save();
    }

    public static CustomFontRenderer getCustomFontRenderer() {
        return customFontRenderer;
    }

}
