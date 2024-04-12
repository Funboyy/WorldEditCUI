package de.funboyy.addon.worldedit.cui.api.render;

import de.funboyy.addon.worldedit.cui.api.MinecraftHelper;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.gfx.color.GFXAlphaFunction;
import net.labymod.api.client.gfx.color.blend.GFXBlendParameter;
import net.labymod.api.client.gfx.pipeline.Blaze3DGlStatePipeline;
import net.labymod.api.client.render.gl.GlStateBridge;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.MinecraftCamera;
import net.labymod.api.util.math.vector.FloatVector3;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.PipelineProvider;
import org.enginehub.worldeditcui.render.RenderSink;
import org.enginehub.worldeditcui.util.Vector3;

public class WorldEditRenderer {

  private final Minecraft minecraft;
  private final GlStateBridge bridge;
  private final Blaze3DGlStatePipeline pipeline;
  private final MinecraftHelper helper;
  private final RenderHelper renderHelper;
  private final WorldEdit controller;
  private final RenderContext context;
  private final List<PipelineProvider> pipelines;
  private int currentPipelineIdx;
  private RenderSink sink;

  public WorldEditRenderer(final List<PipelineProvider> pipelines) {
    this.minecraft = Laby.labyAPI().minecraft();
    this.bridge = Laby.references().glStateBridge();
    this.pipeline = Laby.gfx().blaze3DGlStatePipeline();
    this.helper = WorldEdit.references().minecraftHelper();
    this.renderHelper = WorldEdit.references().renderHelper();
    this.controller = new WorldEdit();
    this.context = new RenderContext();
    this.pipelines = pipelines;
  }

  private RenderSink providePipeline() {
    if (this.sink != null) {
      return this.sink;
    }

    for (int i = this.currentPipelineIdx; i < this.pipelines.size(); i++) {
      final PipelineProvider pipeline = this.pipelines.get(i);

      if (!pipeline.available()) {
        continue;
      }

      try {
        final RenderSink sink = pipeline.provide();
        this.currentPipelineIdx = i;
        return this.sink = sink;
      }
      catch (final Exception exception) {
        exception.printStackTrace();
      }
    }

    throw new IllegalStateException("No pipeline available to render with!");
  }

  protected void invalidatePipeline() {
    if (this.currentPipelineIdx < this.pipelines.size() - 1) {
      this.currentPipelineIdx++;
      this.sink = null;
    }
  }

  public WorldEdit getController() {
    return this.controller;
  }

  public void render(final float tickDelta) {
    try {
      final RenderSink sink = this.providePipeline();

      if (!this.pipelines.get(this.currentPipelineIdx).shouldRender()) {
        return;
      }

      this.helper.pushProfiler("worldeditcui");

      final MinecraftCamera camera = this.minecraft.getCamera();

      if (camera == null) {
        return;
      }

      final FloatVector3 position = camera.renderPosition();

      this.context.init(new Vector3(position.getX(), position.getY(), position.getZ()), tickDelta, sink);

      this.pipeline.blaze3DFog().disable();

      final Stack stack = this.pipeline.getModelViewStack();
      stack.push();

      this.pipeline.disableCull();
      this.pipeline.enableBlend();
      this.pipeline.disableTexture();
      this.pipeline.enableDepthTest();
      this.pipeline.blendFunc(GFXBlendParameter.SOURCE_ALPHA, GFXBlendParameter.ONE_MINUS_SOURCE_ALPHA);
      this.pipeline.depthMask(true);

      this.bridge.lineWidth(LineStyle.DEFAULT_WIDTH);

      final Object oldShader = this.renderHelper.getShader();

      try {
        this.controller.renderSelections(this.context);
        this.sink.flush();
      } catch (final Exception exception) {
        exception.printStackTrace();
        this.invalidatePipeline();
      }

      this.pipeline.depthFunc(GFXAlphaFunction.LEQUAL);

      this.renderHelper.setShader(oldShader);

      this.pipeline.enableTexture();
      this.pipeline.disableBlend();
      this.pipeline.enableCull();
      stack.pop();
      this.pipeline.blaze3DFog().enable();

      this.helper.popProfiler();
    } catch (final Exception exception) {
      exception.printStackTrace();
      this.invalidatePipeline();
    }
  }

}
