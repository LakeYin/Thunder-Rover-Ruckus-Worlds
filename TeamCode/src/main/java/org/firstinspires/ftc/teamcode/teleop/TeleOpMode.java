package org.firstinspires.ftc.teamcode.teleop;

import com.andoverrobotics.core.utilities.Converter;
import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.Arm;

import java.io.IOException;

import static org.firstinspires.ftc.teamcode.teleop.ControlMode.booleanToInt;
import static org.firstinspires.ftc.teamcode.teleop.ControlMode.getMicroAdjustCoord;

@TeleOp(name = "Main TeleOp", group = "ARC Lightning")
public class TeleOpMode extends OpMode {

  private static final double kHookAdjustPower = 0.8;
  private static final double kDeltaLiftPosition = 15;

  private TeleOpBot bot;
  private TeleOpTaskHost hookTaskHost, leftArmTaskHost, rightArmTaskHost;

  @Override
  public void init() {
    try {

      bot = TeleOpBot.fromOpMode(this);
      hookTaskHost = new TeleOpTaskHost();
      leftArmTaskHost = new TeleOpTaskHost();
      rightArmTaskHost = new TeleOpTaskHost();

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
    controlArm(bot.leftArm, -gamepad2.left_stick_y, gamepad2.left_trigger, gamepad2.left_bumper,
        gamepad2.dpad_right, gamepad2.dpad_left, leftArmTaskHost);
    controlArm(bot.rightArm, -gamepad2.right_stick_y, gamepad2.right_trigger,
        gamepad2.right_bumper, gamepad2.b, gamepad2.x, rightArmTaskHost);
    controlHookArm(gamepad2.dpad_up, gamepad2.dpad_down, gamepad2);

    bot.leftArm.setExtenderPower(gamepad1.left_trigger * (gamepad1.left_bumper ? -1 : 1));
    bot.rightArm.setExtenderPower(gamepad1.right_trigger * (gamepad1.right_bumper ? -1 : 1));

    telemetry.addData("Connection Keep-Alive", getRuntime());
    addPowerDrawDebug();
  }

  private void controlArm(Arm arm, float rotationPower, float closeGrabberPower,
      boolean grabberOpen, boolean raiseToScore, boolean lowerToCollect, TeleOpTaskHost taskHost) {

    if (taskHost.isRunning() && Math.abs(rotationPower) > 0.1) {
      taskHost.abort();
      arm.liftMotor.setPower(0);
    } else if (raiseToScore && !taskHost.isRunning()) {
      taskHost.beginAsync(TeleOpTaskHost.getRaiseArm(arm));
    } else if (lowerToCollect && !taskHost.isRunning()) {
      taskHost.beginAsync(TeleOpTaskHost.getLowerArm(arm));
    } else if (!taskHost.isRunning()) {

      int currentPos = arm.liftMotor.getCurrentPosition();
      int targetPos = (int) Range.clip(
          Math.round(arm.liftMotor.getTargetPosition() + rotationPower * kDeltaLiftPosition),
          -1300, 0);

      arm.liftMotor.setMode(RunMode.RUN_TO_POSITION);
      arm.liftMotor.setTargetPosition(targetPos);
      arm.setLiftPower(getDesiredPower(targetPos, currentPos));
    }

    telemetry.addData("one arm targetpos", arm.liftMotor.getTargetPosition());
    telemetry.addData("one arm currentpos", arm.liftMotor.getCurrentPosition());
    telemetry.addData("one arm power", arm.liftMotor.getPower());
    telemetry.addData("one arm angle", Converter.radiansToDegrees(arm.angle()));

    if (grabberOpen) {
      arm.openGrabber();
    } else {
      arm.moveGrabber(closeGrabberPower * -0.12);
    }
  }

  private double getDesiredPower(int targetPos, int currentPos) {
    int diff = Math.abs(targetPos - currentPos);
    return diff > 150 ? 0.35 : (diff / 150.0 * 0.25 + 0.05);
  }

  private void controlDrivetrain(Gamepad gamepad) {
    Coordinate strafe = getLeftDrivetrainTarget(gamepad)
        .add(getMicroAdjustCoord(gamepad).multiply(0.55));
    double microRotatePower = (booleanToInt(gamepad.b) - booleanToInt(gamepad.x)) * 0.22;
    bot.drivetrain.setStrafeAndRotation(strafe, gamepad.right_stick_x + microRotatePower,
        strafe.getPolarDistance());
  }

  private Coordinate getLeftDrivetrainTarget(Gamepad gamepad) {
    return Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);
  }

  private void controlHookArm(boolean up, boolean down, Gamepad gamepad) {
    int liftPower = booleanToInt(down) - booleanToInt(up);
    if ((liftPower != 0 || gamepad.a) && hookTaskHost.isRunning()) {
      hookTaskHost.abort();
    }
    if (!hookTaskHost.isRunning() && bot.secondsRemaining() < 20) {
      bot.hookArm.liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
      bot.hookArm.setLiftPower(liftPower * kHookAdjustPower);
    }

    if (gamepad.y) {
      hookTaskHost.beginAsync(TeleOpTaskHost.raiseHook);
    }
    telemetry.addData("Hook pos", bot.hookArm.liftMotor.getCurrentPosition());
  }

  private void addPowerDrawDebug() {
    telemetry.addData("Hub 2 total current draw", bot.hub2.getTotalModuleCurrentDraw());
    telemetry.addData("Hub 7 total current draw", bot.hub7.getTotalModuleCurrentDraw());
  }
}
