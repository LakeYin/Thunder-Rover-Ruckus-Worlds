package com.andoverrobotics.core.drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Lake Yin on 5/23/2018.
 */

public abstract class StrafingDriveTrain extends DriveTrain {

  public StrafingDriveTrain(OpMode opMode) {
    super(opMode);
  }

  // [-1, 1]
  public final void strafeRight(double distanceInInches) {
    strafeRight(distanceInInches, defaultPower);
  }
  public abstract void strafeRight(double distanceInInches, double power);

  // [-1, 1]
  public final void strafeLeft(double distanceInInches) {
    strafeLeft(distanceInInches, defaultPower);
  }
  public abstract void strafeLeft(double distanceInInches, double power);

  // [-1, 1]
  public final void strafeToCoordinate(double xInInches, double yInInches) {
    strafeToCoordinate(xInInches, yInInches, defaultPower);
  }
  public abstract void strafeToCoordinate(double xInInches, double yInInches, double power);

  // [0, 360]
  // [-1, 1]
  public final void strafeDegrees(int degrees, double distanceInInches) {
    strafeDegrees(degrees, distanceInInches, defaultPower);
  }
  public abstract void strafeDegrees(int degrees, double distanceInInches, double power);

  // -- Teleop Methods --

  // [0, 360]
  // [-1, 1]
  public final void setDegreeOfStrafe(int degrees) {
    setDegreeOfStrafe(degrees, defaultPower);
  }
  public abstract void setDegreeOfStrafe(int degrees, double power);
}
