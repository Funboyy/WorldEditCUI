package de.funboyy.addon.worldedit.cui.api;

import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.serverapi.protocol.packet.protocol.Protocol;
import org.enginehub.worldeditcui.WorldEdit;

@Referenceable
public interface WorldEditLoader {

  void registerProtocol(final Protocol protocol);

  WorldEdit getController();

}
