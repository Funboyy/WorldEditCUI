package org.enginehub.worldeditcui.render.region;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import net.labymod.api.client.entity.Entity;
import org.enginehub.worldeditcui.InitialisationFactory;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.exceptions.InvalidSelectionTypeException;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.RenderStyle.RenderType;

public abstract class Region implements InitialisationFactory {

  protected final WorldEdit controller;
  protected final RenderStyle[] defaultStyles;
  protected RenderStyle[] styles;
  protected RenderType renderType = RenderType.ANY;

  protected Region(final WorldEdit controller, final RenderStyle... styles) {
    this.controller = controller;
    this.styles = this.defaultStyles = styles;
  }

  @Override
  public void initialise() {
  }

  public abstract void render(final RenderContext context);

  public RenderStyle[] getDefaultStyles() {
    return this.defaultStyles;
  }

  public void setRenderType(final RenderType renderType) {
    this.renderType = renderType;
    this.updateRenderStyle();
  }

  public void setStyles(final RenderStyle... styles) {
    if (styles.length < this.defaultStyles.length) {
      throw new IllegalArgumentException("Invalid colour palette supplied for " + this.getType().getName() + " region");
    }

    this.styles = styles;
    this.updateRenderStyle();
    this.updateStyles();
  }

  protected void updateRenderStyle() {
    for (final RenderStyle style : this.styles) {
      if (style != null) {
        style.setRenderType(this.renderType);
      }
    }
  }

  protected abstract void updateStyles();

  public void setGridSpacing(final double spacing) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setGridSpacing");
  }

  public void setCuboidPoint(final int id, final double x, final double y, final double z) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setCuboidPoint");
  }

  public void setCuboidVertexLatch(final int id, final Entity entity, final double traceDistance) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setCuboidVertexLatch");
  }

  public void setPolygonPoint(final int id, final int x, final int z) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setPolygonPoint");
  }

  public void setEllipsoidCenter(final int x, final int y, final int z) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setEllipsoidCenter");
  }

  public void setEllipsoidRadii(final double x, final double y, final double z) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setEllipsoidRadii");
  }

  public void setMinMax(final int min, final int max) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setMinMax");
  }

  public void setCylinderCenter(final int x, final int y, final int z) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setCylinderCenter");
  }

  public void setCylinderRadius(final double x, final double z) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "setCylinderRadius");
  }

  public void addPolygon(final int[] vertexIds) {
    throw new InvalidSelectionTypeException(this.getType().getName(), "addPolygon");
  }

  public abstract RegionType getType();

}
