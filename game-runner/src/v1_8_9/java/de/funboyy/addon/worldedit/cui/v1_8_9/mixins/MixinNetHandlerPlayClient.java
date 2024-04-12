package de.funboyy.addon.worldedit.cui.v1_8_9.mixins;

import de.funboyy.addon.worldedit.cui.core.protocol.WorldEditProtocol;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {

  @Shadow
  public abstract void addToSendQueue(final Packet<?> packet);

  @Inject(
      method = "addToSendQueue",
      at = @At("HEAD")
  )
  public void worldEdit$sendPacket(final Packet<?> packet, final CallbackInfo callbackInfo) {
    if (!(packet instanceof C17PacketCustomPayload payload)) {
      return;
    }

    final String channelName = payload.getChannelName();
    final PacketBuffer payloadData = payload.getBufferData();

    // if 'REGISTER' is sent without 'WECUI', send a second 'REGISTER' payload to register 'WECUI'
    if (channelName.equals("REGISTER")) {
      final String[] channels = payloadData.toString(StandardCharsets.UTF_8).split("\u0000");

      for (final String channel : channels) {
        if (channel.equals(WorldEditProtocol.LEGACY_CHANNEL)) {
          return;
        }
      }

      this.addToSendQueue(new C17PacketCustomPayload(channelName, new PacketBuffer(Unpooled
          .wrappedBuffer((WorldEditProtocol.LEGACY_CHANNEL).getBytes(StandardCharsets.UTF_8)))));
      return;
    }

    // also send to channel 'WECUI' if the channel is 'worldedit:cui'
    if (channelName.equals(WorldEditProtocol.IDENTIFIER.toString())) {
      this.addToSendQueue(new C17PacketCustomPayload(WorldEditProtocol.LEGACY_CHANNEL, payloadData));
    }
  }

}
