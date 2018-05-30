package com.andoverrobotics.core.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDrive extends DriveTrain {

  private DcMotor motorL;
  private DcMotor motorR;

  private double defaultPower;

  public TankDrive(DcMotor motorL, DcMotor motorR) {
    this.motorL = motorL;
    this.motorR = motorR;
  }

  @Override
  public void driveForwards(double distanceInInches, double power) {
    // FIXME implement
  }

  @Override
  public void driveBackwards(double distanceInInches, double power) {
    // FIXME implement
  }

  @Override
  public void rotateClockwise(int degrees, double power) {

  }

  @Override
  public void rotateCounterClockwise(int degrees, double power) {

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
