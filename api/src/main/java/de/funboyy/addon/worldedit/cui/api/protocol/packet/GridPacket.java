package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;

public class GridPacket extends WorldEditPacket {

  private double spacing;
  private String renderType;

  public GridPacket() {
    super(PacketType.GRID);
  }

  public double getSpacing() {
    return this.spacing;
  }

  public String getRenderType() {
    return this.renderType;
  }

  @Override
  public void handleIncomingPayload(final PacketMessage message) {
    final String[] parameters = message.getParameters();
    this.multi = message.isMulti();

    this.checkParameters(parameters);

    this.spacing = message.getDouble(0);
    this.renderType = parameters.length > 1 ? message.getString(1) : null;
  }

}
