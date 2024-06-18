package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;

public class Point2DPacket extends WorldEditPacket {

  private int id;
  private int x;
  private int z;

  public Point2DPacket() {
    super(PacketType.POINT2D);
  }

  public int getId() {
    return this.id;
  }

  public int getX() {
    return this.x;
  }

  public int getZ() {
    return this.z;
  }

  @Override
  public void read(final PacketMessage message) {
    this.multi = message.isMulti();

    this.checkParameters(message.getParameters());

    this.id = message.getInt(0);
    this.x = message.getInt(1);
    this.z = message.getInt(2);
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<Point2DPacket> handler = protocol.getHandler(Point2DPacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }

}
