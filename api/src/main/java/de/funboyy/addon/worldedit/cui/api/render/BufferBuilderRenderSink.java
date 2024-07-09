package de.funboyy.addon.worldedit.cui.api.render;

import java.util.Objects;
import java.util.function.Supplier;
import net.labymod.api.Laby;
import net.labymod.api.client.gfx.color.GFXAlphaFunction;
import net.labymod.api.client.gfx.pipeline.Blaze3DGlStatePipeline;
import net.labymod.api.client.render.RenderPipeline;
import net.labymod.api.client.render.gl.GlStateBridge;
import net.labymod.api.client.render.vertex.BufferBuilder;
import net.labymod.api.client.render.vertex.VertexFormatType;
import net.labymod.api.util.math.vector.FloatVector3;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderSink;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.jetbrains.annotations.Nullable;

public class BufferBuilderRenderSink implements RenderSink {

  private static final RenderType QUADS = new RenderType(Mode.QUADS,
      VertexFormatType.POSITION_COLOR, WorldEdit.references().renderHelper()::getPositionColorShader);
  private static final RenderType LINES = new RenderType(Mode.LINES,
      VertexFormatType.POSITION_COLOR_NORMAL, WorldEdit.references().renderHelper()::getRenderTypeLinesShader);
  private static final RenderType LINES_LOOP = new RenderType(Mode.LINES,
      VertexFormatType.POSITION_COLOR_NORMAL, WorldEdit.references().renderHelper()::getRenderTypeLinesShader);

  private static final RenderType DEBUG_LINES = new RenderType(Mode.DEBUG_LINES,
      VertexFormatType.POSITION_COLOR, WorldEdit.references().renderHelper()::getPositionColorShader);
  private static final RenderType DEBUG_LINES_LOOP = new RenderType(Mode.DEBUG_LINES,
      VertexFormatType.POSITION_COLOR, WorldEdit.references().renderHelper()::getPositionColorShader);

  private final GlStateBridge bridge;
  private final RenderPipeline renderPipeline;
  private final Blaze3DGlStatePipeline statePipeline;
  private final RenderHelper renderHelper;

  private final Supplier<Boolean> debugSupplier;
  private final Runnable preFlush;
  private final Runnable postFlush;
  private BufferBuilder builder;
  private @Nullable RenderType activeRenderType;
  private boolean active;
  private boolean canFlush;
  private float red = -1f, green, blue, alpha;
  private double loopX, loopY, loopZ; // track previous vertices for lines_loop
  private double loopFirstX, loopFirstY, loopFirstZ; // track initial vertices for lines_loop
  private boolean canLoop;
  private boolean debug;

  // line state
  private float lastLineWidth = -1;
  private GFXAlphaFunction lastDepthFunc = null;

  public BufferBuilderRenderSink() {
    this(() -> false, () -> {}, () -> {});
  }

  public BufferBuilderRenderSink(final Supplier<Boolean> debugSupplier, final Runnable preFlush, final Runnable postFlush) {
    this.bridge = Laby.references().glStateBridge();
    this.renderPipeline = Laby.references().renderPipeline();
    this.statePipeline = Laby.references().blaze3DGlStatePipeline();
    this.renderHelper = WorldEdit.references().renderHelper();

    this.debugSupplier = debugSupplier;
    this.preFlush = preFlush;
    this.postFlush = postFlush;
  }

  @Override
  public RenderSink color(final float red, final float green, final float blue, final float alpha) {
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.alpha = alpha;
    return this;
  }

