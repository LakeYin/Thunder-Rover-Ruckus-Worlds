package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.tasks.SampleMineralTask;

@Autonomous(name = "Autonomous Depot Side to Near Crater", group = "ARC Lightning")
public class AutoOpModeDepotNear extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-depotSide.task");
  }
}
