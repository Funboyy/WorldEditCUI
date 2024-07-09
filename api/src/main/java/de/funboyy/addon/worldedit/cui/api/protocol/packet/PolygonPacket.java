package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;

public class PolygonPacket extends WorldEditPacket {

  private int[] vertices;

  public PolygonPacket() {
    super(PacketType.POLYGON);
  }

  public int[] getVertices() {
    return this.vertices;
  }

  @Override
  public void read(final PacketMessage message) {
    final String[] parameters = message.getParameters();
    this.multi = message.isMulti();

    this.checkParameters(parameters);

    this.vertices = new int[parameters.length];

    for (int i = 0; i < parameters.length; i++) {
      this.vertices[i] = message.getInt(i);
    }
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<PolygonPacket> handler = protocol.getHandler(PolygonPacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }

}
