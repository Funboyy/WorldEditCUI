package de.funboyy.addon.worldedit.cui.v1_21_1;

import com.mojang.blaze3d.vertex.BufferUploader;
import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.models.Implements;
import net.labymod.v1_21_1.client.render.vertex.VersionedBufferBuilder;
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
  public void endTesselator(final BufferBuilder builder) {
    final VersionedBufferBuilder versionedBuilder = (VersionedBufferBuilder) builder;

    versionedBuilder.end();
    BufferUploader.drawWithShader(versionedBuilder.renderedBuffer());
  }

  @Override
  public void applyModelViewMatrix() {
    com.mojang.blaze3d.systems.RenderSystem.applyModelViewMatrix();
  }

}