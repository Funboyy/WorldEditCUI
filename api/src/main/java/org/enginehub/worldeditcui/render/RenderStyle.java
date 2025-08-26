package org.enginehub.worldeditcui.render;

import net.labymod.api.util.Color;
import net.labymod.laby3d.api.pipeline.ComparisonStrategy;

public interface RenderStyle {

  enum RenderType {

    ANY(ComparisonStrategy.ALWAYS),
    HIDDEN(ComparisonStrategy.GEQUAL),
    VISIBLE(ComparisonStrategy.LESS);

    private final ComparisonStrategy strategy;

    RenderType(final ComparisonStrategy strategy) {
      this.strategy = strategy;
    }

    public boolean matches(final RenderType other) {
      return other == RenderType.ANY || other == this;
    }

    public ComparisonStrategy getStrategy() {
      return this.strategy;
    }

  }

  void setRenderType(final RenderType renderType);

  RenderType getRenderType();

  void setColor(final Color color);

  Color getColor();

  LineStyle[] getLines();

}
