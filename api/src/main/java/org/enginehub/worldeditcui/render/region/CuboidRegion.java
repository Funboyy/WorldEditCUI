package org.enginehub.worldeditcui.render.region;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import net.labymod.api.client.entity.Entity;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.ConfiguredColor;
import org.enginehub.worldeditcui.render.points.PointCube;
import org.enginehub.worldeditcui.render.points.PointCubeTracking;
import org.enginehub.worldeditcui.render.shapes.Render3DBox;
import org.enginehub.worldeditcui.render.shapes.Render3DGrid;
import org.enginehub.worldeditcui.util.BoundingBox;

public class CuboidRegion extends Region {

  private final PointCube[] points = new PointCube[2];

  private Render3DGrid grid;
  private Render3DBox box;

  private double spacing = 1.0;

  public CuboidRegion(final WorldEdit controller) {
    super(controller, ConfiguredColor.CUBOID_BOX.style(), ConfiguredColor.CUBOID_GRID.style(),
        ConfiguredColor.CUBOID_POINT_ONE.style(), ConfiguredColor.CUBOID_POINT_TWO.style());
  }

  @Override
  public void render(final RenderContext context) {
    if (this.points[0] != null && this.points[1] != null) {
      this.points[0].updatePoint(context.dt());
      this.points[1].updatePoint(context.dt());

      this.grid.render(context);
      this.box.render(context);

      this.points[0].render(context);
      this.points[1].render(context);
    }

    else if (this.points[0] != null) {
      this.points[0].updatePoint(context.dt());
      this.points[0].render(context);
    }

    else if (this.points[1] != null) {
      this.points[1].updatePoint(context.dt());
      this.points[1].render(context);
    }
  }

  @Override
  public void setGridSpacing(final double spacing) {
    this.spacing = spacing;

    if (this.grid != null) {
      this.grid.setSpacing(spacing);
    }
  }

  @Override
  public void setCuboidPoint(final int id, final double x, final double y, final double z) {
    if (id < 2) {
      this.points[id] = new PointCube(x, y, z).setStyle(this.styles[id+2]);
    }

    this.updateBounds();
  }

  @Override
  public void setCuboidVertexLatch(final int id, final Entity entity, final double traceDistance) {
    if (id < 2) {
      this.points[id] = new PointCubeTracking(entity, traceDistance).setStyle(this.styles[id+2]);
    }

    this.updateBounds();
  }

  private void updateBounds() {
    if (this.points[0] != null && this.points[1] != null) {
      final BoundingBox bounds = new BoundingBox(this.points[0], this.points[1]);

      this.grid = new Render3DGrid(this.styles[1], bounds).setSpacing(this.spacing);
      this.box = new Render3DBox(this.styles[0], bounds);
    }
  }

  @Override
  protected void updateStyles() {
    if (this.box != null) {
      this.box.setStyle(this.styles[0]);
    }

    if (this.grid != null) {
      this.grid.setStyle(this.styles[1]);
    }

    if (this.points[0] != null) {
      this.points[0].setStyle(this.styles[2]);
    }

    if (this.points[1] != null) {
      this.points[1].setStyle(this.styles[3]);
    }
  }

  @Override
  public RegionType getType() {
    return RegionType.CUBOID;
  }

}
