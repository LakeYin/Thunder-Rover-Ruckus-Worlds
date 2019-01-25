package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector;
import org.firstinspires.ftc.teamcode.autonomous.VuMarkDetector;
import org.firstinspires.ftc.teamcode.autonomous.VuforiaManager;

@Autonomous(name = "TF <3 VuMark?", group = "ARC")
public class TFVuMarkCoexistenceDemo extends LinearOpMode {

  @Override
  public void runOpMode() {
    VuforiaManager.initVuforia(hardwareMap);
    MineralDetector detector = new MineralDetector(hardwareMap);

    waitForStart();

    detector.activate();

    while (getRuntime() < 10 && opModeIsActive() && !isStopRequested()) {
      telemetry.addData("Current recog", detector.currentRecognition());
      telemetry.update();
    }
    detector.shutdown();

    VuMarkDetector vuMark = new VuMarkDetector();
    vuMark.activate();

    while (getRuntime() < 20 && opModeIsActive() && !isStopRequested()) {
      telemetry.addData("Current recog", vuMark.visibleTarget());
      telemetry.update();
    }
  }
}
