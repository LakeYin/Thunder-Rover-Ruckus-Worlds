package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.tasks.DepotSideSampleMineralTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.SampleMineralTask;

@Autonomous(name = "Autonomous Depot Far Crater", group = "ARC Lightning")
public class AutoOpModeDepotOpposingCrater extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-depot-opposingCrater.task");
  }

  @Override
  protected SampleMineralTask getSampleMineralTask() {
    return new DepotSideSampleMineralTask(hardwareMap);
  }
}
