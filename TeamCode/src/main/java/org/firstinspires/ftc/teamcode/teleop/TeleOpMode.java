package org.firstinspires.ftc.teamcode.teleop;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.Arm;

import java.io.IOException;

import static org.firstinspires.ftc.teamcode.teleop.ControlMode.booleanToInt;
import static org.firstinspires.ftc.teamcode.teleop.ControlMode.getMicroAdjustCoord;

@TeleOp(name = "Main TeleOp", group = "ARC Lightning")
public class TeleOpMode extends OpMode {

  private static final double HOOK_ADJUST_POWER = 1;
  private static final double LIFT_POWER = 0.25;

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

    controlDrivetrain(gamepad1);
    controlArm(bot.leftArm, -gamepad2.left_stick_y, gamepad2.left_trigger, gamepad2.left_bumper);
    controlArm(bot.rightArm, -gamepad2.right_stick_y, gamepad2.right_trigger,
        gamepad2.right_bumper);
    controlHookArm(gamepad2);

    telemetry.addData("Connection Keep-Alive", getRuntime());
  }

  private void controlArm(Arm arm, float rotationPower, float closeGrabberPower,
      boolean grabberOpen) {
    arm.setLiftPower(rotationPower * LIFT_POWER);
    if (grabberOpen) {
      arm.openGrabber();
    } else {
      arm.moveGrabber(closeGrabberPower * -0.1);
    }
  }

  private void controlDrivetrain(Gamepad gamepad) {
    Coordinate strafe = getLeftDrivetrainTarget(gamepad)
        .add(getMicroAdjustCoord(gamepad).multiply(0.45));
    bot.drivetrain.setStrafeAndRotation(strafe, gamepad.right_stick_x * 0.8,
        strafe.getPolarDistance());
  }

  private Coordinate getLeftDrivetrainTarget(Gamepad gamepad) {
    return Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);
  }

  private void controlHookArm(Gamepad gamepad) {
    if (gamepad.a) {
      controlMapper.cheer();
    }

    int liftPower = booleanToInt(gamepad.dpad_down) - booleanToInt(gamepad.dpad_up);
    if (liftPower != 0 && taskHost.isRunning()) {
      taskHost.abort();
    }
    if (!taskHost.isRunning()) {
      bot.hookArm.liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
      bot.hookArm.setLiftPower(liftPower * HOOK_ADJUST_POWER);
    }

    if (gamepad.y) {
      taskHost.beginAsync(TeleOpTaskHost.raiseHook);
    }
  }
}
