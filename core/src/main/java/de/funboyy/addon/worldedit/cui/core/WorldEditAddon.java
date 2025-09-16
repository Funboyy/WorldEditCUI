package de.funboyy.addon.worldedit.cui.core;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.enginehub.worldeditcui.WorldEdit;

@AddonMain
public class WorldEditAddon extends LabyAddon<WorldEditConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    WorldEdit.init(this.referenceStorageAccessor());

    this.registerListener(new WorldEditListener());
  }

  @Override
  protected Class<WorldEditConfiguration> configurationClass() {
    return WorldEditConfiguration.class;
  }

}
