package de.funboyy.addon.worldedit.cui.v1_8_9;

import de.funboyy.addon.worldedit.cui.api.render.pipeline.ShaderAccessor;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.optifine.shaders.Shaders;

@Singleton
@Implements(ShaderAccessor.class)
public class VersionedShaderAccessor implements ShaderAccessor {

  @Override
  public boolean isShadowPass() {
    return Shaders.isShadowPass;
  }

  @Override
  public void begin() {
    Shaders.beginLeash();
  }

  @Override
  public void end() {
    Shaders.endLeash();
  }

}
