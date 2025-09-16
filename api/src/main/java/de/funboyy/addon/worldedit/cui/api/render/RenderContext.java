package de.funboyy.addon.worldedit.cui.api.render;

import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.Color;
import org.enginehub.worldeditcui.render.LineStyle;
import org.enginehub.worldeditcui.render.RenderSink;
import org.enginehub.worldeditcui.render.RenderStyle.RenderType;
import org.enginehub.worldeditcui.util.Vector3;

public class RenderContext implements RenderSink {

  private Stack stack;
  private Vector3 cameraPos;
  private float tickDelta;
  private RenderSink delegateSink;

  public Vector3 cameraPos() {
    return this.cameraPos;
  }

  public float tickDelta() {
    return this.tickDelta;
  }

  public void pop() {
    this.stack.pop();
  }

  public void push() {
    this.stack.push();
  }

  public void translate(final double x, final double y, final double z) {
    this.stack.translate((float) x, (float) y, (float) z);
  }

  public void init(final Vector3 cameraPos, final float tickDelta, final RenderSink sink) {
    this.cameraPos = cameraPos;
    this.tickDelta = tickDelta;
    this.delegateSink = sink;
  }

  @Override
  public RenderContext stack(final Stack stack) {
    this.stack = stack;
    this.delegateSink.stack(stack);
    return this;
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
