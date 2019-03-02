package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.firstinspires.ftc.teamcode.Bot;

import java.io.IOException;

public class AutonomousBot extends Bot {
  private static LinearOpMode currentOpMode;

  public AutonomousBot(LinearOpMode opMode) throws IOException {
    super(opMode.hardwareMap, opMode.telemetry, opMode.hardwareMap.appContext, opMode);
    currentOpMode = opMode;

    leftArm.liftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    rightArm.liftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    while (leftArm.liftMotor.getCurrentPosition() != 0 || rightArm.liftMotor.getCurrentPosition() != 0 && isActive());
  }

  public static boolean isActive() {
    if (currentOpMode == null)
      return false;
    return currentOpMode.opModeIsActive();
  }

  public static void sleep(long ms) {
    if (currentOpMode == null) {
      try {
        Thread.sleep(ms);
      } catch (InterruptedException interrupted) {
        Thread.currentThread().interrupt();
      }
    } else {
      currentOpMode.sleep(ms);
    }
  }

  public static double secondsRemaining() {
    if (currentOpMode == null) {
      return 0;
    }
    return 30 - currentOpMode.getRuntime();
  }
}
