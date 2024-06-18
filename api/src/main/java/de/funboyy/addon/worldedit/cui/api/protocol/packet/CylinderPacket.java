package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;

public class CylinderPacket extends WorldEditPacket {

  private int x;
  private int y;
  private int z;
  private double radX;
  private double radZ;

  public CylinderPacket() {
    super(PacketType.CYLINDER);
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getZ() {
    return this.z;
  }

  public double getRadX() {
    return this.radX;
  }

  public double getRadZ() {
    return this.radZ;
  }

  @Override
  public void read(final PacketMessage message) {
    this.multi = message.isMulti();

    this.checkParameters(message.getParameters());

    this.x = message.getInt(0);
    this.y = message.getInt(1);
    this.z = message.getInt(2);
    this.radX = message.getDouble(3);
    this.radZ = message.getDouble(4);
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<CylinderPacket> handler = protocol.getHandler(CylinderPacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }

}
