package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;

public class UpdatePacket extends WorldEditPacket {

  public UpdatePacket() {
    super(PacketType.UPDATE);
  }

  @Override
  public void handleIncomingPayload(final PacketMessage message) {
    this.multi = message.isMulti();

    this.checkParameters(message.getParameters());
  }

}
