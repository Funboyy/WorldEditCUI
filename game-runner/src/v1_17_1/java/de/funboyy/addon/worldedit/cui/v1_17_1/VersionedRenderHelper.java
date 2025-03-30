package de.funboyy.addon.worldedit.cui.v1_17_1;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.models.Implements;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;

@Implements(RenderHelper.class)
public class VersionedRenderHelper implements RenderHelper {

  @Override
  public Object getRenderResource() {
    return RenderSystem.getShader();
  }

  @Override
  public void setRenderResource(final Object object) {
    if (!(object instanceof ShaderInstance shader)) {
      throw new IllegalArgumentException("You can only set a ShaderInstance as render resource");
    }

    RenderSystem.setShader(() -> shader);
  }

  @Override
  public Object getQuadsRenderResource() {
    return GameRenderer.getPositionColorShader();
  }

  @Override
  public Object getLinesRenderResource() {
    return GameRenderer.getRendertypeLinesShader();
  }

  @Override
  public void endTesselator(final BufferBuilder builder) {
    Tesselator.getInstance().end();
  }

}
