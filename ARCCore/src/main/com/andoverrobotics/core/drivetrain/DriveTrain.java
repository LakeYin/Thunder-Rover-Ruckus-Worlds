package com.andoverrobotics.core.drivetrain;

public interface DriveTrain {

  void setDefaultDrivePower(double power);
  double getDefaultDrivePower();

  // -- Autonomous Methods --

  // [0, 1]
  void driveForwards(double distanceInInches);
  void driveForwards(double distanceInInches, double power);

  // [0, 1]
  void driveBackwards(double distanceInInches);
  void driveBackwards(double distanceInInches, double power);

  // [0, 360]
  void rotateClockwise(int degrees);
  void rotateCounterClockwise(int degrees);

  // -- Teleop Methods --

  // [-1, 1]
  void setMovementPower(double power);

  // [-1, 1] positive is clockwise
  void setRotationPower(double power);

}
