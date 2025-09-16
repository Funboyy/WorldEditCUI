package de.funboyy.addon.worldedit.cui.api.render;

import net.labymod.api.laby3d.pipeline.RenderStates;
import net.labymod.api.laby3d.vertex.VertexDescriptions;
import net.labymod.laby3d.api.pipeline.ComparisonStrategy;
import net.labymod.laby3d.api.pipeline.DrawingMode;
import net.labymod.laby3d.api.pipeline.RenderState;
import net.labymod.laby3d.api.pipeline.blend.DefaultBlendFunctions;
import net.labymod.laby3d.api.pipeline.shader.ShaderProgramDescription;
import net.labymod.laby3d.api.resource.AssetId;
import org.jetbrains.annotations.Nullable;

public class WorldEditRenderStates {

  private static AssetId buildStateId(final String name) {
    return AssetId.of("worldedit", "renderstate/" + name);
  }

  private static AssetId buildProgramId(final String name) {
    return AssetId.of("worldedit", "shaderprogram/" + name);
  }

  // Quads

  public static final RenderState QUADS = RenderState.builder()
      .setId(buildStateId("quads"))
      .setVertexDescription(VertexDescriptions.POSITION_COLOR)
      .setDrawingMode(DrawingMode.QUADS)
      .setBlendFunction(DefaultBlendFunctions.TRANSLUCENT)
      .setCull(false)
      .setShaderProgramDescription(
          ShaderProgramDescription.builder(RenderStates.DEFAULT_SHADER_SNIPPET)
              .setId(buildProgramId("quads"))
              .setVertexShader(RenderStates.SHADER_RESOLVER.apply("core/position_color.vsh"))
              .setFragmentShader(RenderStates.SHADER_RESOLVER.apply("core/position_color.fsh"))
              .build()
      )
      .build();

  private static final RenderState QUADS_ANY = RenderState.builder(QUADS.toSnippet())
      .setId(buildStateId("quads_any"))
      .setDepthTestStrategy(ComparisonStrategy.ALWAYS)
      .setWriteDepth(true)
      .build();

  private static final RenderState QUADS_HIDDEN = RenderState.builder(QUADS.toSnippet())
      .setId(buildStateId("quads_hidden"))
      .setDepthTestStrategy(ComparisonStrategy.GEQUAL)
      .setWriteDepth(false)
      .build();

  private static final RenderState QUADS_VISIBLE = RenderState.builder(QUADS.toSnippet())
      .setId(buildStateId("quads_visible"))
      .setDepthTestStrategy(ComparisonStrategy.LESS)
      .setWriteDepth(true)
      .build();

  // Lines

  public static final RenderState LINES = RenderState.builder()
      .setId(buildStateId("lines"))
      .setVertexDescription(VertexDescriptions.POSITION_COLOR_NORMAL)
      .setDrawingMode(DrawingMode.LINES)
      .setBlendFunction(DefaultBlendFunctions.TRANSLUCENT)
      .setCull(false)
      .setShaderProgramDescription(
          ShaderProgramDescription.builder(RenderStates.DEFAULT_SHADER_SNIPPET)
              .setId(buildProgramId("lines"))
              .setVertexShader(RenderStates.SHADER_RESOLVER.apply("core/lines.vsh"))
              .setFragmentShader(RenderStates.SHADER_RESOLVER.apply("core/lines.fsh"))
              .build()
      )
      .build();

  private static final RenderState LINES_ANY = RenderState.builder(LINES.toSnippet())
      .setId(buildStateId("lines_any"))
      .setDepthTestStrategy(ComparisonStrategy.ALWAYS)
      .setWriteDepth(true)
      .build();

  private static final RenderState LINES_HIDDEN = RenderState.builder(LINES.toSnippet())
      .setId(buildStateId("lines_hidden"))
      .setDepthTestStrategy(ComparisonStrategy.GEQUAL)
      .setWriteDepth(false)
      .build();

  private static final RenderState LINES_VISIBLE = RenderState.builder(LINES.toSnippet())
      .setId(buildStateId("lines_visible"))
      .setDepthTestStrategy(ComparisonStrategy.LESS)
      .setWriteDepth(true)
      .build();

  // Util

  public static RenderState get(final DrawingMode mode, @Nullable final ComparisonStrategy strategy) {
    if (mode == DrawingMode.QUADS) {
      return switch (strategy) {
        case ALWAYS -> QUADS_ANY;
        case GEQUAL -> QUADS_HIDDEN;
        case null, default -> QUADS_VISIBLE;
      };
    }

    if (mode == DrawingMode.LINES) {
      return switch (strategy) {
        case ALWAYS -> LINES_ANY;
        case GEQUAL -> LINES_HIDDEN;
        case null, default -> LINES_VISIBLE;
      };
    }

    return null;
  }

}
