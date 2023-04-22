package cc.winterclient.client.command;

import net.minecraft.client.Minecraft;
import scala.collection.parallel.ParIterableLike;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    private final String name;
    private final String description;
    private final String[] aliases;
    protected Minecraft mc;

    public Command(String name, String description, String... aliases){
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.mc = Minecraft.getMinecraft();
    }

    public abstract void execute(String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }
}
