package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.autonomous.tasks.SampleMineralTask;

@Autonomous(name = "Autonomous Crater Side", group = "ARC Lightning")
public class AutoOpModeCrater extends AutoOpMode {
  @Override
  public void runOpMode() {
    runOpMode("auto-craterSide.task");
  }
}
