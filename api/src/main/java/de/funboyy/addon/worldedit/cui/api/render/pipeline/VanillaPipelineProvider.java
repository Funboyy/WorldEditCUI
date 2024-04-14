package de.funboyy.addon.worldedit.cui.api.render.pipeline;

import de.funboyy.addon.worldedit.cui.api.render.BufferBuilderRenderSink;
import org.enginehub.worldeditcui.render.PipelineProvider;
import org.enginehub.worldeditcui.render.RenderSink;

public final class VanillaPipelineProvider implements PipelineProvider {

  @Override
  public String id() {
    return "vanilla";
  }

  @Override
  public boolean available() {
    return true;
  }

  @Override
  public RenderSink provide() {
    return new BufferBuilderRenderSink();
  }

}
