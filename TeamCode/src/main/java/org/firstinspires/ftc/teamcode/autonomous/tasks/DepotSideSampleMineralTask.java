package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class DepotSideSampleMineralTask extends SampleMineralTask {

  public DepotSideSampleMineralTask(HardwareMap map) {
    super(map);
  }

  @Override
  protected void runMineralCheck() throws InterruptedException {
    if (detectedGold()) {
      knockMineral();
      return;
    }

    switchRight();

    if (detectedGold()) {
      knockMineral();
      switchLeft();
      return;
    }

    switchLeft();
    switchLeft();

    knockMineral();

    switchRight();
  }
}
