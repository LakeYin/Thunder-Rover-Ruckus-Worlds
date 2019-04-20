package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.tasks.LandTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.SampleMineralTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.ScoreMineralTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.Task;
import org.firstinspires.ftc.teamcode.autonomous.tasks.TaskFactory;
import org.firstinspires.ftc.teamcode.autonomous.tasks.TeamMarkerTask;

public abstract class AutoOpMode extends LinearOpMode {

  private static final String CONFIG_PATH = "/storage/self/primary/FIRST/config/";

  private AutonomousBot bot;
  private TaskFactory tasks;

  @Override
  public abstract void runOpMode();

  protected void runOpMode(String filename) {
    try {

      initFields();
      spamTelemetryAndWaitForStart();
      startSpammingTelemetry();
      throwIfInterrupted();
      readMineralInitially();
      executeCommands(CONFIG_PATH + filename);

    } catch (Exception e) {
      e.printStackTrace();
      //cleanup();
    } finally {
      stopSpammingTelemetry();
    }
  }

  private void readMineralInitially() {
    SampleMineralTask.detector.activate();
    Bot.sleep(1000);
    AutonomousBot.centerMineral = SampleMineralTask.detector.currentRecognition();
    Log.d("Minerals", "Center = " + AutonomousBot.centerMineral);
  }

  private void spamTelemetryAndWaitForStart() {
    while (!opModeIsActive() && !isStopRequested()) {
      telemetry.addData("Waiting in Init", System.currentTimeMillis());
      telemetry.update();
      telemetry.clearAll();
    }
  }

  private void throwIfInterrupted() throws InterruptedException {
    if (!opModeIsActive())
      throw new InterruptedException("OpMode stopped manually");
  }

  private void initFields() throws IOException {
    VuforiaManager.initVuforia(hardwareMap);
    bot = new AutonomousBot(this);
    tasks = new TaskFactory(bot.drivetrain);
    SampleMineralTask sampleMineralTask = getSampleMineralTask();

    tasks.addCustomTask("sample_mineral", sampleMineralTask);
    tasks.addCustomTask("land", new LandTask());
    tasks.addCustomTask("drop_team_marker", new TeamMarkerTask());
    tasks.addCustomTask("score_minerals", new ScoreMineralTask());
  }

  protected abstract SampleMineralTask getSampleMineralTask();

  private void executeCommands(String filename) throws FileNotFoundException, InterruptedException {
    String[] commands = tasks.commandsInFile(filename);
    for (String command : commands) {
      Task task = tasks.parseTask(command);
      throwIfInterrupted();

      if (task != null) {
        telemetry.addData("Task file", filename);
        telemetry.addData("Running task", command);
        telemetry.update();
        Log.d("Task Runner", String.format("Running %s from file %s", command, filename));
        //startSpammingTelemetry();
        task.run();
        //stopSpammingTelemetry();
        throwIfInterrupted();
      }
    }
    telemetry.addData("Done running", filename);
    telemetry.update();
  }

  private final Runnable spamTelemetry = () -> {
    while (!Thread.interrupted()) {
      telemetry.update();
    }
  };

  private Thread telemetrySpammer;

  private void stopSpammingTelemetry() {
    if (telemetrySpammer != null)
      telemetrySpammer.interrupt();
  }

  private void startSpammingTelemetry() {
    stopSpammingTelemetry();
    telemetrySpammer = new Thread(spamTelemetry);
    telemetrySpammer.start();
  }
}
