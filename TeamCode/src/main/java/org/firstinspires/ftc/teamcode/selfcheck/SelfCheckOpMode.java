package org.firstinspires.ftc.teamcode.selfcheck;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.selfcheck.trials.DrivetrainEncoderTrial;
import org.firstinspires.ftc.teamcode.selfcheck.trials.GamepadTrial;

@Autonomous(name = "Self-Check All", group = "ARC")
public class SelfCheckOpMode extends LinearOpMode {

  private static final Trial[] kTrials = {
      new DrivetrainEncoderTrial(),
      new GamepadTrial()
  };

  @Override
  public void runOpMode() {
    telemetry.setAutoClear(false);

    for (Trial trial : kTrials) {
      trial.addPrerequisites(telemetry);
    }
    telemetry.update();

    waitForStart();

    for (Trial trial : kTrials) {
      try {
        boolean result = trial.runTrial(telemetry, this);

        if (!result) {
          telemetry.addData("SELF-CHECK FAILED", "STOPPING");
          break;
        }
      } catch (Exception e) {
        telemetry.addData("SELF-CHECK CRASHED", e.getMessage());
        e.printStackTrace();
        break;
      }
    }

    telemetry.update();

    while (opModeIsActive() && !isStopRequested());
  }
}
