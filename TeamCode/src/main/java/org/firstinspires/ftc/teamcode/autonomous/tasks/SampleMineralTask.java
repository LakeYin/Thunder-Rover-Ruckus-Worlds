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
  public void run() throws InterruptedException {
    detector.activate();
    runMineralCheck();
    detector.shutdown();
  }

  private void runMineralCheck() throws InterruptedException {
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

    switchRight();

    knockMineral();

    switchLeft();
    switchLeft();
  }

  private void switchLeft() {
    Bot.getInstance().drivetrain.driveBackwards(schema.distanceBetweenMinerals);
  }

  private void switchRight() {
    Bot.getInstance().drivetrain.driveForwards(schema.distanceBetweenMinerals);
  }

  private void knockMineral() throws InterruptedException {
    tts.speak("Bueno, yo encontré el mineral correcto.");
    Bot bot = Bot.getInstance();
    bot.drivetrain.rotateCounterClockwise(90);

    bot.intake.extend(schema.knockDistance, 0.8);
    bot.intake.runSweeperIn();
    Bot.sleep(800);
    bot.intake.orientToTransit();
    bot.intake.stopSweeper();
    bot.intake.retractFully(0.8);
    bot.intake.orientToTransfer();
    bot.intake.runSweeperIn();
    Bot.sleep(2000);
    bot.intake.stopSweeper();
    bot.deposit.prepareToDeposit();
    bot.intake.extend(0.2, 0.4);

    bot.drivetrain.rotateClockwise(90);
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
