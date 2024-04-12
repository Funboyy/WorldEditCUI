package org.enginehub.worldeditcui.util;

import org.enginehub.worldeditcui.render.points.PointCube;
import org.enginehub.worldeditcui.render.shapes.RenderRegion;

public class BoundingBox extends Observable<RenderRegion> implements Observer {

  private static final double OFF = 0.02;

  private static final Vector3 MIN_VEC = new Vector3(BoundingBox.OFF, BoundingBox.OFF, BoundingBox.OFF);
  private static final Vector3 MAX_VEC = new Vector3(BoundingBox.OFF + 1, BoundingBox.OFF + 1, BoundingBox.OFF + 1);

  private final PointCube pointCube1, pointCube2;
  private Vector3 min, max;

  public BoundingBox(final PointCube pointCube1, final PointCube pointCube2)
  {
    this.pointCube1 = pointCube1;
    this.pointCube2 = pointCube2;

    this.update();

    if (this.pointCube1.isDynamic()) {
      this.pointCube1.addObserver(this);
    }

    if (this.pointCube2.isDynamic()) {
      this.pointCube2.addObserver(this);
    }
  }

  public Vector3 getMin() {
    return this.min;
  }

  public Vector3 getMax() {
    return this.max;
  }

  public boolean isDynamic() {
    return this.pointCube1.isDynamic() || this.pointCube2.isDynamic();
  }

  @Override
  public void notifyChanged(final Observable<?> source) {
    if (source == this.pointCube1 || source == this.pointCube2) {
      this.update();
      this.notifyObservers();
    }
  }

  private void update() {
    final Vector3 point1 = this.pointCube1.getPoint();
    final Vector3 point2 = this.pointCube2.getPoint();

    this.min = new Vector3(
        Math.min(point1.getX(), point2.getX()),
        Math.min(point1.getY(), point2.getY()),
        Math.min(point1.getZ(), point2.getZ())
    ).subtract(BoundingBox.MIN_VEC);

    this.max = new Vector3(
        Math.max(point1.getX(), point2.getX()),
        Math.max(point1.getY(), point2.getY()),
        Math.max(point1.getZ(), point2.getZ())
    ).add(BoundingBox.MAX_VEC);
  }

}
