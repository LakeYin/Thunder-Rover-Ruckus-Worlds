package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.io.IOException;
import java.util.Optional;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral;

public class AutonomousBot extends Bot {
  private static LinearOpMode currentOpMode;
  public static Optional<Mineral> centerMineral = Optional.empty(),
          rightMineral = Optional.empty();

  public AutonomousBot(LinearOpMode opMode) throws IOException {
    super(opMode.hardwareMap, opMode.telemetry, opMode.hardwareMap.appContext, opMode);
    currentOpMode = opMode;

   deposit.slideMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
   hookLift.liftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
   intake.slideMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
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
