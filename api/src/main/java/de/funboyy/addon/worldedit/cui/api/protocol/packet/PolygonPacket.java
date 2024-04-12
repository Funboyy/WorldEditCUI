package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;

public class PolygonPacket extends WorldEditPacket {

  private int[] vertices;

  public PolygonPacket() {
    super(PacketType.POLYGON);
  }

  public int[] getVertices() {
    return this.vertices;
  }

  @Override
  public void handleIncomingPayload(final PacketMessage message) {
    final String[] parameters = message.getParameters();
    this.multi = message.isMulti();

    this.checkParameters(parameters);

    this.vertices = new int[parameters.length];

    for (int i = 0; i < parameters.length; i++) {
      this.vertices[i] = message.getInt(i);
    }
  }

}
