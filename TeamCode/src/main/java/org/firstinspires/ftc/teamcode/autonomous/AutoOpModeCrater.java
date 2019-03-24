package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous FacingCrater", group = "ARC Lightning")
public class AutoOpModeCrater extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-craterSide.task");
  }
}
