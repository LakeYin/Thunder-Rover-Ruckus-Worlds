package org.firstinspires.ftc.teamcode.teleop;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.Bot;

import java.io.IOException;

import static org.firstinspires.ftc.teamcode.teleop.ControlMode.booleanToInt;
import static org.firstinspires.ftc.teamcode.teleop.ControlMode.getMicroAdjustCoord;

@TeleOp(name = "Main TeleOp", group = "ARC Lightning")
public class TeleOpMode extends OpMode {

  private TeleOpBot bot;
  private ControlMapper controlMapper;

  @Override
  public void init() {
    try {

      bot = TeleOpBot.fromOpMode(this);
      controlMapper = new ControlMapper();

    } catch (IOException e) {
      stop();
      e.printStackTrace();
    }
  }

  @Override
  public void loop() {
    // Control modes accessible via this statement
    // controlMapper.applyGamepadInputs(gamepad1, gamepad2);

    controlDriveTrainRightArm(gamepad1);
    controlLeftArm(gamepad2);

    telemetry.addData("Connection Keep-Alive", getRuntime());
  }

  private static final double LIFT_POWER = 0.2;

  private void controlDriveTrainRightArm(Gamepad gamepad) {
    Coordinate leftTarget = Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);

    if (Math.abs(gamepad.right_stick_x) > 0.1) {
      bot.drivetrain.setRotationPower(gamepad.right_stick_x);
    } else {
      bot.drivetrain.setStrafe(leftTarget, leftTarget.getPolarDistance());
    }

    bot.rightArm.setLiftPower((booleanToInt(gamepad.dpad_up) - booleanToInt(gamepad.dpad_down)) * LIFT_POWER);
    controlClawByTriggers(bot.rightArm, gamepad);
  }

  private void controlLeftArm(Gamepad gamepad) {
    bot.drivetrain.setStrafe(getMicroAdjustCoord(gamepad), 0.45);
    controlClawByTriggers(bot.leftArm, gamepad);
    bot.leftArm.setLiftPower(-gamepad.right_stick_y * LIFT_POWER);
    bot.hookArm.setLiftPower(-gamepad.left_stick_y);
    if (gamepad.a)
      controlMapper.cheer();
  }

  private void controlClawByTriggers(Arm arm, Gamepad gamepad) {
    arm.moveGrabber((gamepad.left_trigger - gamepad.right_trigger) * 0.01);
  }
}
