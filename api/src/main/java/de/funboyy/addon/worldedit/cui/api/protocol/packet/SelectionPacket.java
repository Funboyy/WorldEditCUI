package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
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
  public void handleIncomingPayload(final PacketMessage message) {
    final String[] parameters = message.getParameters();
    this.multi = message.isMulti();

    this.checkParameters(parameters);

    this.shape = message.getString(0);

    if (parameters.length == 2) {
      this.id = UUID.fromString(message.getString(1));
    }
  }

}
