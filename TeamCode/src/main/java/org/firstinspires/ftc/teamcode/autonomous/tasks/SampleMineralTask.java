package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.andoverrobotics.core.config.Configuration;
import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.io.IOException;
import java.util.Optional;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector;
import org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral;

import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.GOLD;
import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.SILVER;

public class SampleMineralTask implements Task {

  // Fields and class need to be kept public (so that the schema can be loaded)
  public static class ConfigSchema {
    public double knockDistance;
    public int distanceBetweenMinerals;
    public int delayBeforeRecognition;
  }

  private ConfigSchema schema;
  public static MineralDetector detector;
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

  // Precondition: phone facing minerals
  // Post-condition: front facing minerals, left
  @Override
  public void run() {

    tellemReadMinerals();
    detectAsNecessary();
    tellemReadMinerals();

    Bot.getInstance().drivetrain.rotateCounterClockwise(90);

    if (AutonomousBot.centerMineral.orElse(SILVER) == GOLD) {
      knockMineral();
      switchLeft();
    } else if (AutonomousBot.rightMineral.orElse(SILVER) == GOLD) {
      switchRight();
      knockMineral();
      switchLeft();
      switchLeft();
    } else {
      switchLeft();
      knockMineral();
    }
  }

  protected void tellemReadMinerals() {
    Telemetry telemetry = Bot.getInstance().telemetry;

    telemetry.addData("center mineral", AutonomousBot.centerMineral);
    telemetry.addData("right mineral", AutonomousBot.rightMineral);
    telemetry.update();
  }

  protected void detectAsNecessary() {
    MecanumDrive drive = Bot.getInstance().drivetrain;

    if (!AutonomousBot.centerMineral.isPresent()) {
      detector.activate();
      AutonomousBot.centerMineral = Optional.of(detectedGold() ? GOLD : SILVER);
      detector.shutdown();
    }

    if (!AutonomousBot.rightMineral.isPresent()) {
      detector.activate();
      drive.rotateClockwise(40);
      AutonomousBot.rightMineral = Optional.of(detectedGold() ? GOLD : SILVER);
      detector.shutdown();
      drive.rotateCounterClockwise(40);
    }
  }

  protected void switchLeft() {
    Bot.getInstance().drivetrain.strafeLeft(schema.distanceBetweenMinerals);
  }

  protected void switchRight() {
    Bot.getInstance().drivetrain.strafeRight(schema.distanceBetweenMinerals);
  }

  protected void knockMineral() {
    tts.speak("Bueno, yo encontré el mineral correcto.");
    Bot bot = Bot.getInstance();

    bot.drivetrain.driveForwards(schema.knockDistance);
    bot.drivetrain.driveBackwards(schema.knockDistance);

  }

  protected boolean detectedGold() {
    try {
      Bot bot = Bot.getInstance();
      long startTime = System.currentTimeMillis();
      Optional<Mineral> recognition;

      AutonomousBot.sleep(schema.delayBeforeRecognition);
      while (!(recognition = detector.currentRecognition()).isPresent() &&
          System.currentTimeMillis() < startTime + 500) {
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
