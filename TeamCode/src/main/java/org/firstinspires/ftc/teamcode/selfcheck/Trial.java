package org.firstinspires.ftc.teamcode.selfcheck;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface Trial {

  // Should addData to telemetry
  void addPrerequisites(Telemetry telemetry);

  // Should write any data or failure explanation to telemetry
  // Returns true if passed
  boolean runTrial(Telemetry telemetry, LinearOpMode opMode);

}
