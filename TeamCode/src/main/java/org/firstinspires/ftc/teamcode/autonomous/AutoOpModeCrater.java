package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.tasks.CraterSideSampleMineralTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.SampleMineralTask;

@Autonomous(name = "Autonomous FacingCrater", group = "ARC Lightning")
public class AutoOpModeCrater extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-craterSide.task");
  }

  @Override
  protected SampleMineralTask getSampleMineralTask() {
    return new CraterSideSampleMineralTask(hardwareMap);
  }
}
