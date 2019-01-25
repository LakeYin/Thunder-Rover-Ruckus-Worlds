package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import org.firstinspires.ftc.teamcode.autonomous.VuMarkDetector.Target;
import org.firstinspires.ftc.teamcode.autonomous.tasks.LandTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.SampleMineralTask;
import org.firstinspires.ftc.teamcode.autonomous.tasks.Task;
import org.firstinspires.ftc.teamcode.autonomous.tasks.TaskFactory;
import org.firstinspires.ftc.teamcode.autonomous.tasks.TeamMarkerTask;

@Autonomous(name = "Main Autonomous", group = "ARC Lightning")
public class AutoOpMode extends LinearOpMode {

  private static final String CONFIG_PATH = "/storage/self/primary/FIRST/config/";

  private static final String
      CRATER_LEFT_TASK = CONFIG_PATH + "auto-craterLeft.task",
      CRATER_RIGHT_TASK = CONFIG_PATH + "auto-craterRight.task";

  private AutonomousBot bot;
  private TaskFactory tasks;
  private VuMarkDetector detector;
  private SampleMineralTask sampleMineralTask;

  @Override
  public void runOpMode() {
    try {

      initFields();
      spamTelemetryAndWaitForStart();
      throwIfInterrupted();
      executeCommands(CONFIG_PATH + "auto-part1.task");
      executeCommands(taskFilenameForDetectedTarget());

    } catch (Exception e) {
      e.printStackTrace();
      //cleanup();
    }
  }

  private void spamTelemetryAndWaitForStart() {
    while (!opModeIsActive() && !isStopRequested()) {
      telemetry.addData("Waiting in Init", System.currentTimeMillis());
      telemetry.update();
    }
  }

  private void throwIfInterrupted() throws InterruptedException {
    if (Thread.interrupted())
      throw new InterruptedException("OpMode stopped manually");
  }

  private String taskFilenameForDetectedTarget() {
    detector = new VuMarkDetector();
    detector.activate();
    Target target = waitForVisibleTarget(5000);

    if (target == null) {
      return Math.random() > 0.5 ? CRATER_LEFT_TASK : CRATER_RIGHT_TASK;
    }

    return target == Target.BACK_SPACE || target == Target.FRONT_CRATERS ?
        CRATER_LEFT_TASK : CRATER_RIGHT_TASK;
  }

  private void initFields() throws IOException {
    VuforiaManager.initVuforia(hardwareMap);
    bot = new AutonomousBot(this);
    tasks = new TaskFactory(bot.drivetrain);
    sampleMineralTask = new SampleMineralTask(hardwareMap);

    tasks.addCustomTask("sample_mineral", sampleMineralTask);
    tasks.addCustomTask("land", new LandTask());
    tasks.addCustomTask("drop_team_marker", new TeamMarkerTask());
  }

  private Target waitForVisibleTarget(int msTimeout) {
    double startTime = getRuntime();

    while ((getRuntime() - startTime) * 1000 < msTimeout && !Thread.interrupted() && opModeIsActive()) {
      Optional<Target> target = detector.visibleTarget();

      if (target.isPresent())
        return target.get();
    }

    return null;
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
        task.run();
        throwIfInterrupted();
      }
    }
    telemetry.addData("Done running", filename);
    telemetry.update();
  }
}
