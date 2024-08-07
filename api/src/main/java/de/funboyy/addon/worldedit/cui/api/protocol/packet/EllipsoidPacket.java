package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;

public class EllipsoidPacket extends WorldEditPacket {

  private int id;
  private Number x;
  private Number y;
  private Number z;

  public EllipsoidPacket() {
    super(PacketType.ELLIPSOID);
  }

  public int getId() {
    return this.id;
  }

  public Number getX() {
    return this.x;
  }

  public Number getY() {
    return this.y;
  }

  public Number getZ() {
    return this.z;
  }

  @Override
  public void read(final PacketMessage message) {
    this.multi = message.isMulti();

    this.checkParameters(message.getParameters());

    this.id = message.getInt(0);

    if (this.id == 0) {
      this.x = message.getInt(1);
      this.y = message.getInt(2);
      this.z = message.getInt(3);
      return;
    }

    this.x = message.getDouble(1);
    this.y = message.getDouble(2);
    this.z = message.getDouble(3);
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<EllipsoidPacket> handler = protocol.getHandler(EllipsoidPacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }

}
