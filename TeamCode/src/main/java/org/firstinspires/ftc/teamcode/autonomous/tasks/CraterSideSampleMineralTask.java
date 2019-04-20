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
      knockCenterMineral();
      return;
    }

    switchRight();

    if (AutonomousBot.centerMineral.orElse(Mineral.SILVER) == Mineral.GOLD) {
      knockCenterMineral();
      switchLeft();
      return;
    }

    switchRight();

    knockCenterMineral();

    switchLeft();
    switchLeft();
  }
}
