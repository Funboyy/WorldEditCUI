package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import java.nio.charset.StandardCharsets;

public class VersionPacket extends WorldEditPacket {

  private int version;

  public VersionPacket() {
    super(PacketType.VERSION);
  }

  public VersionPacket(final int version) {
    super(PacketType.VERSION);
    this.version = version;
  }

  @Override
  public byte[] write() {
    final String payload = super.getType().getTypeId() + "|" + this.version;

    return  payload.getBytes(StandardCharsets.UTF_8);
  }

}
