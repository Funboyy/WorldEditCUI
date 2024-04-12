package de.funboyy.addon.worldedit.cui.core.protocol;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.WorldEditPacket;
import net.labymod.serverapi.protocol.packet.Packet;
import net.labymod.serverapi.protocol.payload.translation.AbstractPayloadTranslationListener;

public class WorldEditTranslationListener extends AbstractPayloadTranslationListener {

  public WorldEditTranslationListener(final WorldEditProtocol protocol) {
    super(protocol);
  }

  @Override
  public byte[] translateIncomingPayload(final byte[] payload) {
    return payload;
  }

  @Override
  public <T extends Packet> byte[] translateOutgoingPayload(final T packet) {
    if (!(packet instanceof WorldEditPacket worldEditPacket)) {
      return new byte[0];
    }

    return worldEditPacket.translateOutgoingPayload();
  }

}
