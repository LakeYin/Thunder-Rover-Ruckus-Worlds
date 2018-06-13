package com.andoverrobotics.core.utilities;

public class Coordinate {

  private double x;
  private double y;

  private Coordinate(double x, double y) {
    this.x = x;
    this.y = y;
  }
  public Coordinate(Coordinate other) {
    this.x = other.x;
    this.y = other.y;
  }

  // Counter-clockwise by default
  public Coordinate rotate(int degrees) {
    return Coordinate.fromPolar(
        getPolarDistance(),
        Converter.normalizedDegrees(getPolarDirection() + degrees));
  }

  public int getPolarDirection() {
    return Converter.radiansToDegrees(Math.atan2(y, x));
  }

  public double getPolarDistance() {
    return Math.hypot(x, y);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public static Coordinate fromXY(double x, double y) {
    return new Coordinate(x, y);
  }

  public static Coordinate fromPolar(double distance, int degrees) {
    double angle = Converter.degreesToRadians(degrees);

    return new Coordinate(distance * Math.cos(angle), distance * Math.sin(angle));
  }
}
