package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;

public class ColorPacket extends WorldEditPacket {

  private String[] colors;

  public ColorPacket() {
    super(PacketType.COLOR);
  }

  public String[] getColors() {
    return this.colors;
  }

  @Override
  public void read(final PacketMessage message) {
    final String[] parameters = message.getParameters();
    this.multi = message.isMulti();

    this.checkParameters(parameters);

    this.colors = new String[parameters.length];

    System.arraycopy(parameters, 0, this.colors, 0, parameters.length);
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<ColorPacket> handler = protocol.getHandler(ColorPacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }
}
