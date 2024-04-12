package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;

public class PointPacket extends WorldEditPacket {

  private int id;
  private double x;
  private double y;
  private double z;
  private boolean vertexLatch;
  private double distance;

  public PointPacket() {
    super(PacketType.POINT);
  }

  public int getId() {
    return this.id;
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public double getZ() {
    return this.z;
  }

  public boolean isVertexLatch() {
    return this.vertexLatch;
  }

  public double getDistance() {
    return this.distance;
  }

  @Override
  public void handleIncomingPayload(final PacketMessage message) {
    this.multi = message.isMulti();

    this.checkParameters(message.getParameters());

    this.id = message.getInt(0);

    if (this.isMulti()
        && "~".equals(message.getString(1))
        && "~".equals(message.getString(2))
        && "~".equals(message.getString(3))) {

      this.vertexLatch = true;
      this.distance = message.getDouble(4);
      return;
    }

    this.x = message.getDouble(1);
    this.y = message.getDouble(2);
    this.z = message.getDouble(3);
  }

}
