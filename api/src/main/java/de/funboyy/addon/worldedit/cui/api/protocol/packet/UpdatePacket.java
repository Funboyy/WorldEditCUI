package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;

public class UpdatePacket extends WorldEditPacket {

  public UpdatePacket() {
    super(PacketType.UPDATE);
  }

  @Override
  public void read(final PacketMessage message) {
    this.multi = message.isMulti();

    this.checkParameters(message.getParameters());
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<UpdatePacket> handler = protocol.getHandler(UpdatePacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }

}
