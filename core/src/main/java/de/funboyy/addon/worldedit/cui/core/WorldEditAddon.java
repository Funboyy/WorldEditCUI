package de.funboyy.addon.worldedit.cui.core;

import de.funboyy.addon.worldedit.cui.core.protocol.WorldEditProtocol;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.enginehub.worldeditcui.WorldEdit;

@AddonMain
public class WorldEditAddon extends LabyAddon<WorldEditConfiguration> {

  // ToDo:
  //  - find a good way to make it work with OptiFine Shaders
  //  - sometimes the player is missing in the player list in 1.8.9 and 1.16.5

  @Override
  protected void enable() {
    this.registerSettingCategory();

    WorldEdit.init(this.referenceStorageAccessor());

    final WorldEditProtocol protocol = new WorldEditProtocol();
    protocol.register();
  }

  @Override
  protected Class<WorldEditConfiguration> configurationClass() {
    return WorldEditConfiguration.class;
  }

}
