package de.funboyy.addon.worldedit.cui.api.render.pipeline;

import de.funboyy.addon.worldedit.cui.api.render.BufferBuilderRenderSink;
import net.labymod.api.Laby;
import net.labymod.api.thirdparty.optifine.OptiFine;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.PipelineProvider;
import org.enginehub.worldeditcui.render.RenderSink;

public final class OptiFinePipelineProvider implements PipelineProvider {

  private final OptiFine optiFine = Laby.references().optiFine();
  private final ShaderAccessor shader = WorldEdit.references().shaderAccessor();

  @Override
  public String id() {
    return "optifine";
  }

  @Override
  public boolean available() {
    return this.optiFine.isOptiFinePresent();
  }

  @Override
  public boolean shouldRender() {
    return !this.shader.isShadowPass();
  }

  @Override
  public RenderSink provide() {
    return new BufferBuilderRenderSink(
        () -> {
          if (!this.available()) {
            return;
          }

          if (this.optiFine.optiFineConfig().hasShaders()) {
            this.shader.begin();
          }
        },
        () -> {
          if (!this.available()) {
            return;
          }

          if (this.optiFine.optiFineConfig().hasShaders()) {
            this.shader.end();
          }
        }
    );
  }

}
