package com.andoverrobotics.core.utilities;

// Radians are [0, tau)
// Degrees are [0, 360)

/**
 * Provides utility functions that convert between radians and degrees, millimeters and inches, and
 * normalize angle measures.
 */
public class Converter {

  /**
   * The &tau; constant, or two times &pi;.
   */
  public static final double TAU = 2 * Math.PI;

  /**
   * Converts the given angle measure in degrees into the equivalent measure in radians.
   *
   * @param degrees The degrees of which the radian representation will be returned
   * @return The radian representation of the given angle measure
   */
  public static double degreesToRadians(double degrees) {
    return normalizedRadians(degrees / 180.0 * Math.PI);
  }

  /**
   * Converts the given angle measure in radians into the equivalent measure in degrees.
   *
   * @param radians The radians of which the degree representation will be returned
   * @return The degree representation of the given angle measure
   */
  public static double radiansToDegrees(double radians) {
    return normalizedDegrees(radians / Math.PI * 180);
  }

  /**
   * Converts the given distance (in inches) to millimeters.
   *
   * @param inches The distance, in inches, to be converted into millimeters
   * @return The given distance in millimeters
   */
  public static double inchesToMillimeters(double inches) {
    return inches * 25.4;
  }

  /**
   * Converts the given distance (in millimeters) to inches.
   *
   * @param millimeters The distance, in millimeters, to be converted into inches
   * @return The given distance in inches
   */
  public static double millimetersToInches(double millimeters) {
    return millimeters / 25.4;
  }

  /**
   * Converts the given angle measure in degrees to its equivalent value in the [0, 360] range.
   *
   * @param inputDegrees The angle measure of which the equivalent value in the [0, 360] range will
   * be returned
   * @return The equivalent value of the given angle measure in the [0, 360] range
   */
  public static double normalizedDegrees(final double inputDegrees) {
    double degrees = inputDegrees;

    while (degrees < 0) {
      degrees += 360;
    }
    while (degrees >= 360) {
      degrees -= 360;
    }

    return degrees;
  }

  /**
   * Converts the given angle measure in radians to its equivalent value in the [0, {@link #TAU}]
   * range.
   *
   * @param inputRadians The angle measure of which the equivalent value in the [0, {@link #TAU}]
   * range will be returned
   * @return The equivalent value of the given angle measure in the [0, {@link #TAU}] range
   */
  public static double normalizedRadians(final double inputRadians) {
    double radians = inputRadians;

    while (radians < 0) {
      radians += TAU;
    }
    while (radians >= TAU) {
      radians -= TAU;
    }

    return radians;
  }
}
