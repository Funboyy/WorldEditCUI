package de.funboyy.addon.worldedit.cui.v1_21_5;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import de.funboyy.addon.worldedit.cui.api.render.RenderHelper;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.models.Implements;
import net.labymod.v1_21_5.client.render.vertex.VersionedBufferBuilder;
import net.minecraft.client.Minecraft;

@Implements(RenderHelper.class)
public class VersionedRenderHelper implements RenderHelper {

  private RenderPipeline pipeline = null;

  @Override
  public Object getRenderResource() {
    return this.pipeline;
  }

  @Override
  public void setRenderResource(final Object object) {
    if (object == null) {
      this.pipeline = null;
      return;
    }

    if (object instanceof RenderPipeline renderPipeline) {
      this.pipeline = renderPipeline;
      return;
    }

    throw new IllegalArgumentException("You can only set a RenderPipeline as render resource");
  }

  @Override
  public Object getQuadsRenderResource() {
    return WorldEditRenderPipelines.QUADS;
  }

  @Override
  public Object getLinesRenderResource() {
    return WorldEditRenderPipelines.LINES;
  }

  @Override
  public Object getDebugLinesRenderResource() {
    return WorldEditRenderPipelines.DEBUG_LINES;
  }

  @Override
  public void endTesselator(final BufferBuilder builder) {
    final VersionedBufferBuilder versionedBuilder = (VersionedBufferBuilder) builder;

    versionedBuilder.end();

    this.draw(versionedBuilder.renderedBuffer());
  }

  // this is the same as 'RenderType#draw' maybe use it in the future
  private void draw(final MeshData meshData) {
    final RenderPipeline pipeline = this.pipeline;

    try {
      final GpuBuffer vertexBuffer = pipeline.getVertexFormat().uploadImmediateIndexBuffer(meshData.vertexBuffer());

      final GpuBuffer indexBuffer;
      final VertexFormat.IndexType indexType;

      if (meshData.indexBuffer() == null) {
        final RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(meshData.drawState().mode());

        indexBuffer = indices.getBuffer(meshData.drawState().indexCount());
        indexType = indices.type();
      }

      else {
        indexBuffer = pipeline.getVertexFormat().uploadImmediateIndexBuffer(meshData.indexBuffer());
        indexType = meshData.drawState().indexType();
      }

      final Minecraft minecraft = Minecraft.getInstance();
      final RenderTarget translucent = minecraft.levelRenderer.getTranslucentTarget();
      final RenderTarget target = translucent == null
          ? minecraft.getMainRenderTarget()
          : translucent;

      try (final RenderPass pass = RenderSystem.getDevice().createCommandEncoder()
          .createRenderPass(target.getColorTexture(), OptionalInt.empty(),
              target.useDepth ? target.getDepthTexture() : null, OptionalDouble.empty())) {

        pass.setPipeline(pipeline);
        pass.setVertexBuffer(0, vertexBuffer);

        if (RenderSystem.SCISSOR_STATE.isEnabled()) {
          pass.enableScissor(RenderSystem.SCISSOR_STATE);
        }

        for (int i = 0; i < 12; ++i) {
          final GpuTexture texture = RenderSystem.getShaderTexture(i);

          if (texture != null) {
            pass.bindSampler("Sampler" + i, texture);
          }
        }

        pass.setIndexBuffer(indexBuffer, indexType);
        pass.drawIndexed(0, meshData.drawState().indexCount());
      }
    } catch (final Throwable throwable) {
      if (meshData == null) {
        throw throwable;
      }

      try {
        meshData.close();
      } catch (final Throwable suppressed) {
        throwable.addSuppressed(suppressed);
      }

      throw throwable;
    }

    meshData.close();
  }

}
