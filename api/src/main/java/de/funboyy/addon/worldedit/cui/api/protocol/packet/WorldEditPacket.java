package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import net.labymod.serverapi.api.packet.Packet;

public abstract class WorldEditPacket implements Packet {

  private final PacketType type;
  protected boolean multi;

  protected WorldEditPacket(final PacketType type) {
    this.type = type;
  }

  public PacketType getType() {
    return this.type;
  }

  public boolean isMulti() {
    return this.multi;
  }

  protected void checkParameters(final String[] parameters) {
    final int length = parameters.length;

    if (length < this.type.getMinParameters() || length > this.type.getMaxParameters()) {
      final String message = String.format(
          "Invalid number of parameters. %s event requires %s parameters but received %s [%s]",
          this.getType().getName(),
          this.getRequiredParameterString(),
          length,
         String.join(", ", parameters));
      throw new IllegalArgumentException(message);
    }
  }

  private String getRequiredParameterString() {
    if (this.getType().getMaxParameters() == this.getType().getMinParameters()) {
      return String.valueOf(this.getType().getMaxParameters());
    }

    return String.format("between %d and %d", this.getType().getMinParameters(), this.getType().getMaxParameters());
  }

  public void read(final PacketMessage message) throws Exception {
  }

  public byte[] write() {
    return new byte[0];
  }

  public void handle(final WorldEditProtocol protocol) {
  }

}
