package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous FacingCrater", group = "ARC Lightning")
public class AutoOpModeLeft extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-craterRight.task");
  }
}