package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral;

public class CraterSideSampleMineralTask extends SampleMineralTask {

  public CraterSideSampleMineralTask(HardwareMap map) {
    super(map);
  }

  @Override
  protected void runMineralCheck() throws InterruptedException {
    if (detectedGold()) {
      knockMineral();
      return;
    }

    switchRight();

    if (AutonomousBot.initialMineral.orElse(Mineral.SILVER) == Mineral.GOLD) {
      knockMineral();
      switchLeft();
      return;
    }

    switchRight();

    knockMineral();

    switchLeft();
    switchLeft();
  }
}
