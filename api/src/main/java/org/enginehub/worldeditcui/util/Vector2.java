package org.enginehub.worldeditcui.util;

public class Vector2 implements Comparable<Vector2> {

  public final static Vector2 ZERO = new Vector2(0, 0);
  public final static Vector2 UNIT_X = new Vector2(1, 0);
  public final static Vector2 UNIT_Y = new Vector2(0, 1);
  public static Vector2 ONE = new Vector2(1, 1);

  protected double x, z;

  public Vector2(final double x, final double y) {
    this.x = x;
    this.z = y;
  }

  public Vector2(final Double x, final Double y) {
    this(x.doubleValue(), y.doubleValue());
  }

  public Vector2() {
    this(0, 0);
  }

  public Vector2(final Vector2 vector) {
    this(vector.x, vector.z);
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.z;
  }

  public Vector2 add(final Vector2 vector) {
    return Vector2.add(this, vector);
  }

  public Vector2 subtract(final Vector2 vector) {
    return Vector2.subtract(this, vector);
  }

  public Vector2 scale(final double scale) {
    return Vector2.scale(this, scale);
  }

  public double dot(final Vector2 vector) {
    return Vector2.dot(this, vector);
  }

  public Vector3 toVector3() {
    return Vector2.toVector3(this);
  }

  public Vector3 toVector3(final double y) {
    return Vector2.toVector3(this, y);
  }

  public Vector2 cross() {
    return new Vector2(this.z, -this.x);
  }

  public Vector2 ceil() {
    return new Vector2(Math.ceil(this.x), Math.ceil(this.z));
  }

  public Vector2 floor() {
    return new Vector2(Math.floor(this.x), Math.floor(this.z));
  }

  public Vector2 round() {
    return new Vector2(Math.round(this.x), Math.round(this.z));
  }

  public Vector2 abs() {
    return new Vector2(Math.abs(this.x), Math.abs(this.z));
  }

  public double distance(final Vector2 vector) {
    return Vector2.distance(vector, this);
  }

  public Vector2 pow(final double power) {
    return Vector2.pow(this, power);
  }

  public double lengthSquared() {
    return Vector2.lengthSquared(this);
  }

  public double length() {
    return Vector2.length(this);
  }

  public Vector2 normalize() {
    return Vector2.normalize(this);
  }

  public double[] toArray() {
    return Vector2.toArray(this);
  }

  @Override
  public int compareTo(final Vector2 vector) {
    return Vector2.compareTo(this, vector);
  }

  @Override
  public boolean equals(final Object object) {
    return Vector2.equals(this, object);
  }

  public static double length(final Vector2 vector) {
    return Math.sqrt(lengthSquared(vector));
  }

  public static double lengthSquared(final Vector2 vector) {
    return Vector2.dot(vector, vector);
  }

  public static Vector2 normalize(final Vector2 vector) {
    return Vector2.scale(vector, (1.f / vector.length()));
  }

  public static Vector2 subtract(final Vector2 vector1, final Vector2 vector2) {
    return new Vector2(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
  }

  public static Vector2 add(final Vector2 vector1, final Vector2 vector2) {
    return new Vector2(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
  }

  public static Vector2 scale(final Vector2 vector, final double scale) {
    return new Vector2(vector.getX() * scale, vector.getY() * scale);
  }

  public static double dot(final Vector2 vector1, final Vector2 vector2) {
    return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
  }

  public static Vector3 toVector3(final Vector2 vector) {
    return new Vector3(vector.x, 0, vector.z);
  }

  public static Vector3 toVector3(final Vector2 vector, final double y) {
    return new Vector3(vector.x, y, vector.z);
  }

  public static Vector2 ceil(final Vector2 vector) {
    return new Vector2(Math.ceil(vector.x), Math.ceil(vector.z));
  }

  public static Vector2 floor(final Vector2 vector) {
    return new Vector2(Math.floor(vector.x), Math.floor(vector.z));
  }

  public static Vector2 round(final Vector2 vector) {
    return new Vector2(Math.round(vector.x), Math.round(vector.z));
  }

  public static Vector2 abs(final Vector2 vector) {
    return new Vector2(Math.abs(vector.x), Math.abs(vector.z));
  }

  public static Vector2 min(final Vector2 vector1, final Vector2 vector2) {
    return new Vector2(Math.min(vector1.x, vector2.x), Math.min(vector1.z, vector2.z));
  }

  public static Vector2 max(final Vector2 vector1, final Vector2 vector2) {
    return new Vector2(Math.max(vector1.x, vector2.x), Math.max(vector1.z, vector2.z));
  }

  public static Vector2 rand() {
    return new Vector2(Math.random(), Math.random());
  }

  public static double[] toArray(final Vector2 vector) {
    return new double[] { vector.getX(), vector.getY() };
  }

  public static int compareTo(final Vector2 vector1, final Vector2 vector2) {
    return (int) vector1.lengthSquared() - (int) vector2.lengthSquared();
  }

  public static double distance(final Vector2 vector1, final Vector2 vector2) {
    final Vector2 tempVector = Vector2.pow(Vector2.subtract(vector1, vector2), 2);
    return Math.sqrt(tempVector.x + tempVector.z);
  }

  public static Vector2 pow(final Vector2 vector, final double power) {
    return new Vector2(Math.pow(vector.x, power), Math.pow(vector.z, power));
  }

  public static boolean equals(final Object object1, final Object object2) {
    if (!(object1 instanceof Vector2 vector1) || !(object2 instanceof Vector2 vector2)) {
      return false;
    }

    if (object1 == object2) {
      return true;
    }
    return compareTo(vector1, vector2) == 0;
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.z + ")";
  }

  @Override
  public int hashCode() {
    return (int) (this.x % this.z);
  }

}
