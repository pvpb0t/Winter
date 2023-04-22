package cc.winterclient.client.util.logger;

import cc.winterclient.client.Winter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public final class Logger {
    /** Logger instance */
    private static Logger logger = null;

    /**
     * Appends message param to client tag,
     * and prints it to the log.
     *
     * @param message to be printed
     */
    public void print(String message) {
        System.out.println(String.format("[%s] %s", Winter.clientName, message));
    }

    /**
     * Appends message param to client tag,
     * and prints it to the chat.
     *
     * @param message to be printed
     */
    public void printToChat(String message) {

        //Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("§9§l[§c%s§9] §7%s", Callisto.ClientName, message.replace("&", "§"))).setStyle(new Style().setColor(TextFormatting.GRAY)));
        this.PrintTextCompToChat(new TextComponentString((message)).setStyle(new Style().setBold(Boolean.valueOf(false)).setColor(TextFormatting.GRAY)));

    }

    public static Logger getLogger() {
        return logger == null ? (logger = new Logger()) : logger;
    }

    private final ITextComponent ClientNameChatComponent() {
        return new TextComponentString("").appendSibling(new TextComponentString("[").setStyle(new Style().setColor(TextFormatting.BLUE).setBold(Boolean.valueOf(true)))).appendSibling(new TextComponentString(Winter.clientName).setStyle(new Style().setColor(TextFormatting.RED))).appendSibling(new TextComponentString("] ").setStyle(new Style().setColor(TextFormatting.BLUE).setBold(Boolean.valueOf(true))));
    }

    private void PrintTextCompToChat(ITextComponent s) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("").appendSibling(this.ClientNameChatComponent()).appendSibling(s), 0);
    }

}
