package de.funboyy.addon.worldedit.cui.v1_21_5;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;

public class WorldEditRenderPipelines {

  private static final BlendFunction WORLD_EDIT;

  private static final RenderPipeline.Snippet MATRICES_SNIPPET;
  private static final RenderPipeline.Snippet MATRICES_COLOR_SNIPPET;
  private static final RenderPipeline.Snippet MATRICES_COLOR_FOG_SNIPPET;
  private static final RenderPipeline.Snippet LINES_SNIPPET;

  public static final RenderPipeline QUADS;
  public static final RenderPipeline LINES;
  public static final RenderPipeline DEBUG_LINES;

  private static ResourceLocation buildLocation(String path) {
    return ResourceLocation.tryBuild("worldedit", "pipeline/" + path);
  }

  static {
     WORLD_EDIT = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

    MATRICES_SNIPPET = RenderPipeline.builder()
        .withUniform("ModelViewMat", UniformType.MATRIX4X4)
        .withUniform("ProjMat", UniformType.MATRIX4X4)
        .buildSnippet();
    MATRICES_COLOR_SNIPPET = RenderPipeline.builder(MATRICES_SNIPPET)
        .withUniform("ColorModulator", UniformType.VEC4)
        .buildSnippet();
    MATRICES_COLOR_FOG_SNIPPET = RenderPipeline.builder(MATRICES_COLOR_SNIPPET)
        .withUniform("FogStart", UniformType.FLOAT)
        .withUniform("FogEnd", UniformType.FLOAT)
        .withUniform("FogShape", UniformType.INT)
        .withUniform("FogColor", UniformType.VEC4)
        .buildSnippet();
    LINES_SNIPPET = RenderPipeline.builder(MATRICES_COLOR_FOG_SNIPPET)
        .withUniform("LineWidth", UniformType.FLOAT)
        .withUniform("ScreenSize", UniformType.VEC2)
        .buildSnippet();

    QUADS = RenderPipeline.builder(MATRICES_COLOR_SNIPPET)
        .withLocation(buildLocation("quads"))
        .withVertexShader("core/position_color")
        .withFragmentShader("core/position_color")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
        .withBlend(WORLD_EDIT)
        .withCull(false)
        .build();

    LINES = RenderPipeline.builder(LINES_SNIPPET)
        .withLocation(buildLocation("lines"))
        .withVertexShader("core/rendertype_lines")
        .withFragmentShader("core/rendertype_lines")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES)
        .withBlend(WORLD_EDIT)
        .withCull(false)
        .build();

    DEBUG_LINES = RenderPipeline.builder(MATRICES_COLOR_SNIPPET)
        .withLocation(buildLocation("debug_lines"))
        .withVertexShader("core/position_color")
        .withFragmentShader("core/position_color")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINES)
        .withBlend(WORLD_EDIT)
        .withCull(false)
        .build();
  }

}
