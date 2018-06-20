package com.andoverrobotics.core.utilities;

// Radians are [0, 2pi)
// Degrees are [0, 360)
public class Converter {
  private static final double TAU = 2 * Math.PI;

  public static double degreesToRadians(double degrees) {
    return normalizedRadians(degrees / 180.0 * Math.PI);
  }

  public static double radiansToDegrees(double radians) {
    return normalizedDegrees(radians / Math.PI * 180);
  }

  public static double inchesToMillimeter(double inches) {
    return inches * 25.4;
  }

  public static double millimeterToInches(double millimeters) {
    return millimeters / 25.4;
  }

  public static double normalizedDegrees(final double inputDegrees) {
    double degrees = inputDegrees;

    while (degrees < 0)
      degrees += 360;
    while (degrees >= 360)
      degrees -= 360;

    return degrees;
  }

  public static double normalizedRadians(final double inputRadians) {
    double radians = inputRadians;

    while (radians < 0)
      radians += TAU;
    while (radians >= TAU)
      radians -= TAU;

    return radians;
  }
}
