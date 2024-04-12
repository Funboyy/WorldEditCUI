package de.funboyy.addon.worldedit.cui.api.protocol.handler;

import de.funboyy.addon.worldedit.cui.api.protocol.packet.WorldEditPacket;
import net.labymod.serverapi.protocol.packet.PacketHandler;
import org.enginehub.worldeditcui.WorldEdit;

public abstract class WorldEditHandler<P extends WorldEditPacket> implements PacketHandler<P> {

  private final WorldEdit controller;

  protected WorldEditHandler(final WorldEdit controller) {
    this.controller = controller;
  }

  protected WorldEdit getController() {
    return this.controller;
  }

}
