package de.funboyy.addon.worldedit.cui.api.render.pipeline;

import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface ShaderAccessor {

  boolean isShadowPass();

  void beginLeash();

  void endLeash();

}
