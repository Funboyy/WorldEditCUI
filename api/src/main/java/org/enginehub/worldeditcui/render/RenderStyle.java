package org.enginehub.worldeditcui.render;

import net.labymod.api.client.gfx.color.GFXAlphaFunction;
import net.labymod.api.util.Color;

public interface RenderStyle {

  enum RenderType {

    ANY(GFXAlphaFunction.ALWAYS),
    HIDDEN(GFXAlphaFunction.GEQUAL),
    VISIBLE(GFXAlphaFunction.LESS);

    private final GFXAlphaFunction alphaFunction;

    RenderType(final GFXAlphaFunction alphaFunction) {
      this.alphaFunction = alphaFunction;
    }

    public boolean matches(final RenderType other) {
      return other == RenderType.ANY || other == this;
    }

    public GFXAlphaFunction getDepthFunc() {
      return this.alphaFunction;
    }

  }

  void setRenderType(final RenderType renderType);

  RenderType getRenderType();

  void setColor(final Color color);

  Color getColor();

  LineStyle[] getLines();

}
