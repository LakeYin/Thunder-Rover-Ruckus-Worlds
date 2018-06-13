package com.andoverrobotics.core.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

public class MotorAdapter implements IMotor {

  private final DcMotor motor;

  public MotorAdapter(DcMotor motor) {
    this.motor = motor;
  }

  public DcMotor getMotor() {
    return motor;
  }

  @Override
  public void setPower(double power) {
    motor.setPower(power);
  }

  @Override
  public void addTargetPosition(int tickOffset) {
    motor.setTargetPosition(motor.getCurrentPosition() + tickOffset);
  }

  @Override
  public void startRunToPosition(int tickOffset, double absPower) {
    if (tickOffset == 0 || absPower < 1e-5)
      return;

    setMode(RunMode.RUN_TO_POSITION);
    addTargetPosition(tickOffset);
    setPower(tickOffset > 0 ? Math.abs(absPower) : -Math.abs(absPower));
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
