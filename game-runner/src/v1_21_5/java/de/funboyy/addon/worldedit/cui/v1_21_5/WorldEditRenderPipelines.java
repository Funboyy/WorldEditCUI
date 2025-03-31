package de.funboyy.addon.worldedit.cui.v1_21_5;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
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

  private static final RenderPipeline.Snippet QUADS;
  private static final RenderPipeline.Snippet LINES;
  private static final RenderPipeline.Snippet DEBUG_LINES;

  public static final RenderPipeline QUADS_ANY;
  public static final RenderPipeline QUADS_HIDDEN;
  public static final RenderPipeline QUADS_VISIBLE;

  public static final RenderPipeline LINES_ANY;
  public static final RenderPipeline LINES_HIDDEN;
  public static final RenderPipeline LINES_VISIBLE;

  public static final RenderPipeline DEBUG_LINES_ANY;
  public static final RenderPipeline DEBUG_LINES_HIDDEN;
  public static final RenderPipeline DEBUG_LINES_VISIBLE;

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
        .withVertexShader("core/position_color")
        .withFragmentShader("core/position_color")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
        .withBlend(WORLD_EDIT)
        .withCull(false)
        .buildSnippet();

    LINES = RenderPipeline.builder(LINES_SNIPPET)
        .withVertexShader("core/rendertype_lines")
        .withFragmentShader("core/rendertype_lines")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES)
        .withBlend(WORLD_EDIT)
        .withCull(false)
        .buildSnippet();

    DEBUG_LINES = RenderPipeline.builder(MATRICES_COLOR_SNIPPET)
        .withVertexShader("core/position_color")
        .withFragmentShader("core/position_color")
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINES)
        .withBlend(WORLD_EDIT)
        .withCull(false)
        .buildSnippet();

    QUADS_ANY = RenderPipeline.builder(QUADS)
        .withLocation(buildLocation("quads_any"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build();
    QUADS_HIDDEN = RenderPipeline.builder(QUADS)
        .withLocation(buildLocation("quads_hidden"))
        .withDepthTestFunction(DepthTestFunction.GREATER_DEPTH_TEST)
        .withDepthWrite(false)
        .build();
    QUADS_VISIBLE = RenderPipeline.builder(QUADS)
        .withLocation(buildLocation("quads_visible"))
        .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
        .withDepthWrite(true)
        .build();

    LINES_ANY = RenderPipeline.builder(LINES)
        .withLocation(buildLocation("lines_any"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build();
    LINES_HIDDEN = RenderPipeline.builder(LINES)
        .withLocation(buildLocation("lines_hidden"))
        .withDepthTestFunction(DepthTestFunction.GREATER_DEPTH_TEST)
        .withDepthWrite(false)
        .build();
    LINES_VISIBLE = RenderPipeline.builder(LINES)
        .withLocation(buildLocation("lines_visible"))
        .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
        .withDepthWrite(true)
        .build();

    DEBUG_LINES_ANY = RenderPipeline.builder(DEBUG_LINES)
        .withLocation(buildLocation("debug_lines_any"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build();
    DEBUG_LINES_HIDDEN = RenderPipeline.builder(DEBUG_LINES)
        .withLocation(buildLocation("debug_lines_hidden"))
        .withDepthTestFunction(DepthTestFunction.GREATER_DEPTH_TEST)
        .withDepthWrite(false)
        .build();
    DEBUG_LINES_VISIBLE = RenderPipeline.builder(DEBUG_LINES)
        .withLocation(buildLocation("debug_lines_visible"))
        .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
        .withDepthWrite(true)
        .build();
  }

  public enum Pipeline {

    QUADS(QUADS_ANY, QUADS_HIDDEN, QUADS_VISIBLE),
    LINES(LINES_ANY, LINES_HIDDEN, LINES_VISIBLE),
    DEBUG_LINES(DEBUG_LINES_ANY, DEBUG_LINES_HIDDEN, DEBUG_LINES_VISIBLE);

    private final RenderPipeline any;
    private final RenderPipeline hidden;
    private final RenderPipeline visible;

    Pipeline(final RenderPipeline any, final RenderPipeline hidden, final RenderPipeline visible) {
      this.any = any;
      this.hidden = hidden;
      this.visible = visible;
    }

    public RenderPipeline any() {
      return this.any;
    }

    public RenderPipeline hidden() {
      return this.hidden;
    }

    public RenderPipeline visible() {
      return this.visible;
    }

  }

}
