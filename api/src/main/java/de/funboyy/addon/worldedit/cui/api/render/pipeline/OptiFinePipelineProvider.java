package de.funboyy.addon.worldedit.cui.api.render.pipeline;

import de.funboyy.addon.worldedit.cui.api.render.BufferBuilderRenderSink;
import net.labymod.api.Laby;
import net.labymod.api.thirdparty.optifine.OptiFine;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.PipelineProvider;
import org.enginehub.worldeditcui.render.RenderSink;

public final class OptiFinePipelineProvider implements PipelineProvider {

  // we only need to use debug, when using shaders if we have a version newer than 1.16.5,
  // so we just use the protocol version of 1.16.5 to check it
  private static final int SHADER_DEBUG_PROTOCOL_VERSION = 754;

  private final OptiFine optiFine = Laby.references().optiFine();
  private final ShaderAccessor shader = WorldEdit.references().shaderAccessor();
  private final int protocolVersion = Laby.labyAPI().minecraft().getProtocolVersion();

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
  public boolean useDebug() {
    if (!this.available()) {
      return false;
    }

    // only use debug if version is newer than 1.16.5
    if (this.protocolVersion <= SHADER_DEBUG_PROTOCOL_VERSION) {
      return false;
    }

    return this.optiFine.optiFineConfig().hasShaders();
  }

  @Override
  public RenderSink provide() {
    return new BufferBuilderRenderSink(
        this::useDebug,
        () -> {
          if (!this.available()) {
            return;
          }

          if (this.optiFine.optiFineConfig().hasShaders()) {
            this.shader.beginLeash();
          }
        },
        () -> {
          if (!this.available()) {
            return;
          }

          if (this.optiFine.optiFineConfig().hasShaders()) {
            this.shader.endLeash();
          }
        }
    );
  }

}
