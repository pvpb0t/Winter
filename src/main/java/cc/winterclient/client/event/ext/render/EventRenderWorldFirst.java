package cc.winterclient.client.event.ext.render;

import cc.winterclient.client.event.Event;
import net.minecraft.client.renderer.RenderGlobal;

public class EventRenderWorldFirst extends Event {

    private final RenderGlobal context;
    private final float partialTicks;
    public EventRenderWorldFirst(RenderGlobal context, float partialTicks)
    {
        this.context = context;
        this.partialTicks = partialTicks;
    }

    public RenderGlobal getContext()
    {
        return context;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

}
