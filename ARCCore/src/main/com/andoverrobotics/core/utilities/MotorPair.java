package com.andoverrobotics.core.utilities;

import android.util.Pair;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

public class MotorPair extends Pair<DcMotor, DcMotor> implements IMotor {

  private MotorPair(DcMotor first, DcMotor second) {
    super(first, second);
  }

  public static MotorPair of(DcMotor one, DcMotor two) {
    return new MotorPair(one, two);
  }

  @Override
  public void setPower(double power) {
    first.setPower(power);
    second.setPower(power);
  }

  @Override
  public void addTargetPosition(int position) {
    first.setTargetPosition(first.getCurrentPosition() + position);
    second.setTargetPosition(second.getCurrentPosition() + position);
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

  @Override
  public void setMode(RunMode mode) {
    first.setMode(mode);
    second.setMode(mode);
  }

  @Override
  public boolean isBusy() {
    return first.isBusy() || second.isBusy();
  }
}
