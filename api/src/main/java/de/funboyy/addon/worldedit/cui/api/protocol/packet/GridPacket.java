package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;

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
  public void read(final PacketMessage message) {
    final String[] parameters = message.getParameters();
    this.multi = message.isMulti();

    this.checkParameters(parameters);

    this.spacing = message.getDouble(0);
    this.renderType = parameters.length > 1 ? message.getString(1) : null;
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<GridPacket> handler = protocol.getHandler(GridPacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }

}
