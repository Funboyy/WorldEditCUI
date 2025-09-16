package org.enginehub.worldeditcui.render;

import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.Color;
import org.enginehub.worldeditcui.render.RenderStyle.RenderType;

public interface RenderSink {

  RenderSink stack(final Stack stack);

  RenderSink color(final float red, final float green, final float blue, float alpha);

  default RenderSink color(final Color color) {
    return this.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
  }

  default RenderSink color(final LineStyle style) {
    return this.color(style.red / 255f, style.green / 255f, style.blue / 255f, style.alpha / 255f);
  }

  default RenderSink color(final LineStyle style, final float tint) {
    return this.color(style.red / 255f, style.green / 255f, style.blue / 255f, (style.alpha / 255f) * tint);
  }

  boolean apply(final LineStyle line, final RenderType type);

  RenderSink vertex(final double x, final double y, final double z);

  RenderSink beginLineLoop();

  RenderSink endLineLoop();

  RenderSink beginLines();

  RenderSink endLines();

  RenderSink beginQuads();

  RenderSink endQuads();

  void flush();

}
