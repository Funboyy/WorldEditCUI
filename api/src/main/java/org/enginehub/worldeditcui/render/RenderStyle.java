package org.enginehub.worldeditcui.render;

import net.labymod.api.client.gfx.GlConst;
import net.labymod.api.util.Color;

public interface RenderStyle {

  enum RenderType {

    ANY(GlConst.GL_ALWAYS),
    HIDDEN(GlConst.GL_GEQUAL),
    VISIBLE(GlConst.GL_LESS);

    private final int depthFunction;

    RenderType(final int depthFunction) {
      this.depthFunction = depthFunction;
    }

    public boolean matches(final RenderType other) {
      return other == RenderType.ANY || other == this;
    }

    public int getDepthFunc() {
      return this.depthFunction;
    }

  }

  void setRenderType(final RenderType renderType);

  RenderType getRenderType();

  void setColor(final Color color);

  Color getColor();

  LineStyle[] getLines();

}
