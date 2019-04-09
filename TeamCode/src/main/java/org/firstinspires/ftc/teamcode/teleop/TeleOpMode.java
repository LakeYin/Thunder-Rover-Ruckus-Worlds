package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.CYCLE_COLLECT;
import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.CYCLE_DELIVER;
import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.CYCLE_SCORE;
import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.CYCLE_TRANSFER;
import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.ENDGAME;
import static org.firstinspires.ftc.teamcode.teleop.TeleOpState.MANUAL;

import com.andoverrobotics.core.utilities.Coordinate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.io.IOException;

@TeleOp(name = "Main TeleOp", group = "ARC")
public class TeleOpMode extends LinearOpMode {

  private TeleOpBot bot;
  private TeleOpState state = MANUAL;

  @Override
  public void runOpMode() {
    initialize();
    waitForStart();
    while (opModeIsActive()) {

      controlDrivetrain(gamepad1);
      controlCycle(gamepad2);
      adjustByState(gamepad2);

      telemetry.addData("Connection Keep-Alive", getRuntime());
      telemetry.addData("State", state);
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
        .add(getMicroAdjustCoord(gamepad).multiply(0.7));
    double microRotatePower = (booleanToInt(gamepad.b) - booleanToInt(gamepad.x)) * 0.35;
    bot.drivetrain.setStrafeAndRotation(strafe, gamepad.right_stick_x + microRotatePower,
        strafe.getPolarDistance());
  }

  private void controlCycle(Gamepad gamepad) {
    if (gamepad.x && (state == CYCLE_SCORE || state == MANUAL)) {
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

    } else if (state == MANUAL) {
      controlManually();
    } else if (state == ENDGAME) {
      controlEndgame();
    }

    if (gamepad.left_stick_button) {
      state = MANUAL;
    } else if (gamepad.right_stick_button) {
      state = ENDGAME;
    }
  }

  private void adjustByState(Gamepad cycleGamepad) {
    switch (state) {
      case CYCLE_COLLECT:
        bot.intake.controlSlidesManually(-cycleGamepad.left_stick_x);
        bot.intake.orientManually(cycleGamepad.left_stick_y);
        break;
      case CYCLE_TRANSFER:
        bot.intake.controlSlidesManually(-cycleGamepad.left_stick_x * 0.4);
        break;
      case CYCLE_DELIVER:
        // Empty for no adjustment mappings applicable in DELIVER state
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

    else if (Math.abs(gamepad2.left_stick_y) > 0.01) {
      bot.hookLift.adjust(-gamepad2.left_stick_y);
    }
  }

  private void controlManually() {
    bot.intake.controlSlidesManually(-gamepad2.left_stick_x);
    bot.intake.orientManually(-gamepad2.left_stick_y);

    if (gamepad2.left_bumper)
      bot.intake.runSweeperIn();
    else if (gamepad2.right_bumper)
      bot.intake.stopSweeper();

    if (gamepad2.dpad_down) {
      bot.deposit.retract();
    } else if (gamepad2.dpad_up) {
      bot.deposit.deliverToLander().begin();
    }
    if (gamepad2.dpad_left || gamepad2.dpad_right) {
      bot.deposit.score();
    }

    bot.hookLift.adjust(-gamepad2.right_stick_y * 0.5);
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