  @Override
  public boolean apply(final LineStyle line, final RenderStyle.RenderType type) {
    if (!line.renderType.matches(type)) {
      return false;
    }

    if (line.lineWidth == this.lastLineWidth && line.renderType.getDepthFunc() == this.lastDepthFunc) {
      return true;
    }

    this.flush();

    if (this.active && this.activeRenderType != null) {
      this.canFlush = true;
      this.builder = this.renderPipeline.createBufferBuilder();
      this.bridge.color4f(1.0f, 1.0f, 1.0f, 1.0f);
      this.builder.begin(this.activeRenderType.mode(), this.activeRenderType.type());
    }

    this.bridge.lineWidth(this.lastLineWidth = line.lineWidth);
    this.statePipeline.depthFunc(this.lastDepthFunc = line.renderType.getDepthFunc());

    return true;
  }

  @Override
  public RenderSink vertex(final double x, final double y, final double z) {
    if (this.red == -1f) {
      throw new IllegalStateException("No color has been set!");
    }

    if (!this.active) {
      throw new IllegalStateException("Tried to draw when not active");
    }

    final BufferBuilder builder = this.builder;
    if (this.activeRenderType == LINES_LOOP || this.activeRenderType == DEBUG_LINES_LOOP) {
      // duplicate last
      if (this.canLoop) {
        final FloatVector3 normal = this.activeRenderType.hasNormals() ?
            this.computeNormal(this.loopX, this.loopY, this.loopZ, x, y, z) : null;

        builder.vertex((float) this.loopX, (float) this.loopY, (float) this.loopZ)
            .color(this.red, this.green, this.blue, this.alpha);

        if (normal != null) {
          // we need to compute normals pointing directly towards the screen
          builder.normal(normal.getX(), normal.getY(), normal.getZ());
        }

        builder.next();
        builder.vertex((float) x, (float) y, (float) z).color(this.red, this.green, this.blue, this.alpha);

        if (normal != null) {
          builder.normal(normal.getX(), normal.getY(), normal.getZ());
        }

        builder.next();
      }

      else {
        this.loopFirstX = x;
        this.loopFirstY = y;
        this.loopFirstZ = z;
      }

      this.loopX = x;
      this.loopY = y;
      this.loopZ = z;
      this.canLoop = true;
    } else if (this.activeRenderType == LINES || this.activeRenderType == DEBUG_LINES) {
      // we buffer vertices so we can compute normals here
      if (this.canLoop) {
        final FloatVector3 normal = this.activeRenderType.hasNormals() ?
            this.computeNormal(this.loopX, this.loopY, this.loopZ, x, y, z) : null;

        builder.vertex((float) this.loopX, (float) this.loopY, (float) this.loopZ)
            .color(this.red, this.green, this.blue, this.alpha);

        if (normal != null) {
          builder.normal(normal.getX(), normal.getY(), normal.getZ());
        }

        builder.next();
        builder.vertex((float) x, (float) y, (float) z).color(this.red, this.green, this.blue, this.alpha);

        if (normal != null) {
          builder.normal(normal.getX(), normal.getY(), normal.getZ());
        }

        builder.next();
        this.canLoop = false;
      }

      else {
          this.loopX = x;
          this.loopY = y;
          this.loopZ = z;
          this.canLoop = true;
      }
    }

    else {
      builder.vertex((float) x, (float) y, (float) z).color(this.red, this.green, this.blue, this.alpha).next();
    }

    return this;
  }

  private FloatVector3 computeNormal(final double x0, final double y0, final double z0, final double x1, final double y1, final double z1) {
    // we need to compute normals so all drawn planes appear perpendicular to the screen
    final double dX = (x1 - x0);
    final double dY = (y1 - y0);
    final double dZ = (z1 - z0);
    final double length = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

    return new FloatVector3((float) (dX / length), (float) (dY / length), (float) (dZ / length));
  }

  @Override
  public RenderSink beginLineLoop() {
    this.debug = this.debugSupplier.get();

    this.transitionState(this.debug ? DEBUG_LINES_LOOP : LINES_LOOP);
    return this;
  }

