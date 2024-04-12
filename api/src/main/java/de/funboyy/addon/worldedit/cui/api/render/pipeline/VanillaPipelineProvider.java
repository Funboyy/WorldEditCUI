package de.funboyy.addon.worldedit.cui.api.render.pipeline;

import de.funboyy.addon.worldedit.cui.api.render.BufferBuilderRenderSink;
import de.funboyy.addon.worldedit.cui.api.render.BufferBuilderRenderSink.Mode;
import de.funboyy.addon.worldedit.cui.api.render.BufferBuilderRenderSink.RenderType;
import de.funboyy.addon.worldedit.cui.api.render.BufferBuilderRenderSink.TypeFactory;
import net.labymod.api.client.render.vertex.VertexFormatType;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.PipelineProvider;
import org.enginehub.worldeditcui.render.RenderSink;

public final class VanillaPipelineProvider implements PipelineProvider {

  public static class DefaultTypeFactory implements TypeFactory {

    public static final DefaultTypeFactory INSTANCE = new DefaultTypeFactory();

    private static final RenderType QUADS = new RenderType(Mode.QUADS,
        VertexFormatType.POSITION_COLOR, WorldEdit.references().renderHelper().getPositionColorShader());
    private static final RenderType LINES = new RenderType(Mode.LINES,
        VertexFormatType.POSITION_COLOR_NORMAL, WorldEdit.references().renderHelper().getRenderTypeLinesShader());
    private static final RenderType LINES_LOOP = new RenderType(Mode.LINES,
        VertexFormatType.POSITION_COLOR_NORMAL, WorldEdit.references().renderHelper().getRenderTypeLinesShader());

    private DefaultTypeFactory() {}

    @Override
    public RenderType quads() {
      return QUADS;
    }

    @Override
    public RenderType lines() {
      return LINES;
    }

    @Override
    public RenderType linesLoop() {
      return LINES_LOOP;
    }

  }

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
    return new BufferBuilderRenderSink(DefaultTypeFactory.INSTANCE);
  }

}
