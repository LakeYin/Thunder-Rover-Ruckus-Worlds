package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.io.IOException;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral;

public class SampleMineralTask implements Runnable {

  // Fields and class need to be kept public (so that the schema can be loaded)
  public static class ConfigSchema {
    public double knockDistance;
    public double distanceBetweenMinerals;
    public int delayBeforeRecognition;
  }

  private ConfigSchema schema;
  private final MineralDetector detector;

  public SampleMineralTask(HardwareMap map) {
    populateSchema();
    detector = new MineralDetector(map);
    detector.activate();
  }

  private void populateSchema() {
    try {
      schema = new ConfigSchema();
      Configuration.fromPropertiesFile("sampleMineral.properties").loadToSchema(schema);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // Precondition: detector activated and phone facing minerals
  @Override
  public void run() {
    runMineralCheck();
    detector.shutdown();
  }

  private void runMineralCheck() {
    final StrafingDriveTrain driveTrain = Bot.getInstance().drivetrain;

    if (detectedGold()) {
      knockMineral();
      return;
    }

    driveTrain.strafeRight(schema.distanceBetweenMinerals);

    if (detectedGold()) {
      knockMineral();
      driveTrain.strafeLeft(schema.distanceBetweenMinerals);
      return;
    }

    driveTrain.strafeLeft(schema.distanceBetweenMinerals * 2);

    knockMineral();
    driveTrain.strafeLeft(schema.distanceBetweenMinerals);
  }

  private void knockMineral() {
    final StrafingDriveTrain drive = Bot.getInstance().drivetrain;
    drive.driveBackwards(schema.knockDistance);
    drive.driveForwards(schema.knockDistance);
  }

  private boolean detectedGold() {
    try {

      Thread.sleep(schema.delayBeforeRecognition);
      return detector.currentRecognition().orElse(null) == Mineral.GOLD;

    } catch (InterruptedException e) {

      e.printStackTrace();
      return false;
    }
  }
}
