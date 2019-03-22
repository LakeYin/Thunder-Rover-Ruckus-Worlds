package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public abstract class AutoOpMode extends LinearOpMode {

  private static final String CONFIG_PATH = "/storage/self/primary/FIRST/config/";

  private AutonomousBot bot;
  private TaskFactory tasks;
  private AutoAsyncTaskHost asyncTaskHost;

  private Runnable extendLeftArm = () -> {
    int armPosition = bot.mainConfig.getInt("armUpPosition");
    bot.leftArm.startRunningLiftToPosition(armPosition, 0.15);
    while (bot.leftArm.isLiftRunningToPosition() && AutonomousBot.isActive());

    bot.leftArm.setExtenderPower(0.8);
    AutonomousBot.sleep(13000);
    bot.leftArm.setExtenderPower(0);
  };

  @Override
  public abstract void runOpMode();

  protected void runOpMode(String part2Filename) {
    try {

      initFields();
      spamTelemetryAndWaitForStart();
      throwIfInterrupted();
      executeCommands(CONFIG_PATH + "auto-part1.task");
      asyncTaskHost.submit(extendLeftArm);
      executeCommands(CONFIG_PATH + part2Filename);
      letArmsRest();

    } catch (Exception e) {
      e.printStackTrace();
      stopSpammingTelemetry();
      //cleanup();
    }
  }

  private void letArmsRest() {
    for (DcMotor motor : Arrays.asList(bot.leftArm.liftMotor, bot.rightArm.liftMotor)) {
      motor.setPower(0);
      motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    }
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
    telemetry.setAutoClear(false);
    VuforiaManager.initVuforia(hardwareMap);
    bot = new AutonomousBot(this);
    tasks = new TaskFactory(bot.drivetrain);
    SampleMineralTask sampleMineralTask = new SampleMineralTask(hardwareMap);

    tasks.addCustomTask("sample_mineral", sampleMineralTask);
    tasks.addCustomTask("land", new LandTask(bot.mainConfig.getBoolean("landWithEncoder")));
    tasks.addCustomTask("drop_team_marker", new TeamMarkerTask());
    tasks.addCustomTask("score_minerals", new ScoreMineralTask());
    asyncTaskHost = new AutoAsyncTaskHost();
  }

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
