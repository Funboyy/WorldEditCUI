package org.enginehub.worldeditcui.render.shapes;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.util.Observable;
import org.enginehub.worldeditcui.util.Observer;

public abstract class RenderRegion implements Observer {

  protected static final double OFFSET = 0.001d; // to avoid z-fighting with blocks

  protected RenderStyle style;

  protected RenderRegion(final RenderStyle style) {
    this.style = style;
  }

  public final void setStyle(final RenderStyle style) {
    this.style = style;
  }

  public abstract void render(final RenderContext context);

  @Override
  public void notifyChanged(final Observable<?> source) {
  }

}
