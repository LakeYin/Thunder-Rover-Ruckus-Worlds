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
  private TeleOpTaskHost taskHost;

  private int armCollectionPosition, armScorePosition;

  @Override
  public void init() {
    try {

      bot = TeleOpBot.fromOpMode(this);
      taskHost = new TeleOpTaskHost();
      loadArmPositions();

    } catch (IOException e) {
      stop();
      e.printStackTrace();
    }
  }

  private void loadArmPositions() {
    armCollectionPosition = bot.mainConfig.getInt("armDownPosition");
    armScorePosition = bot.mainConfig.getInt("armUpPosition");
  }

  @Override
  public void loop() {
    // Control modes accessible via this statement
    // controlMapper.applyGamepadInputs(gamepad1, gamepad2);

    controlDrivetrain(gamepad1);
    controlArm(bot.leftArm, -gamepad2.left_stick_y, gamepad2.left_trigger, gamepad2.left_bumper,
        gamepad2.dpad_right, gamepad2.dpad_left);
    controlArm(bot.rightArm, -gamepad2.right_stick_y, gamepad2.right_trigger,
        gamepad2.right_bumper, gamepad2.b, gamepad2.x);
    controlHookArm(gamepad2);

    bot.leftArm.setExtenderPower(gamepad1.left_trigger * (gamepad1.left_bumper ? -1 : 1));
    bot.rightArm.setExtenderPower(gamepad1.right_trigger * (gamepad1.right_bumper ? -1 : 1));

    telemetry.addData("Connection Keep-Alive", getRuntime());
    addPowerDrawDebug();
  }

  private double armAngle(int position) {
    return (position - 185.0) * Math.PI / 694.0;
  }

  private void controlArm(Arm arm, float rotationPower, float closeGrabberPower,
      boolean grabberOpen, boolean raiseToScore, boolean lowerToCollect) {

    if (rotationPower > 0.1) {
      taskHost.abort();
    }
    if (raiseToScore) {
      taskHost.beginAsync(TeleOpTaskHost.getRaiseArm(arm));
    } else if (lowerToCollect) {
      taskHost.beginAsync(TeleOpTaskHost.getLowerArm(arm));
    }

    int currentPos = arm.liftMotor.getCurrentPosition();
    int targetPos =
        raiseToScore ? armScorePosition : (int) (lowerToCollect ? armCollectionPosition
            : Range.clip(Math.round(arm.liftMotor.getTargetPosition() + rotationPower * kDeltaLiftPosition), -1300, 0));

    arm.liftMotor.setMode(RunMode.RUN_TO_POSITION);
    arm.liftMotor.setTargetPosition(targetPos);
    arm.setLiftPower(getDesiredPower(targetPos, currentPos));
    telemetry.addData("one arm targetpos", targetPos);
    telemetry.addData("one arm currentpos", currentPos);
    telemetry.addData("one arm power", arm.liftMotor.getPower());
    telemetry.addData("one arm angle", Converter.radiansToDegrees(armAngle(currentPos)));

    if (grabberOpen) {
      arm.openGrabber();
    } else {
      arm.moveGrabber(closeGrabberPower * -0.1);
    }
  }

  private double getDesiredPower(int targetPos, int currentPos) {
    int diff = Math.abs(targetPos - currentPos);
    return diff > 150 ? 0.35 : (diff / 150.0 * 0.25 + 0.1);
  }

  private void controlDrivetrain(Gamepad gamepad) {
    Coordinate strafe = getLeftDrivetrainTarget(gamepad)
        .add(getMicroAdjustCoord(gamepad).multiply(0.45));
    double microRotatePower = (booleanToInt(gamepad.b) - booleanToInt(gamepad.x)) * 0.22;
    bot.drivetrain.setStrafeAndRotation(strafe, gamepad.right_stick_x + microRotatePower,
        strafe.getPolarDistance());
  }

  private Coordinate getLeftDrivetrainTarget(Gamepad gamepad) {
    return Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);
  }

  private void controlHookArm(Gamepad gamepad) {
    int liftPower = booleanToInt(gamepad.dpad_down) - booleanToInt(gamepad.dpad_up);
    if ((liftPower != 0 || gamepad.a) && taskHost.isRunning()) {
      taskHost.abort();
    }
    if (!taskHost.isRunning()) {
      bot.hookArm.liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
      bot.hookArm.setLiftPower(liftPower * kHookAdjustPower);
    }

    if (gamepad.y) {
      taskHost.beginAsync(TeleOpTaskHost.raiseHook);
    }
  }

  private void addPowerDrawDebug() {
    telemetry.addData("Hub 2 total current draw", bot.hub2.getTotalModuleCurrentDraw());
    telemetry.addData("Hub 7 total current draw", bot.hub7.getTotalModuleCurrentDraw());
  }
}
