package cc.winterclient.client.injection.mixins;

import cc.winterclient.client.util.logger.Logger;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.event.ext.scenarios.EventSendPacket;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void onSendPacketPre(Packet<?> packet, CallbackInfo info) {
        EventSendPacket event = new EventSendPacket(packet);
        event.call();
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method = {"channelRead0"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void onChannelReadPre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info) {
        EventReceivePacket event = new EventReceivePacket(packet);
        event.call();
        if (event.isCancelled()) {
            info.cancel();
        }
    }

}
