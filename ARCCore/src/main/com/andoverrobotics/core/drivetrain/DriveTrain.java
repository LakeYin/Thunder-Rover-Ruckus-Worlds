package com.andoverrobotics.core.drivetrain;


import com.andoverrobotics.core.utilities.IMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

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

  public abstract void setMovementAndRotation(double movePower, double rotatePower);

  protected boolean opModeIsActive() {
    boolean isAutonomous = opMode instanceof LinearOpMode;

    return !isAutonomous || ((LinearOpMode) opMode).opModeIsActive();
  }

  public void stop() {
    for (IMotor motor : getMotors()) {
      motor.setPower(0);
    }
  }

  // -- Internal methods

  protected abstract IMotor[] getMotors();

  protected void setMotorMode(RunMode mode) {
    for (IMotor motor : getMotors()) {
      motor.setMode(mode);
    }
  }

  protected boolean isBusy() {
    for (IMotor motor : getMotors()) {
      if (motor.isBusy())
        return true;
    }
    return false;
  }
}
