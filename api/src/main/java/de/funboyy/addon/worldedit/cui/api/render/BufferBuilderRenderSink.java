package de.funboyy.addon.worldedit.cui.api.render;

import java.util.Objects;
import net.labymod.api.Laby;
import net.labymod.api.client.gfx.pipeline.renderer.mesh.MeshRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.laby3d.GameTransformations;
import net.labymod.api.laby3d.Laby3D;
import net.labymod.api.laby3d.math.JomlMath;
import net.labymod.api.laby3d.shaders.block.DynamicTransformsUniformBlock;
import net.labymod.api.laby3d.shaders.block.DynamicTransformsUniformBlockData;
import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.util.RenderUtil;
import net.labymod.api.util.math.vector.FloatVector3;
import net.labymod.laby3d.api.buffers.BufferBuilder;
import net.labymod.laby3d.api.pipeline.ComparisonStrategy;
import net.labymod.laby3d.api.pipeline.DrawingMode;
import net.labymod.laby3d.api.pipeline.RenderState;
import net.labymod.laby3d.api.pipeline.pass.DrawRenderCommand;
import net.labymod.laby3d.api.vertex.VertexAttributes;
import net.labymod.laby3d.api.vertex.VertexDescription;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderSink;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class BufferBuilderRenderSink implements RenderSink {

  // only needed while there is no build in way to change the line width via LabyMod
  private static final boolean USE_IDENTITY_MATRIX = MinecraftVersions.V1_12_2.orOlder();
  private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();

  private static final RenderType QUADS = new RenderType(WorldEditRenderStates.QUADS);
  private static final RenderType LINES = new RenderType(WorldEditRenderStates.LINES);
  private static final RenderType LINES_LOOP = new RenderType(WorldEditRenderStates.LINES);

  private final Laby3D laby3D;
  private final GameTransformations transformations;

  private final Runnable preFlush;
  private final Runnable postFlush;
  private Stack stack;
  private BufferBuilder builder;
  private @Nullable RenderType activeRenderType;
  private boolean active;
  private boolean canFlush;
  private float red = -1f, green, blue, alpha;
  private double loopX, loopY, loopZ; // track previous vertices for lines_loop
  private double loopFirstX, loopFirstY, loopFirstZ; // track initial vertices for lines_loop
  private boolean canLoop;

  // line state
  private float lastLineWidth = LineStyle.DEFAULT_WIDTH;
  private ComparisonStrategy lastStrategy = null;

  public BufferBuilderRenderSink() {
    this(() -> {}, () -> {});
  }

  public BufferBuilderRenderSink(final Runnable preFlush, final Runnable postFlush) {
    this.laby3D = Laby.references().laby3D();
    this.transformations = Laby.references().gameTransformations();

    this.preFlush = preFlush;
    this.postFlush = postFlush;
  }

  @Override
  public RenderSink stack(final Stack stack) {
    this.stack = stack;
    return this;
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

    if (line.lineWidth == this.lastLineWidth && line.renderType.getStrategy() == this.lastStrategy) {
      return true;
    }

    this.flush();

    if (this.active && this.activeRenderType != null) {
      this.canFlush = true;
      this.builder = this.laby3D.begin(this.activeRenderType.mode(), this.activeRenderType.description());
    }

    this.lastLineWidth = line.lineWidth;
    this.lastStrategy = line.renderType.getStrategy();

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

    final Matrix4f pose = this.stack.getProvider().getPose();
    final BufferBuilder builder = this.builder;

    if (this.activeRenderType == LINES_LOOP) {
      // duplicate last
      if (this.canLoop) {
        final FloatVector3 normal = this.activeRenderType.hasNormals() ?
            this.computeNormal(this.loopX, this.loopY, this.loopZ, x, y, z) : null;

        builder.addVertex(pose, (float) this.loopX, (float) this.loopY, (float) this.loopZ)
            .setColor(this.red, this.green, this.blue, this.alpha);

        if (normal != null) {
          // we need to compute normals pointing directly towards the screen
          builder.setNormal(normal.getX(), normal.getY(), normal.getZ());
        }

        builder.addVertex(pose, (float) x, (float) y, (float) z).setColor(this.red, this.green, this.blue, this.alpha);

        if (normal != null) {
          builder.setNormal(normal.getX(), normal.getY(), normal.getZ());
        }
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
    } else if (this.activeRenderType == LINES) {
      // we buffer vertices so we can compute normals here
      if (this.canLoop) {
        final FloatVector3 normal = this.activeRenderType.hasNormals() ?
            this.computeNormal(this.loopX, this.loopY, this.loopZ, x, y, z) : null;

        builder.addVertex(pose, (float) this.loopX, (float) this.loopY, (float) this.loopZ)
            .setColor(this.red, this.green, this.blue, this.alpha);

        if (normal != null) {
          builder.setNormal(normal.getX(), normal.getY(), normal.getZ());
        }

        builder.addVertex(pose, (float) x, (float) y, (float) z).setColor(this.red, this.green, this.blue, this.alpha);

        if (normal != null) {
          builder.setNormal(normal.getX(), normal.getY(), normal.getZ());
        }

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
      builder.addVertex(pose, (float) x, (float) y, (float) z).setColor(this.red, this.green, this.blue, this.alpha);
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
    this.transitionState(LINES_LOOP);
    return this;
  }

  @Override
  public RenderSink endLineLoop() {
    this.end(LINES_LOOP);

    if (!this.canLoop) {
      return this;
    }

    this.canLoop = false;

    final Matrix4f pose = this.stack.getProvider().getPose();
    final FloatVector3 normal = this.activeRenderType.hasNormals() ?
        this.computeNormal(this.loopX, this.loopY, this.loopZ, this.loopFirstX, this.loopFirstY, this.loopFirstZ) : null;

    this.builder.addVertex(pose, (float) this.loopX, (float) this.loopY, (float) this.loopZ)
        .setColor(this.red, this.green, this.blue, this.alpha);

    if (normal != null) {
      this.builder.setNormal(normal.getX(), normal.getY(), normal.getZ());
    }

    this.builder.addVertex(pose, (float) this.loopFirstX, (float) this.loopFirstY, (float) this.loopFirstZ)
        .setColor(this.red, this.green, this.blue, this.alpha);

    if (normal != null) {
      this.builder.setNormal(normal.getX(), normal.getY(), normal.getZ());
    }

    return this;
  }

  @Override
  public RenderSink beginLines() {
    this.transitionState(LINES);
    return this;
  }

  @Override
  public RenderSink endLines() {
    this.end(LINES);
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
        MeshRenderer.drawImmediate(this.builder.build(),
            WorldEditRenderStates.get(this.activeRenderType.mode(), this.lastStrategy),
            this::setupDynamicTransformsUniform);
      }
    } finally {
      this.postFlush.run();
      this.builder = null;
      this.activeRenderType = null;
    }
  }

  // only needed while there is no build in way to change the line width via LabyMod
  private void setupDynamicTransformsUniform(final DrawRenderCommand command) {
    Matrix4f modelViewMatrix = USE_IDENTITY_MATRIX ? IDENTITY_MATRIX : this.transformations.modelViewMatrix();

    final Matrix4f offscreenModelViewMatrix = RenderUtil.getOffscreenModelViewMatrix();

    if (offscreenModelViewMatrix != null) {
      modelViewMatrix = offscreenModelViewMatrix;
    }

    final DynamicTransformsUniformBlockData blockData = new DynamicTransformsUniformBlockData();
    blockData.setLineWidth(this.lastLineWidth);
    blockData.modelViewMatrix().set(JomlMath.cloneMatrix(modelViewMatrix));

    command.setUniformBlockData(DynamicTransformsUniformBlock.NAME, blockData);
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
      this.builder = this.laby3D.begin(renderType.mode(), renderType.description());
    }

    this.activeRenderType = renderType;
    this.active = true;
  }

  public static class RenderType {

    private final DrawingMode mode;
    private final VertexDescription description;
    private final boolean hasNormals;

    public RenderType(final RenderState renderState) {
      this.mode = renderState.drawingMode();
      this.description = renderState.vertexDescription();
      this.hasNormals = this.description.containsAttribute(VertexAttributes.NORMAL);
    }

    DrawingMode mode() {
      return this.mode;
    }

    VertexDescription description() {
      return this.description;
    }

    boolean hasNormals() {
      return this.hasNormals;
    }

    boolean mustFlushAfter(final RenderType previous) {
      return previous.mode() != this.mode || !Objects.equals(previous.description(), this.description);
    }

  }

}
