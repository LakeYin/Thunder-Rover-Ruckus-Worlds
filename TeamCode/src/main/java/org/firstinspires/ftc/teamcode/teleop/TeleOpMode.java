package org.firstinspires.ftc.teamcode.teleop;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.Arm;

import java.io.IOException;

import static org.firstinspires.ftc.teamcode.teleop.ControlMode.booleanToInt;
import static org.firstinspires.ftc.teamcode.teleop.ControlMode.getMicroAdjustCoord;

@TeleOp(name = "Main TeleOp", group = "ARC Lightning")
public class TeleOpMode extends OpMode {

  private TeleOpBot bot;
  private ControlMapper controlMapper;
  private TeleOpTaskHost taskHost;

  @Override
  public void init() {
    try {

      bot = TeleOpBot.fromOpMode(this);
      controlMapper = new ControlMapper();
      taskHost = new TeleOpTaskHost();

    } catch (IOException e) {
      stop();
      e.printStackTrace();
    }
  }

  @Override
  public void loop() {
    // Control modes accessible via this statement
    // controlMapper.applyGamepadInputs(gamepad1, gamepad2);

    controlRightArm(gamepad1);
    controlLeftArm(gamepad2);

    Coordinate strafe = getLeftDrivetrainTarget(gamepad1).add(getMicroAdjustCoord(gamepad2).multiply(0.45));
    bot.drivetrain.setStrafeAndRotation(strafe, gamepad1.right_stick_x * 0.8,
        strafe.getPolarDistance());


    telemetry.addData("Connection Keep-Alive", getRuntime());
  }

  private static final double LIFT_POWER = 0.2;

  private void controlRightArm(Gamepad gamepad) {
    bot.rightArm.setLiftPower((booleanToInt(gamepad.y) - booleanToInt(gamepad.a)) * LIFT_POWER);

    controlClawByTriggers(bot.rightArm, gamepad);
  }

  private Coordinate getLeftDrivetrainTarget(Gamepad gamepad) {
    return Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);
  }

  private void controlLeftArm(Gamepad gamepad) {
    controlClawByTriggers(bot.leftArm, gamepad);
    bot.leftArm.setLiftPower(-gamepad.right_stick_y * LIFT_POWER);

    if (gamepad.a)
      controlMapper.cheer();

    if (!taskHost.isRunning())
      bot.hookArm.setLiftPower(-gamepad.left_stick_y);

    if (gamepad.y)
      taskHost.beginAsync(TeleOpTaskHost.raiseHook);
    else if (gamepad.x) {
      taskHost.abort();
      bot.hookArm.setLiftPower(0);
    }
  }

  private void controlClawByTriggers(Arm arm, Gamepad gamepad) {
    arm.moveGrabber((gamepad.right_trigger - gamepad.left_trigger) * 0.01);
  }
}
