package de.funboyy.addon.worldedit.cui.v1_21_5;

import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import net.labymod.api.client.gfx.GlConst;
import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.models.Implements;
import net.labymod.v1_21_5.client.render.vertex.VersionedBufferBuilder;
import net.minecraft.client.renderer.RenderType;

@Implements(RenderHelper.class)
public class VersionedRenderHelper implements RenderHelper {

  private RenderType renderType = null;

  @Override
  public void setRenderResource(final Object object) {
    if (object == null) {
      this.renderType = null;
      return;
    }

    if (object instanceof RenderType type) {
      this.renderType = type;
      return;
    }

    throw new IllegalArgumentException("You can only set a RenderType as render resource");
  }

  @Override
  public Object getQuadsRenderResource() {
    return RenderType.debugQuads();
  }

  @Override
  public Object getLinesRenderResource() {
    return RenderType.debugLine(RenderSystem.getShaderLineWidth());
  }

  @Override
  public Object getDebugLinesRenderResource() {
    return RenderType.debugLine(RenderSystem.getShaderLineWidth());
  }

  @Override
  public void endTesselator(final BufferBuilder builder, final int depthFunc) {
    final VersionedBufferBuilder versionedBuilder = (VersionedBufferBuilder) builder;

    versionedBuilder.end();

    final DepthTestFunction depthFunction = switch (depthFunc) {
      case GlConst.GL_ALWAYS -> DepthTestFunction.NO_DEPTH_TEST;
      case GlConst.GL_GEQUAL -> DepthTestFunction.GREATER_DEPTH_TEST;
      default -> DepthTestFunction.LESS_DEPTH_TEST;
    };

    final RenderPipelineAccessor pipeline = (RenderPipelineAccessor) this.renderType.getRenderPipeline();
    pipeline.storeDepthTestFunction();
    pipeline.setDepthTestFunction(depthFunction);

    this.renderType.draw(versionedBuilder.renderedBuffer());

    pipeline.restoreDepthTestFunction();
  }

}
