package org.firstinspires.ftc.teamcode.autonomous;

import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral;

public class SampleMineralTask implements Runnable {

  private static final int kKnockDistance = 18;
  private static final double kDistanceBetweenMinerals = 14.5;

  private final MineralDetector detector;

  public SampleMineralTask(HardwareMap map) {
    detector = new MineralDetector(map);
    detector.activate();
  }

  // Precondition: detector activated and phone facing minerals
  @Override
  public void run() {
    final StrafingDriveTrain driveTrain = Bot.getInstance().drivetrain;

    if (detectedGold()) {
      knockMineral();
      return;
    }

    driveTrain.strafeLeft(kDistanceBetweenMinerals);

    if (detectedGold()) {
      knockMineral();
      driveTrain.strafeRight(kDistanceBetweenMinerals);
      return;
    }

    driveTrain.strafeRight(kDistanceBetweenMinerals * 2);

    knockMineral();
    driveTrain.strafeLeft(kDistanceBetweenMinerals);
  }

  private void knockMineral() {
    final StrafingDriveTrain drive = Bot.getInstance().drivetrain;
    drive.driveForwards(kKnockDistance);
    drive.driveBackwards(kKnockDistance);
  }

  private boolean detectedGold() {
    try {

      Thread.sleep(2000);
      return detector.currentRecognition().orElse(null) == Mineral.GOLD;

    } catch (InterruptedException e) {

      e.printStackTrace();
      return false;
    }
  }
}
