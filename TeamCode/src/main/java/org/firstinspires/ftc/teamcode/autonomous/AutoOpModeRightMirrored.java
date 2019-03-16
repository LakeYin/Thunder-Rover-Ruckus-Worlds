package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous FacingDepotMirrorPath", group = "ARC Lightning")
public class AutoOpModeRightMirrored extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-craterLeftMirrored.task");
  }
}
