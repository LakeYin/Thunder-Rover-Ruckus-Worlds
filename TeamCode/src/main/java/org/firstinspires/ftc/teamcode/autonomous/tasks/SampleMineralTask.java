package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.io.IOException;
import java.util.Optional;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral;

public class SampleMineralTask implements Task {

  // Fields and class need to be kept public (so that the schema can be loaded)
  public static class ConfigSchema {
    public double knockDistance;
    public double distanceBetweenMinerals;
    public int delayBeforeRecognition;
    public int degreeBetweenMinerals;
    public boolean useRotation;
  }

  private ConfigSchema schema;
  private final MineralDetector detector;
  private AndroidTextToSpeech tts;

  public SampleMineralTask(HardwareMap map) {
    tts = new AndroidTextToSpeech();
    tts.initialize();
    tts.setLanguage("es");
    populateSchema();
    detector = new MineralDetector(map);
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
    detector.activate();
    runMineralCheck();
    detector.shutdown();
  }

  public void shutdown() {
    detector.shutdown();
  }

  private void runMineralCheck() {
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

  private void switchLeft() {
    if (schema.useRotation) {
      Bot.getInstance().drivetrain.rotateClockwise(schema.degreeBetweenMinerals);
    } else {
      Bot.getInstance().drivetrain.strafeLeft(schema.distanceBetweenMinerals);
    }
  }

  private void switchRight() {
    if (schema.useRotation) {
      Bot.getInstance().drivetrain.rotateCounterClockwise(schema.degreeBetweenMinerals);
    } else {
      Bot.getInstance().drivetrain.strafeRight(schema.distanceBetweenMinerals);
    }
  }

  private void knockMineral() {
    tts.speak("Bueno, yo encontré el mineral correcto.");
    final StrafingDriveTrain drive = Bot.getInstance().drivetrain;
    drive.driveBackwards(schema.knockDistance);
    drive.driveForwards(schema.knockDistance);
  }

  private boolean detectedGold() {
    try {
      long startTime = System.currentTimeMillis();
      Optional<Mineral> recognition;

      AutonomousBot.sleep(schema.delayBeforeRecognition);
      while (!(recognition = detector.currentRecognition()).isPresent() &&
          System.currentTimeMillis() < startTime + 200) {
        if (Thread.interrupted())
          throw new InterruptedException("Interrupted while waiting for mineral recognition");

        Bot.getInstance().opMode.telemetry.addData("Seen", recognition);
        Bot.getInstance().opMode.telemetry.update();
      }

      return recognition.orElse(Mineral.SILVER) == Mineral.GOLD;

    } catch (InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }
}
