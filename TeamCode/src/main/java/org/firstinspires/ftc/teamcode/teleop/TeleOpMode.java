package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.ENDGAME;
import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.MANUAL;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.io.IOException;
import java.util.Arrays;

@TeleOp(name = "Main TeleOp", group = "ARC")
public class TeleOpMode extends LinearOpMode {

  private TeleOpBot bot;
  private TeleOpState state = MANUAL;

  @Override
  public void runOpMode() {
    initialize();
    telemetry.addData("Initialization", "Complete");
    telemetry.update();
    waitForStart();
    long prevTime = System.currentTimeMillis();
    while (opModeIsActive()) {

      controlDrivetrain(gamepad1);
      controlCycle(gamepad2);
      adjustByState(gamepad2);
      if (gamepad2.back || gamepad1.right_bumper) {
        stopAllMotion();
      }

      telemetry.addData("Connection Keep-Alive", getRuntime());
      telemetry.addData("State", state);
      addPowerDrawDebug();

      long time = System.currentTimeMillis();
      telemetry.addData("Cycle Rate", "%dms/cycle", time - prevTime);
      bot.addAllDiagnosableData();
      prevTime = time;

      telemetry.addData("memoPos", Arrays.toString(bot.drivetrain.getMemorizedPosition()));
      telemetry.update();
    }
  }

  private void stopAllMotion() {
    bot.intake.controlSlidesManually(0);
    bot.deposit.adjust(0);
    bot.hookLift.adjust(0);
    bot.intake.stopSweeper();
    bot.drivetrain.stop();
    bot.hub2.setLedColor(255, 0, 0);
    bot.hub7.setLedColor(255, 0, 0);
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
    if (!bot.drivetrain.isRunningToPosition()) {
      Coordinate strafe = getLeftDrivetrainTarget(gamepad)
              .add(getMicroAdjustCoord(gamepad).multiply(0.7));
      double microRotatePower = (booleanToInt(gamepad.b) - booleanToInt(gamepad.x)) * 0.35;
      bot.drivetrain.setStrafeAndRotation(strafe, gamepad.right_stick_x + microRotatePower,
              strafe.getPolarDistance());

      if (gamepad.start) {
        bot.drivetrain.memorizeCurrentPosition();
      } else if (gamepad.left_bumper) {
        bot.drivetrain.startGoingToMemorizedPosition(0.6);
      }
    }
  }

  private void controlCycle(Gamepad gamepad) {
    // TODO enable
    /*if (gamepad.x && (state == CYCLE_SCORE || state == MANUAL)) {
      state = CYCLE_COLLECT;
      bot.deposit.retract();
      bot.intake.extend(0.5).begin().whenDone(() -> {
        bot.intake.orientToCollect();
        bot.intake.runSweeperIn();
      });

    } else if (gamepad.a && state == CYCLE_COLLECT) {
      state = CYCLE_TRANSFER;
      bot.intake.stopSweeper();
      bot.intake.orientToTransit();
      bot.intake.retractFully().begin().whenDone(bot.intake::orientToTransfer);

    } else if (gamepad.b && (state == CYCLE_TRANSFER || state == MANUAL)) {
      state = CYCLE_DELIVER;
      bot.intake.orientToTransit();
      bot.deposit.prepareToDeposit();
      bot.deposit.deliverToLander().begin();

    } else if (gamepad.y && state == CYCLE_DELIVER) {
      state = CYCLE_SCORE;
      bot.deposit.score();

    } */

    if (gamepad.left_stick_button) {
      state = MANUAL;
    } else if (gamepad.right_stick_button) {
      state = ENDGAME;
    }
  }

  private void adjustByState(Gamepad cycleGamepad) {
    switch (state) {
      case CYCLE_COLLECT:
        if (!bot.intake.isSlideRunningToPosition()) {
          bot.intake.controlSlidesManually(-cycleGamepad.left_stick_x);
        }
        bot.intake.orientManually(cycleGamepad.left_stick_y);
        break;
      case CYCLE_TRANSFER:
        if (!bot.intake.isSlideRunningToPosition()) {
          bot.intake.controlSlidesManually(-cycleGamepad.left_stick_x * 0.4);
        }
        break;
      case CYCLE_DELIVER:
        if (!bot.deposit.isRunningToPosition()) {
          bot.deposit.adjust(cycleGamepad.right_stick_x);
        }
        break;
      case CYCLE_SCORE:
        bot.deposit.orientator.setPosition(bot.deposit.orientator.getPosition() + cycleGamepad.right_stick_x * 0.07);
        break;
      case MANUAL:
        controlManually();
        break;
      case ENDGAME:
        controlEndgame();
        break;
    }
  }


  private void controlEndgame() {
    if (gamepad2.dpad_up) {
      bot.hookLift.liftToHook().begin();
    }
    if (gamepad2.dpad_down) {
      bot.hookLift.lowerToBottom().begin();
    }

    else if (bot.hookLift.liftMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION ||
            !bot.hookLift.liftMotor.isBusy()) {
      bot.hookLift.adjust(-gamepad2.left_stick_y);
    }
  }

  private void controlManually() {
    bot.intake.controlSlidesManually(-gamepad2.left_stick_x);
    bot.intake.orientManually(gamepad2.left_stick_y);

    if (gamepad2.left_bumper)
      bot.intake.runSweeperIn();
    else if (gamepad2.right_bumper)
      bot.intake.runSweeperOut();
    else
      bot.intake.stopSweeper();

    if (gamepad2.y) {
      bot.deposit.orientToTransit();
    }
    if (gamepad2.b) {
      bot.deposit.orientToLevel();
    }
    if (gamepad2.a) {
      bot.deposit.orientToScore();
    }
    if (gamepad2.dpad_up) {
      bot.intake.orientToTransfer();
    }
    if (gamepad2.dpad_right) {
      bot.intake.orientToTransit();
    }
    if (gamepad2.dpad_down) {
      bot.intake.orientToCollect();
    }
    if (gamepad2.x) {
      bot.deposit.retract();
    }

    bot.hookLift.adjust(-gamepad2.right_stick_y * 0.5);

    if (!bot.deposit.isRunningToPosition()) {
      float depositControl = gamepad2.right_stick_x;
      bot.deposit.adjust(depositControl > 0 ? depositControl * 0.8 : depositControl * 0.2);
    }

    bot.deposit.orientator.setPosition(bot.deposit.orientator.getPosition() + (gamepad2.left_trigger - gamepad2.right_trigger) * 0.1);
  }

  private Coordinate getLeftDrivetrainTarget(Gamepad gamepad) {
    return Coordinate.fromXY(gamepad.left_stick_x, -gamepad.left_stick_y);
  }

  private void addPowerDrawDebug() {
    telemetry.addData("Hub 2 total current draw", bot.hub2.getTotalModuleCurrentDraw());
    telemetry.addData("Hub 7 total current draw", bot.hub7.getTotalModuleCurrentDraw());
  }

  private static Coordinate getMicroAdjustCoord(Gamepad gamepad) {
    double x = booleanToInt(gamepad.dpad_right) - booleanToInt(gamepad.dpad_left);
    double y = booleanToInt(gamepad.dpad_up) - booleanToInt(gamepad.dpad_down);
    return Coordinate.fromXY(x * 1.4, y);
  }

  private static int booleanToInt(boolean bool) {
    return bool ? 1 : 0;
  }
}
