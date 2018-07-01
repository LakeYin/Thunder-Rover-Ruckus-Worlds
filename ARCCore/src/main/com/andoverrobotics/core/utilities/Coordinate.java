package com.andoverrobotics.core.utilities;

import java.util.Objects;

/**
 * Represents a coordinate in the 2D coordinate plane with support for both Cartesian and polar
 * components.
 */
public class Coordinate {

  private double x;
  private double y;

  private Coordinate(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Creates a Coordinate instance with the components of another Coordinate.
   *
   * @param other The Coordinate instance from which the components are copied
   */
  public Coordinate(Coordinate other) {
    this.x = other.x;
    this.y = other.y;
  }

  // Counter-clockwise by default

  /**
   * Rotates the point at this Coordinate by the given number of degrees in the counter-clockwise
   * direction about the origin.
   *
   * @param degrees The number of degrees to rotate. Positive means counter-clockwise, negative
   * means clockwise.
   * @return The coordinate of the rotated point
   */
  public Coordinate rotate(int degrees) {
    return Coordinate.fromPolar(
        getPolarDistance(),
        Converter.normalizedDegrees(getPolarDirection() + degrees));
  }

  /**
   * Calculates the angle of the polar representation of this coordinate.
   *
   * @return The angle of the polar representation in degrees
   */
  public double getPolarDirection() {
    return Converter.radiansToDegrees(Math.atan2(y, x));
  }

  /**
   * Calculates the distance from the origin to the point at this coordinate.
   *
   * @return The distance from the origin to the point at this coordinate
   */
  public double getPolarDistance() {
    return Math.hypot(x, y);
  }

  /**
   * @return The x value of the Cartesian representation of this coordinate
   */
  public double getX() {
    return x;
  }

  /**
   * @return The y value of the Cartesian representation of this coordinate
   */
  public double getY() {
    return y;
  }

  /**
   * Constructs a new coordinate with the given Cartesian components.
   *
   * @param x The x component of the Cartesian representation of the new coordinate
   * @param y The y component of the Cartesian representation of the new coordinate
   * @return The new coordinate with the given components
   */
  public static Coordinate fromXY(double x, double y) {
    return new Coordinate(x, y);
  }

  /**
   * Constructs a new coordinate with the given polar components.
   *
   * @param distance The distance from the origin to the new coordinate
   * @param degrees The angle of the polar representation of the new coordinate
   * @return The new coordinate with the given polar components
   */
  public static Coordinate fromPolar(double distance, double degrees) {
    double angle = Converter.degreesToRadians(degrees);

    return new Coordinate(distance * Math.cos(angle), distance * Math.sin(angle));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return Double.compare(that.x, x) == 0 &&
        Double.compare(that.y, y) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
