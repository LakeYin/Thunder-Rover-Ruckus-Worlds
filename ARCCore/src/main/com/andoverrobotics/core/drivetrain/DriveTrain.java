package com.andoverrobotics.core.drivetrain;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Lake Yin on 5/23/2018.
 */

public abstract class DriveTrain {
  protected final OpMode opMode;
  protected double defaultPower;

  public DriveTrain(OpMode opMode) {
    this.opMode = opMode;
  }

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

  // [-1, 1] positive is clockwise
  public abstract void setRotationPower(double power);

  protected boolean opModeIsActive() {
    boolean isAutonomous = opMode instanceof LinearOpMode;

    return !isAutonomous || (isAutonomous &&
            ((LinearOpMode) opMode).opModeIsActive());
  }
}