  @Override
  public RenderSink endLineLoop() {
    this.end(this.debug ? DEBUG_LINES_LOOP : LINES_LOOP);

    if (!this.canLoop) {
      return this;
    }

    this.canLoop = false;

    final FloatVector3 normal = this.activeRenderType.hasNormals() ?
        this.computeNormal(this.loopX, this.loopY, this.loopZ, this.loopFirstX, this.loopFirstY, this.loopFirstZ) : null;

    this.builder.vertex((float) this.loopX, (float) this.loopY, (float) this.loopZ)
        .color(this.red, this.green, this.blue, this.alpha);

    if (normal != null) {
      this.builder.normal(normal.getX(), normal.getY(), normal.getZ());
    }

    this.builder.next();
    this.builder.vertex((float) this.loopFirstX, (float) this.loopFirstY, (float) this.loopFirstZ)
        .color(this.red, this.green, this.blue, this.alpha);

    if (normal != null) {
      this.builder.normal(normal.getX(), normal.getY(), normal.getZ());
    }

    this.builder.next();
    return this;
  }

  @Override
  public RenderSink beginLines() {
    this.debug = this.debugSupplier.get();

    this.transitionState(this.debug ? DEBUG_LINES : LINES);
    return this;
  }

  @Override
  public RenderSink endLines() {
    this.end(this.debug ? DEBUG_LINES : LINES);
    return this;
  }

  @Override
  public RenderSink beginQuads() {
    this.transitionState(QUADS);
    return this;
  }

  @Override
  public RenderSink endQuads() {
    this.end(QUADS);
    return this;
  }

  @Override
  public void flush() {
    if (!this.canFlush) {
      return;
    }

    if (this.active) {
      throw new IllegalStateException("Tried to flush while still active");
    }

    this.canFlush = false;
    this.preFlush.run();

    try {
      if (this.activeRenderType != null) {
        this.renderHelper.setShader(this.activeRenderType.shader());
      }

      this.renderHelper.endTesselator(this.builder);
    } finally {
      this.postFlush.run();
      this.builder = null;
      this.activeRenderType = null;
    }
  }

  private void end(final RenderType renderType) {
    if (!this.active) {
      throw new IllegalStateException("Could not exit " + renderType + ", was not active");
    }

    if (this.activeRenderType != renderType) {
      throw new IllegalStateException("Expected to end state " + renderType + " but was in " + this.activeRenderType);
    }

    this.active = false;
  }

  private void transitionState(final RenderType renderType) {
    if (this.active) {
      throw new IllegalStateException("Tried to enter new state before previous operation had been completed");
    }

    if (this.activeRenderType != null && renderType.mustFlushAfter(this.activeRenderType)) {
      this.flush();
    }

    if (this.activeRenderType == null || this.activeRenderType.mode() != renderType.mode()) {
      this.canFlush = true;
      this.bridge.color4f(1.0f, 1.0f, 1.0f, 1.0f);
      this.builder = this.renderPipeline.createBufferBuilder();
      this.builder.begin(renderType.mode(), renderType.type());
    }

    this.activeRenderType = renderType;
    this.active = true;
  }

  public static class Mode {

    public static final int QUADS = 7;
    public static final int LINES = 1;
    public static final int DEBUG_LINES = -5;

  }

  public static class RenderType {

    private final int mode;
    private final VertexFormatType type;
    private final boolean hasNormals;
    private final Supplier<Object> shaderSupplier;

    public RenderType(final int mode, final VertexFormatType type, final Supplier<Object> shaderSupplier) {
      this.mode = mode;
      this.type = type;
      this.hasNormals = type.vertexName().contains("normal");
      this.shaderSupplier = shaderSupplier;
    }

    int mode() {
      return this.mode;
    }

    VertexFormatType type() {
      return this.type;
    }

    boolean hasNormals() {
      return this.hasNormals;
    }

    Object shader() {
      return this.shaderSupplier.get();
    }

    boolean mustFlushAfter(final RenderType previous) {
      return previous.mode() != this.mode || !Objects.equals(previous.type(), this.type);
    }

  }

}
