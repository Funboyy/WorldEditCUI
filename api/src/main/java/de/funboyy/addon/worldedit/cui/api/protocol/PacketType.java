package de.funboyy.addon.worldedit.cui.api.protocol;

public enum PacketType {

  VERSION("Version", "v", 1),
  SELECTION("Selection", "s", 1, 2),
  POINT("Point3D", "p", 5, 6),
  POINT2D("Point2D", "p2", 4, 5),
  ELLIPSOID("Ellipsoid", "e", 4),
  CYLINDER("Cylinder", "cyl", 5),
  MINMAX("Bounds", "mm", 2),
  UPDATE("Update", "u", 1),
  POLYGON("Polygon", "poly", 3, 99),
  COLOR("Color", "col", 4),
  GRID("Grid", "grid", 1, 2);

  private final String name;
  private final String typeId;
  private final int minParameters;
  private final int maxParameters;

  PacketType(final String name, final String typeId, final int parameter) {
    this(name, typeId, parameter, parameter);
  }

  PacketType(final String name, final String typeId, final int minParameters, final int maxParameters) {
    this.name = name;
    this.typeId = typeId;
    this.minParameters = minParameters;
    this.maxParameters = maxParameters;
  }

  public String getName() {
    return this.name;
  }

  public String getTypeId() {
    return this.typeId;
  }

  public int getMinParameters() {
    return this.minParameters;
  }

  public int getMaxParameters() {
    return this.maxParameters;
  }

}
