package org.firstinspires.ftc.teamcode.selfcheck.trials;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.selfcheck.Trial;

public class GamepadTrial implements Trial {
  private LinearOpMode opMode;

  @Override
  public void addPrerequisites(Telemetry telemetry) {
      telemetry.addData("Gamepads 1 and 2", "Leave them at resting positions");
}

  @Override
  public boolean runTrial(Telemetry telemetry, LinearOpMode opMode) {
    this.opMode = opMode;

    boolean gamepad1Passed = checkGamepad(opMode.gamepad1, "Gamepad 1"),
        gamepad2Passed = checkGamepad(opMode.gamepad2, "Gamepad 2");

    return gamepad1Passed && gamepad2Passed;
  }

  private boolean checkGamepad(Gamepad gamepad, String gamepadName) {
    try {
      assertZero(gamepad.left_trigger, "left trigger");
      assertZero(gamepad.right_trigger, "right trigger");
      assertZero(gamepad.left_stick_x, "left stick x");
      assertZero(gamepad.left_stick_y, "left stick y");
      assertZero(gamepad.right_stick_x, "right stick x");
      assertZero(gamepad.right_stick_y, "right stick y");
      assertFalse(gamepad.a, "a button");
      assertFalse(gamepad.b, "b button");
      assertFalse(gamepad.x, "x button");
      assertFalse(gamepad.y, "y button");
      assertFalse(gamepad.dpad_down, "dpad down");
      assertFalse(gamepad.dpad_up, "dpad up");
      assertFalse(gamepad.dpad_left, "dpad left");
      assertFalse(gamepad.dpad_right, "dpad right");
      assertFalse(gamepad.left_bumper, "left bumper");
      assertFalse(gamepad.right_bumper, "right bumper");
      assertFalse(gamepad.start, "start button");
      assertFalse(gamepad.left_stick_button, "left stick button");
      assertFalse(gamepad.right_stick_button, "right stick button");
      opMode.telemetry.addData(gamepadName + " PASS", "All resting values tested");
    } catch (RuntimeException exc) {
      opMode.telemetry.addData(gamepadName + " FAIL", exc.getMessage());
      return false;
    }
    return true;
  }

  private void assertZero(float value, String valueName) {
    assertEqual(0.0f, value, valueName + " not 0");
  }

  private void assertFalse(boolean value, String valueName) {
    assertEqual(false, value, valueName + " not false");
  }

  private void assertEqual(double expected, double value, String message) {
    if (Math.abs(value - expected) > 1e-5)
      throw new RuntimeException(String.format("%.4f != %.4f: %s", value, expected, message));
  }

  private <T> void assertEqual(T expected, T value, String message) {
    if (value != expected)
      throw new RuntimeException(String.format("%s != %s: %s", value, expected, message));
  }
}
