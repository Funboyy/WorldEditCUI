package de.funboyy.addon.worldedit.cui.api.render;

import net.labymod.api.client.gfx.pipeline.util.MatrixTracker;
import net.labymod.api.util.Color;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderSink;
import org.enginehub.worldeditcui.render.RenderStyle.RenderType;
import org.enginehub.worldeditcui.util.Vector3;

public class RenderContext implements RenderSink {

  private Vector3 cameraPos;
  private float dt;
  private RenderSink delegateSink;

  public Vector3 cameraPos() {
    return this.cameraPos;
  }

  public void pop() {
    MatrixTracker.MODEL_VIEW_MATRIX.pop();
  }

  public void push() {
    MatrixTracker.MODEL_VIEW_MATRIX.push();
  }

  public void translate(final double x, final double y, final double z) {
    MatrixTracker.MODEL_VIEW_MATRIX.translate((float) x, (float) y, (float) z);
  }

  public float dt() {
    return this.dt;
  }

  public void init(final Vector3 cameraPos, final float dt, final RenderSink sink) {
    this.cameraPos = cameraPos;
    this.dt = dt;
    this.delegateSink = sink;
  }

  @Override
  public RenderContext color(final float red, final float green, final float blue, final float alpha) {
    this.delegateSink.color(red, green, blue, alpha);
    return this;
  }

  @Override
  public RenderContext color(final Color color) {
    this.delegateSink.color(color);
    return this;
  }

  @Override
  public boolean apply(final LineStyle line, final RenderType type) {
    return this.delegateSink.apply(line, type);
  }

  @Override
  public RenderContext vertex(final double x, final double y, final double z) {
    this.delegateSink.vertex(x, y, z);
    return this;
  }

  @Override
  public RenderContext beginLineLoop() {
    this.delegateSink.beginLineLoop();
    return this;
  }

  @Override
  public RenderContext endLineLoop() {
    this.delegateSink.endLineLoop();
    return this;
  }

  @Override
  public RenderSink beginLines() {
    this.delegateSink.beginLines();
    return this;
  }

  @Override
  public RenderSink endLines() {
    this.delegateSink.endLines();
    return this;
  }

  @Override
  public RenderContext beginQuads() {
    this.delegateSink.beginQuads();
    return this;
  }

  @Override
  public RenderContext endQuads() {
    this.delegateSink.endQuads();
    return this;
  }

  @Override
  public void flush() {
    this.delegateSink.flush();
  }

}
