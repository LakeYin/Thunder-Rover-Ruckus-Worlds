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

  @Override
  public void runOpMode() {
    try {

      initFields();
      waitForStart();
      executeCommands(CONFIG_PATH + "auto-land.task");
      //executeCommands(taskFilenameForDetectedTarget());

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String taskFilenameForDetectedTarget() {
    detector.activate();
    Target target = waitForVisibleTarget(5000);

    if (target == null) {
      return Math.random() > 0.5 ? CRATER_LEFT_TASK : CRATER_RIGHT_TASK;
    }

    return target == Target.BACK_SPACE || target == Target.FRONT_CRATERS ?
        CRATER_LEFT_TASK : CRATER_RIGHT_TASK;
  }

  private void initFields() throws IOException {
    bot = new AutonomousBot(this);
    tasks = new TaskFactory(bot.drivetrain);
    detector = new VuMarkDetector(hardwareMap);

    tasks.addCustomTask("sample_mineral", new SampleMineralTask(hardwareMap));
    tasks.addCustomTask("land", new LandTask());
    tasks.addCustomTask("drop_team_marker", new TeamMarkerTask());

  }

  private Target waitForVisibleTarget(int msTimeout) {
    double startTime = getRuntime();

    while ((getRuntime() - startTime) * 1000 < msTimeout) {
      Optional<Target> target = detector.visibleTarget();

      if (target.isPresent())
        return target.get();
    }

    return null;
  }

  private void executeCommands(String filename) throws FileNotFoundException {
    String[] commands = tasks.commandsInFile(filename);
    for (String command : commands) {
      Runnable task = tasks.parseTask(command);

      if (task != null) {
        telemetry.addData("Task file", filename);
        telemetry.addData("Running task", command);
        telemetry.update();
        Log.d("Task Runner", "Running " + command);
        task.run();
      }
    }
    telemetry.addData("Done running", filename);
    telemetry.update();
  }
}
