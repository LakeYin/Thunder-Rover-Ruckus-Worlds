package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.teleop.ControlMode.booleanToInt;
import static org.firstinspires.ftc.teamcode.teleop.ControlMode.getMicroAdjustCoord;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.io.IOException;

@TeleOp(name = "Main TeleOp", group = "ARC")
public class TeleOpMode extends LinearOpMode {

  private TeleOpBot bot;

  @Override
  public void runOpMode() {
    initialize();
    waitForStart();
    while (opModeIsActive()) {

      controlDrivetrain(gamepad1);
      controlCycle(gamepad2);

      telemetry.addData("Connection Keep-Alive", getRuntime());
      addPowerDrawDebug();
      telemetry.update();
    }
  }

  private void initialize() {
    try {
      bot = TeleOpBot.fromOpMode(this);
    } catch (IOException e) {
      stop();
      e.printStackTrace();
    }
  }

  private void controlDrivetrain(Gamepad gamepad) {
    Coordinate strafe = getLeftDrivetrainTarget(gamepad)
        .add(getMicroAdjustCoord(gamepad).multiply(0.55));
    double microRotatePower = (booleanToInt(gamepad.b) - booleanToInt(gamepad.x)) * 0.22;
    bot.drivetrain.setStrafeAndRotation(strafe, gamepad.right_stick_x + microRotatePower,
        strafe.getPolarDistance());
  }

  private void controlCycle(Gamepad gamepad) {
    if (gamepad.x) {
      bot.deposit.retract();
      bot.intake.extendFully();
      bot.intake.orientToCollect();
      bot.intake.runSweeperIn();

    } else if (gamepad.y) {
      bot.intake.stopSweeper();
      bot.intake.orientToTransit();
      bot.intake.retractFully();
      bot.intake.orientToTransfer();

    } else if (gamepad.b) {
      bot.intake.orientToTransit();
      bot.deposit.prepareToDeposit();

    } else if (gamepad.a) {
      bot.deposit.deposit();
    }
  }

  private Coordinate getLeftDrivetrainTarget(Gamepad gamepad) {
    return Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);
  }

  private void addPowerDrawDebug() {
    telemetry.addData("Hub 2 total current draw", bot.hub2.getTotalModuleCurrentDraw());
    telemetry.addData("Hub 7 total current draw", bot.hub7.getTotalModuleCurrentDraw());
  }
}
