package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.Optional;
import org.firstinspires.ftc.teamcode.autonomous.VuMarkDetector;
import org.firstinspires.ftc.teamcode.autonomous.VuMarkDetector.Target;
import org.firstinspires.ftc.teamcode.autonomous.VuforiaManager;

@Autonomous(name = "VuMark Detection Demo", group = "ARC")
public class VuMarkDetectorDemo extends LinearOpMode {

  private VuMarkDetector detector;

  @Override
  public void runOpMode() {
    VuforiaManager.initVuforia(hardwareMap);
    detector = new VuMarkDetector();
    detector.activate();

    waitForStart();

    for (Optional<Target> target = Optional.empty();
        opModeIsActive();
        target = detector.visibleTarget()) {
      telemetry.addData("Target Visible", target.map(Enum::name).orElse("none"));
      telemetry.update();
    }
  }
}
