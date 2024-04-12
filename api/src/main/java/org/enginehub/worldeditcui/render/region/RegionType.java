package org.enginehub.worldeditcui.render.region;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.enginehub.worldeditcui.WorldEdit;

public enum RegionType {

  CUBOID("cuboid", "Cuboid", CuboidRegion::new),
  POLYGON("polygon2d", "2D Polygon", PolygonRegion::new),
  ELLIPSOID("ellipsoid", "Ellipsoid", EllipsoidRegion::new),
  CYLINDER("cylinder", "Cylinder", CylinderRegion::new),
  POLYHEDRON("polyhedron", "Polyhedron", PolyhedronRegion::new);

  private static final Map<String, RegionType> BY_NAME = new HashMap<>();

  private final String key;
  private final String name;
  private final Function<WorldEdit, Region> maker;

  RegionType(final String key, final String name, final Function<WorldEdit, Region> maker) {
    this.key = key;
    this.name = name;
    this.maker = maker;
  }

  public String getKey() {
    return this.key;
  }

  public String getName() {
    return this.name;
  }

  public Region make(final WorldEdit controller) {
    return this.maker.apply(controller);
  }

  static {
    for (final RegionType type : values()) {
      BY_NAME.put(type.getKey(), type);
    }
  }

  public static RegionType named(final String key) {
    return BY_NAME.get(key);
  }

}
