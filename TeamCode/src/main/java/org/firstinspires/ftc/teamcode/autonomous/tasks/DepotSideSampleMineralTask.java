package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector;

import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.GOLD;
import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.SILVER;

public class DepotSideSampleMineralTask extends SampleMineralTask {

  public DepotSideSampleMineralTask(HardwareMap map) {
    super(map);
  }

  @Override
  protected void runMineralCheck() throws InterruptedException {
    if (AutonomousBot.centerMineral.orElse(SILVER) == GOLD) {
      knockCenterMineral();
      return;
    }

    if (AutonomousBot.rightMineral.orElse(SILVER) == GOLD) {
      switchRight();
      knockSideMineral();
      switchLeft();
    } else {
      switchLeft();
      knockSideMineral();
      switchRight();
    }

      }
}
