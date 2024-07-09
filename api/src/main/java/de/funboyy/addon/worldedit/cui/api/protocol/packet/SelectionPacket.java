package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;
import java.util.UUID;

public class SelectionPacket extends WorldEditPacket {

  private String shape;
  private UUID id;

  public SelectionPacket() {
    super(PacketType.SELECTION);
  }

  public String getShape() {
    return this.shape;
  }

  public UUID getId() {
    return this.id;
  }

  @Override
  public void read(final PacketMessage message) {
    final String[] parameters = message.getParameters();
    this.multi = message.isMulti();

    this.checkParameters(parameters);

    this.shape = message.getString(0);

    if (parameters.length == 2) {
      this.id = UUID.fromString(message.getString(1));
    }
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<SelectionPacket> handler = protocol.getHandler(SelectionPacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }

}
