package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.tasks.SampleMineralTask;

@Autonomous(name = "Autonomous Depot Side to Far Crater", group = "ARC Lightning")
public class AutoOpModeDepotFar extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-depot-opposingCrater.task");
  }
}
