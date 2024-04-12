package org.enginehub.worldeditcui.render.points;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import org.enginehub.worldeditcui.render.ConfiguredColor;
import org.enginehub.worldeditcui.render.RenderStyle;
import org.enginehub.worldeditcui.render.shapes.Render3DBox;
import org.enginehub.worldeditcui.util.BoundingBox;
import org.enginehub.worldeditcui.util.Observable;
import org.enginehub.worldeditcui.util.Vector3;

public class PointCube extends Observable<BoundingBox> {

  private static final double PADDING = 0.03;

  protected static final Vector3 MIN_VEC = new Vector3(PointCube.PADDING, PointCube.PADDING, PointCube.PADDING);
  protected static final Vector3 MAX_VEC = new Vector3(PointCube.PADDING + 1, PointCube.PADDING + 1, PointCube.PADDING + 1);

  protected int id;
  protected Vector3 point;
  protected RenderStyle style = ConfiguredColor.CUBOID_POINT_ONE.style();
  protected Render3DBox box;

  public PointCube(final double x, final double y, final double z) {
    this(new Vector3(x, y, z));
  }

  public PointCube(final Vector3 point) {
    this.setPoint(point);
  }

  public boolean isDynamic() {
    return false;
  }

  public PointCube setId(final int id) {
    this.id = id;
    return this;
  }

  public int getId() {
    return this.id;
  }

  public void render(final RenderContext context) {
    this.box.render(context);
  }

  public void updatePoint(final float partialTicks) {
  }

  public Vector3 getPoint() {
    return this.point;
  }

  public void setPoint(final Vector3 point) {
    this.point = point;
    this.update();
  }

  public RenderStyle getStyle() {
    return this.style;
  }

  public PointCube setStyle(final RenderStyle style) {
    this.style = style;
    this.update();
    return this;
  }

  private void update() {
    this.box = new Render3DBox(this.style, this.point.subtract(PointCube.MIN_VEC), this.point.add(PointCube.MAX_VEC));
  }

}
