package com.andoverrobotics.core.drivetrain;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Lake Yin on 5/23/2018.
 */

public abstract class StrafingDriveTrain extends DriveTrain {

  public StrafingDriveTrain(OpMode opMode) {
    super(opMode);
  }

  @Override
  public void driveForwards(double distanceInInches, double power) {
    strafeInches(0, Math.abs(distanceInInches), Math.abs(power));
  }

  @Override
  public void driveBackwards(double distanceInInches, double power) {
    strafeInches(0, -Math.abs(distanceInInches), -Math.abs(power));
  }

  // [-1, 1]
  public final void strafeRight(double distanceInInches) {
    strafeRight(distanceInInches, defaultPower);
  }
  public void strafeRight(double distanceInInches, double power) {
    strafeInches(Math.abs(distanceInInches), 0, power);
  }

  // [-1, 1]
  public final void strafeLeft(double distanceInInches) {
    strafeLeft(distanceInInches, defaultPower);
  }
  public void strafeLeft(double distanceInInches, double power) {
    strafeInches(-Math.abs(distanceInInches), 0, power);
  }

  // [-1, 1]
  public final void strafeInches(double xInInches, double yInInches) {
    strafeInches(xInInches, yInInches, defaultPower);
  }
  public final void strafeInches(double xInInches, double yInInches, double power) {
    strafeInches(Coordinate.fromXY(xInInches, yInInches), power);
  }
  public final void strafeInches(Coordinate inchOffset) {
    strafeInches(inchOffset, defaultPower);
  }

  public abstract void strafeInches(Coordinate inchOffset, double power);

  // -- Teleop Methods --

  @Override
  public void setMovementPower(double power) {
    setStrafe(0, 1, power);
  }

  // [0, 360]
  // [-1, 1]
  public final void setStrafe(double x, double y) {
    setStrafe(Coordinate.fromXY(x, y));
  }
  public final void setStrafe(Coordinate direction) {
    setStrafe(direction.getPolarDirection());
  }
  public final void setStrafe(int polarDirection) {
    setStrafe(polarDirection, defaultPower);
  }
  public final void setStrafe(double x, double y, double power) {
    setStrafe(Coordinate.fromXY(x, y), power);
  }
  public final void setStrafe(Coordinate direction, double power) {
    setStrafe(direction.getPolarDirection(), power);
  }
  public abstract void setStrafe(int polarDirection, double power);
}
