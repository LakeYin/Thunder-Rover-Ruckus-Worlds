package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.Optional;
import org.firstinspires.ftc.teamcode.autonomous.VuMarkDetector.Target;

@Autonomous(name = "Main Autonomous", group = "ARC Lightning")
public class AutoOpMode extends LinearOpMode {

  private AutonomousBot bot;
  private VuMarkDetector detector;

  @Override
  public void runOpMode() {
    detector = new VuMarkDetector(hardwareMap);
    detector.activate();

    waitForStart();

    for (Optional<Target> target = Optional.empty();
        opModeIsActive();
        target = detector.visibleTarget()) {
      telemetry.addData("Target Visible", target.map(Enum::name).orElse("none"));
    }
  }
}
