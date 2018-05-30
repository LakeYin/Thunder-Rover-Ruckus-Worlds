package com.andoverrobotics.core.drivetrain;

/**
 * Created by Lake Yin on 5/23/2018.
 */

public abstract class DriveTrain {
  protected double defaultPower;

  public final void setDefaultDrivePower(double power) {
    defaultPower = power;
  }
  public final double getDefaultDrivePower() {
    return defaultPower;
  }

  // -- Autonomous Methods --

  // [0, 1]
  public final void driveForwards(double distanceInInches) {
    driveForwards(distanceInInches, defaultPower);
  }
  public abstract void driveForwards(double distanceInInches, double power);

  // [0, 1]
  public final void driveBackwards(double distanceInInches) {
    driveBackwards(distanceInInches, defaultPower);
  }
  public abstract void driveBackwards(double distanceInInches, double power);

  // [0, 360]
  public final void rotateClockwise(int degrees) {
    rotateClockwise(degrees, defaultPower);
  }
  public abstract void rotateClockwise(int degrees, double power);

  public final void rotateCounterClockwise(int degrees) {
    rotateCounterClockwise(degrees, defaultPower);
  }
  public abstract void rotateCounterClockwise(int degrees, double power);

  // -- Teleop Methods --

  // [-1, 1]
  public abstract void setMovementPower(double power);

  // [-1, 1]
  public abstract void setRotationPower(double power);

}
