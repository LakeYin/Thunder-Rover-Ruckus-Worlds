package com.andoverrobotics.core.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

/**
 * Adapts a DcMotor to the IMotor interface for the DriveTrains.
 */
public class MotorAdapter implements IMotor {

  private final DcMotor motor;

  /**
   * Creates a new MotorAdapter for the given motor.
   *
   * @param motor The motor to send commands to
   */
  public MotorAdapter(DcMotor motor) {
    this.motor = motor;
  }

  /**
   * @return The motor that this instance adapts
   */
  public DcMotor getMotor() {
    return motor;
  }

  @Override
  public void setPower(double power) {
    motor.setPower(power);
  }

  public double getPower() {
    return motor.getPower();
  }

  @Override
  public void addTargetPosition(int tickOffset) {
    motor.setTargetPosition(motor.getCurrentPosition() + tickOffset);
  }

  @Override
  public void startRunToPosition(int tickOffset, double absPower) {
    if (tickOffset == 0 || absPower < 1e-5) {
      return;
    }

    setMode(RunMode.RUN_TO_POSITION);
    addTargetPosition(tickOffset);
    setPower(tickOffset > 0 ? Math.abs(absPower) : -Math.abs(absPower));
  }

  public int targetPositionError() {
    return motor.getTargetPosition() - motor.getCurrentPosition();
  }

  private int tripStartPosition, tripTargetPosition;

  public void beginTripToTickOffset(int tickOffset) {
    tripStartPosition = motor.getCurrentPosition();
    tripTargetPosition = tripStartPosition + tickOffset;
    setMode(RunMode.RUN_USING_ENCODER);
  }

  public double ratioDone() {
    return (motor.getCurrentPosition() - tripStartPosition) /
            (double) (tripTargetPosition - tripStartPosition);
  }

  @Override
  public void setMode(RunMode mode) {
    motor.setMode(mode);
  }

  @Override
  public boolean isBusy() {
    return motor.isBusy();
  }
}
