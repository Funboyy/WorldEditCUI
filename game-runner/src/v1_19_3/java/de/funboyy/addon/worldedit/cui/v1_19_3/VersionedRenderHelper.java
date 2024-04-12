package de.funboyy.addon.worldedit.cui.v1_19_3;

import com.mojang.blaze3d.vertex.Tesselator;
import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import net.labymod.api.models.Implements;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;

@Implements(RenderHelper.class)
public class VersionedRenderHelper implements RenderHelper {

  @Override
  public Object getShader() {
    return com.mojang.blaze3d.systems.RenderSystem.getShader();
  }

  @Override
  public void setShader(final Object object) {
    if (!(object instanceof ShaderInstance shader)) {
      throw new IllegalArgumentException("You can only set a ShaderInstance as shader");
    }

    com.mojang.blaze3d.systems.RenderSystem.setShader(() -> shader);
  }

  @Override
  public Object getPositionColorShader() {
    return GameRenderer.getPositionColorShader();
  }

  @Override
  public Object getRenderTypeLinesShader() {
    return GameRenderer.getRendertypeLinesShader();
  }

  @Override
  public void endTesselator() {
    Tesselator.getInstance().end();
  }

  @Override
  public void applyModelViewMatrix() {
    com.mojang.blaze3d.systems.RenderSystem.applyModelViewMatrix();
  }

}
