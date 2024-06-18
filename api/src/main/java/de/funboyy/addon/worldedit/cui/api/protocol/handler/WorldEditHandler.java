package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.WorldEditPacket;
import org.enginehub.worldeditcui.WorldEdit;

public abstract class WorldEditHandler<P extends WorldEditPacket> {

  private final WorldEdit controller;

  protected WorldEditHandler(final WorldEdit controller) {
    this.controller = controller;
  }

  public abstract void handle(final P packet);

  public abstract Class<P> getPacketClass();

  protected WorldEdit getController() {
    return this.controller;
  }

}
