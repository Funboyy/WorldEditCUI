package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;

public class ColorPacket extends WorldEditPacket {

  private String[] colors;

  public ColorPacket() {
    super(PacketType.COLOR);
  }

  public String[] getColors() {
    return this.colors;
  }

  @Override
  public void handleIncomingPayload(final PacketMessage message) {
    final String[] parameters = message.getParameters();
    this.multi = message.isMulti();

    this.checkParameters(parameters);

    this.colors = new String[parameters.length];

    System.arraycopy(parameters, 0, this.colors, 0, parameters.length);
  }

}
