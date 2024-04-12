package org.enginehub.worldeditcui.util;

import org.jetbrains.annotations.NotNull;

public class Vector3 implements Comparable<Vector3> {

  public final static Vector3 ZERO = new Vector3(0, 0, 0);
  public final static Vector3 UNIT_X = new Vector3(1, 0, 0);
  public final static Vector3 FORWARD = UNIT_X;
  public final static Vector3 UNIT_Y = new Vector3(0, 1, 0);
  public final static Vector3 UP = UNIT_Y;
  public final static Vector3 UNIT_Z = new Vector3(0, 0, 1);
  public final static Vector3 RIGHT = UNIT_Z;
  public final static Vector3 ONE = new Vector3(1, 1, 1);

  protected final double x, y, z;

  public Vector3(final double x, final double y, final double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3(final Double x, final Double y, final Double z) {
    this(x.doubleValue(), y.doubleValue(), z.doubleValue());
  }

  public Vector3() {
    this(0, 0, 0);
  }

  public Vector3(final Vector3 vector) {
    this(vector.getX(), vector.getY(), vector.getZ());
  }

  public Vector3(final Vector2 vector, final double z) {
    this(vector.getX(), vector.getY(), z);
  }

  public Vector3(final Vector2 vector) {
    this(vector, 0);
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public double getZ() {
    return this.z;
  }

  public Vector3 add(final Vector3 that) {
    return Vector3.add(this, that);
  }

  public Vector3 subtract(final Vector3 that) {
    return Vector3.subtract(this, that);
  }

  public Vector3 scale(final double scale) {
    return Vector3.scale(this, scale);
  }

  public double dot(final Vector3 that) {
    return Vector3.dot(this, that);
  }

  public Vector3 cross(final Vector3 that) {
    return Vector3.cross(this, that);
  }

  public Vector2 toVector2() {
    return Vector3.toVector2(this);
  }

  public Vector2m toVector2m() {
    return Vector3.toVector2m(this);
  }

  public Vector3 ceil() {
    return new Vector3(Math.ceil(this.x), Math.ceil(this.y), Math.ceil(this.z));
  }

  public Vector3 floor() {
    return new Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
  }

  public Vector3 round() {
    return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z));
  }

  public Vector3 abs() {
    return new Vector3(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
  }

  public double distance(final Vector3 vector) {
    return Vector3.distance(vector, this);
  }

  public Vector3 pow(final double power) {
    return Vector3.pow(this, power);
  }

  public double lengthSquared() {
    return Vector3.lengthSquared(this);
  }

  public double length() {
    return Vector3.length(this);
  }

  public double fastLength() {
    return Vector3.fastLength(this);
  }

  public Vector3 normalize() {
    return Vector3.normalize(this);
  }

  public double[] toArray() {
    return Vector3.toArray(this);
  }

  @Override
  public int compareTo(final @NotNull Vector3 vector) {
    return Vector3.compareTo(this, vector);
  }

  @Override
  public boolean equals(final Object object) {
    return Vector3.equals(this, object);
  }

  @Override
  public String toString() {
    return String.format("{ %f, %f, %f }", this.x, this.y, this.z);
  }

  public static double length(final Vector3 vector) {
    return Math.sqrt(lengthSquared(vector));
  }

  public static double fastLength(final Vector3 vector) {
    return Math.sqrt(lengthSquared(vector));
  }

  public static double lengthSquared(final Vector3 vector) {
    return Vector3.dot(vector, vector);
  }

  public static Vector3 normalize(final Vector3 vector) {
    return Vector3.scale(vector, (1.f / vector.length()));
  }

  public static Vector3 subtract(final Vector3 vector1, final Vector3 vector2) {
    return new Vector3(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
  }

  public static Vector3 add(final Vector3 vector1, final Vector3 vector2) {
    return new Vector3(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY(), vector1.getZ() + vector2.getZ());
  }

  public static Vector3 scale(final Vector3 vector, final double scale) {
    return new Vector3(vector.getX() * scale, vector.getY() * scale, vector.getZ() * scale);
  }

  public static double dot(final Vector3 vector1, final Vector3 vector2) {
    return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
  }

  public static Vector3 cross(final Vector3 vector1, final Vector3 vector2) {
    return new Vector3(vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY(), 
        vector1.getZ() * vector2.getX() - vector1.getX() * vector2.getZ(), 
        vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX());
  }

  public static Vector3 ceil(final Vector3 vector) {
    return new Vector3(Math.ceil(vector.x), Math.ceil(vector.y), Math.ceil(vector.z));
  }

  public static Vector3 floor(final Vector3 vector) {
    return new Vector3(Math.floor(vector.x), Math.floor(vector.y), Math.floor(vector.z));
  }

  public static Vector3 round(final Vector3 vector) {
    return new Vector3(Math.round(vector.x), Math.round(vector.y), Math.round(vector.z));
  }

  public static Vector3 abs(final Vector3 vector) {
    return new Vector3(Math.abs(vector.x), Math.abs(vector.y), Math.abs(vector.z));
  }

  public static Vector3 min(final Vector3 vector1, final Vector3 vector2) {
    return new Vector3(Math.min(vector1.x, vector2.x), Math.min(vector1.y, vector2.y), Math.min(vector1.z, vector2.z));
  }

  public static Vector3 max(final Vector3 vector1, final Vector3 vector2) {
    return new Vector3(Math.max(vector1.x, vector2.x), Math.max(vector1.y, vector2.y), Math.max(vector1.z, vector2.z));
  }

  public static Vector3 rand() {
    return new Vector3(Math.random(), Math.random(), Math.random());
  }

  public static double distance(final Vector3 vector1, final Vector3 vector2) {
    final double xzDist = Vector2.distance(vector1.toVector2(), vector2.toVector2());
    return Math.sqrt(Math.pow(xzDist, 2) + Math.pow(Math.abs(Vector3.subtract(vector1, vector2).getY()), 2));
  }

  public static Vector3 pow(final Vector3 vector, final double power) {
    return new Vector3(Math.pow(vector.x, power), Math.pow(vector.y, power), Math.pow(vector.z, power));
  }

  public static Vector2 toVector2(final Vector3 vector) {
    return new Vector2(vector.x, vector.z);
  }

  public static Vector2m toVector2m(final Vector3 vector) {
    return new Vector2m(vector.x, vector.z);
  }

  public static double[] toArray(final Vector3 vector) {
    return new double[] { vector.getX(), vector.getY(), vector.getZ() };
  }

  public static int compareTo(final Vector3 vector1, final Vector3 vector2) {
    return (int) vector1.lengthSquared() - (int) vector2.lengthSquared();
  }

  public static boolean equals(final Object object1, final Object object2) {
    if (!(object1 instanceof Vector3 vector1) || !(object2 instanceof Vector3 vector2)) {
      return false;
    }

    if (object1 == object2) {
      return true;
    }

    return vector1.getX() == vector2.getX() && vector1.getY() == vector2.getY() && vector1.getZ() == vector2.getZ();
  }

  @Override
  public int hashCode() {
    return (int) (this.x * this.y % this.z);
  }

}
