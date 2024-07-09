package de.funboyy.addon.worldedit.cui.api.protocol.packet;

import de.funboyy.addon.worldedit.cui.api.protocol.PacketMessage;
import de.funboyy.addon.worldedit.cui.api.protocol.PacketType;
import de.funboyy.addon.worldedit.cui.api.protocol.WorldEditProtocol;
import de.funboyy.addon.worldedit.cui.api.protocol.handler.WorldEditHandler;

public class MinMaxPacket extends WorldEditPacket {

  private int min;
  private int max;

  public MinMaxPacket() {
    super(PacketType.MINMAX);
  }

  public int getMin() {
    return this.min;
  }

  public int getMax() {
    return this.max;
  }

  @Override
  public void read(final PacketMessage message) {
    this.multi = message.isMulti();

    this.checkParameters(message.getParameters());

    this.min = message.getInt(0);
    this.max = message.getInt(1);
  }

  @Override
  public void handle(final WorldEditProtocol protocol) {
    final WorldEditHandler<MinMaxPacket> handler = protocol.getHandler(MinMaxPacket.class);

    if (handler == null) {
      return;
    }

    handler.handle(this);
  }

}
