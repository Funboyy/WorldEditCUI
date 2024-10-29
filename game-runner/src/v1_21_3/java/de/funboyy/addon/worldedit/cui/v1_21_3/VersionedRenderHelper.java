package de.funboyy.addon.worldedit.cui.v1_21_3;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.models.Implements;
import net.labymod.v1_21_3.client.render.vertex.VersionedBufferBuilder;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.ShaderProgram;

@Implements(RenderHelper.class)
public class VersionedRenderHelper implements RenderHelper {

  @Override
  public Object getShader() {
    return RenderSystem.getShader();
  }

  @Override
  public void setShader(final Object object) {
    if (object instanceof ShaderProgram shader) {
      RenderSystem.setShader(shader);
      return;
    }

    if (object instanceof CompiledShaderProgram shader) {
      RenderSystem.setShader(shader);
      return;
    }

    throw new IllegalArgumentException("You can only set a ShaderProgram or a CompiledShaderProgram as shader");
  }

  @Override
  public Object getPositionColorShader() {
    return CoreShaders.POSITION_COLOR;
  }

  @Override
  public Object getRenderTypeLinesShader() {
    return CoreShaders.RENDERTYPE_LINES;
  }

  @Override
  public void endTesselator(final BufferBuilder builder) {
    final VersionedBufferBuilder versionedBuilder = (VersionedBufferBuilder) builder;

    versionedBuilder.end();
    BufferUploader.drawWithShader(versionedBuilder.renderedBuffer());
  }

}
