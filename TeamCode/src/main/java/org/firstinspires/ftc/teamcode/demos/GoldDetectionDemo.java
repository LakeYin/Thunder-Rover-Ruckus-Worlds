package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.Optional;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector.GoldPosition;
import org.firstinspires.ftc.teamcode.autonomous.VuforiaManager;

@Autonomous(name = "Gold Detection Demo", group = "ARC")
public class GoldDetectionDemo extends LinearOpMode {

  private MineralDetector detector;

  @Override
  public void runOpMode() {
    VuforiaManager.initVuforia(hardwareMap);
    detector = new MineralDetector(hardwareMap);
    detector.activate();


    waitForStart();

    for (Optional<GoldPosition> target = Optional.empty();
        opModeIsActive();
        target = detector.goldPosition()) {
      telemetry.addData("Gold Position", target.map(Enum::name).orElse("none"));
      telemetry.addData("currentRecognition()", detector.currentRecognition()
          .map(Enum::name).orElse("none"));
      telemetry.update();
    }

    detector.shutdown();
  }
}
