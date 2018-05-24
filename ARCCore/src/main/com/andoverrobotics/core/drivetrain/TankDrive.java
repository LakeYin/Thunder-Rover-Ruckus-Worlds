package com.andoverrobotics.core.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDrive implements DriveTrain {

  private DcMotor motorL;
  private DcMotor motorR;

  private double defaultPower;

  public TankDrive(DcMotor motorL, DcMotor motorR) {
    this.motorL = motorL;
    this.motorR = motorR;
  }

  @Override
  public void setDefaultDrivePower(double power) {
    defaultPower = power;
  }
  @Override
  public double getDefaultDrivePower() {
    return defaultPower;
  }

  @Override
  public void driveForwards(double distanceInInches) {
    driveForwards(distanceInInches, defaultPower);
  }
  @Override
  public void driveForwards(double distanceInInches, double power) {
    // FIXME implement
  }

  @Override
  public void driveBackwards(double distanceInInches) {
    driveBackwards(distanceInInches, defaultPower);
  }
  @Override
  public void driveBackwards(double distanceInInches, double power) {
    // FIXME implement
  }

  @Override
  public void rotateClockwise(int degrees) {

  }

  @Override
  public void rotateCounterClockwise(int degrees) {

  }

  @Override
  public void setMovementPower(double power) {
    motorL.setPower(power);
    motorR.setPower(power);
  }

  @Override
  public void setRotationPower(double power) {
    motorL.setPower(power);
    motorR.setPower(-power);
  }
}
