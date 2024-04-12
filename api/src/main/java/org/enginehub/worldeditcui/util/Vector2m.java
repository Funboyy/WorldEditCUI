package org.enginehub.worldeditcui.util;

public class Vector2m extends Vector2 {

  public Vector2m() {
  }

  public Vector2m(final Double x, final Double y) {
    super(x, y);
  }

  public Vector2m(final float x, final float y) {
    super(x, y);
  }

  public Vector2m(final Vector2 vector) {
    super(vector);
  }

  public void setX(final float x) {
    this.x = x;
  }

  public void setY(final float y) {
    this.z = y;
  }

  @Override
  public Vector2 add(final Vector2 vector) {
    this.x += vector.x;
    this.z += vector.z;
    return this;
  }

  @Override
  public Vector2 subtract(final Vector2 vector) {
    this.x -= vector.x;
    this.z -= vector.z;
    return this;
  }

  @Override
  public Vector2 scale(final double scale) {
    this.x *= scale;
    this.z *= scale;
    return this;
  }

  public Vector2 cross(final Vector2 vector) {
    final double tmp = this.z;

    this.z = -this.x;
    this.x = tmp;
    return this;
  }

  @Override
  public Vector2 ceil() {
    this.x = (float) Math.ceil(this.x);
    this.z = (float) Math.ceil(this.z);
    return this;
  }

  @Override
  public Vector2 floor() {
    this.x = (float) Math.floor(this.x);
    this.z = (float) Math.floor(this.z);
    return this;
  }

  @Override
  public Vector2 round() {
    this.x = Math.round(this.x);
    this.z = Math.round(this.z);
    return this;
  }

  @Override
  public Vector2 abs() {
    this.x = Math.abs(this.x);
    this.z = Math.abs(this.z);
    return this;
  }

  @Override
  public Vector2 normalize() {
    final double length = this.length();
    this.x *= 1 / length;
    this.z *= 1 / length;
    return this;
  }

}
