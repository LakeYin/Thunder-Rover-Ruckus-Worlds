package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.tasks.DepotSideSampleMineralTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.SampleMineralTask;

@Autonomous(name = "Autonomous FacingDepot", group = "ARC Lightning")
public class AutoOpModeDepot extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-depotSide.task");
  }

  @Override
  protected SampleMineralTask getSampleMineralTask() {
    return new DepotSideSampleMineralTask(hardwareMap);
  }
}
