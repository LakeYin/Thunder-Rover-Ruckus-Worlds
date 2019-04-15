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

public abstract class SampleMineralTask implements Task {

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

  protected abstract void runMineralCheck() throws InterruptedException;

  protected void switchLeft() {
    Bot.getInstance().drivetrain.driveBackwards(schema.distanceBetweenMinerals);
  }

  protected void switchRight() {
    Bot.getInstance().drivetrain.driveForwards(schema.distanceBetweenMinerals);
  }

  protected void knockMineral() throws InterruptedException {
    tts.speak("Bueno, yo encontré el mineral correcto.");
    Bot bot = Bot.getInstance();
    bot.drivetrain.rotateCounterClockwise(90);

    bot.intake.extend(schema.knockDistance * 0.7).begin().waitUntilDone();
    bot.intake.orientToCollect();
    bot.intake.runSweeperIn();
    bot.intake.extend(schema.knockDistance, 0.3).begin().waitUntilDone();
    bot.intake.orientToTransit();
    bot.intake.stopSweeper();
    bot.intake.retractFully().begin().waitUntilDone();
    bot.intake.runSweeperIn();
    bot.intake.orientToTransfer();
    Bot.sleep(2000);
    bot.intake.stopSweeper();
    bot.intake.orientToTransit();
    bot.intake.extend(0.2, 0.4).begin();

    bot.drivetrain.rotateClockwise(90);
  }

  protected boolean detectedGold() {
    try {
      Bot bot = Bot.getInstance();
      long startTime = System.currentTimeMillis();
      Optional<Mineral> recognition;

      AutonomousBot.sleep(schema.delayBeforeRecognition);
      while (!(recognition = detector.currentRecognition()).isPresent() &&
          System.currentTimeMillis() < startTime + 200) {
        if (Thread.interrupted())
          throw new InterruptedException("Interrupted while waiting for mineral recognition");

        bot.opMode.telemetry.addData("Seen", recognition);
        bot.opMode.telemetry.update();
      }

      updateLed(bot, recognition);
      return recognition.orElse(Mineral.SILVER) == Mineral.GOLD;

    } catch (InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }

  private void updateLed(Bot bot, Optional<Mineral> recognition) {
    int[] values = recognition
        .map(mineral -> (mineral.equals(Mineral.SILVER) ? new int[]{255, 255, 255} :
            new int[]{255, 223, 0})).orElseGet(() -> new int[]{255, 0, 0});

    bot.hub2.setLedColor(values[0], values[1], values[2]);
    bot.hub7.setLedColor(values[0], values[1], values[2]);
  }
}
