package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ControlMapper {
  private ControlMode
      modeForGamepad1 = ControlMode.DRIVE,
      modeForGamepad2 = ControlMode.SLIDE_1;

  public void applyGamepadInputs(Gamepad gamepad1, Gamepad gamepad2) {
    modeForGamepad1.apply(gamepad1);
    modeForGamepad2.apply(gamepad2);

    applyModes(gamepad1, gamepad2);
  }

  private void applyModes(Gamepad gamepad1, Gamepad gamepad2) {
    for (ControlMode mode : ControlMode.values()) {
      if (mode.shouldBeActivated(gamepad1))
        modeForGamepad1 = mode;
      else if (mode.shouldBeActivated(gamepad2))
        modeForGamepad2 = mode;
    }
  }

  public ControlMode getModeForGamepad1() {
    return modeForGamepad1;
  }

  public ControlMode getModeForGamepad2() {
    return modeForGamepad2;
  }

  public void setModeForGamepad1(ControlMode modeForGamepad1) {
    this.modeForGamepad1 = modeForGamepad1;
  }

  public void setModeForGamepad2(ControlMode modeForGamepad2) {
    this.modeForGamepad2 = modeForGamepad2;
  }
}
