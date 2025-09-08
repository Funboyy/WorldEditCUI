package de.funboyy.addon.worldedit.cui.v1_21_5;

import de.funboyy.addon.worldedit.cui.api.render.pipeline.ShaderAccessor;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;

@Singleton
@Implements(ShaderAccessor.class)
public class VersionedShaderAccessor implements ShaderAccessor {

  @Override
  public boolean isShadowPass() {
    return false;
  }

  @Override
  public void begin() {
  }

  @Override
  public void end() {
  }

}
