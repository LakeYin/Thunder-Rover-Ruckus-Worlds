package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Bot;

import java.io.IOException;

public class AutonomousBot extends Bot {
  private static LinearOpMode currentOpMode;

  public AutonomousBot(LinearOpMode opMode) throws IOException {
    super(opMode.hardwareMap, opMode.telemetry, opMode.hardwareMap.appContext, opMode);
    currentOpMode = opMode;
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
}
