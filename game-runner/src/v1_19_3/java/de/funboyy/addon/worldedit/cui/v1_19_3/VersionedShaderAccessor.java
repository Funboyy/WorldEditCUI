package de.funboyy.addon.worldedit.cui.v1_19_3;

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
  public void beginLeash() {
    Shaders.beginLeash();
  }

  @Override
  public void endLeash() {
    Shaders.endLeash();
  }

}
