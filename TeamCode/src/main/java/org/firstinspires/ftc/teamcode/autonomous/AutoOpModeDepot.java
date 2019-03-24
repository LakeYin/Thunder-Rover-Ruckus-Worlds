package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous FacingDepot", group = "ARC Lightning")
public class AutoOpModeDepot extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-depotSide.task");
  }
}
