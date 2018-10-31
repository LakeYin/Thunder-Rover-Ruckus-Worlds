package org.firstinspires.ftc.teamcode.autonomous;

import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.andoverrobotics.core.utilities.Coordinate;
import java.util.HashMap;

public class TaskFactory {

  public static class MissingArgumentException extends RuntimeException {
    public MissingArgumentException(String message) {
      super(message);
    }
  }
  public static class NoSuchCommandException extends RuntimeException {
    public NoSuchCommandException(String message) {
      super(message);
    }
  }

  private final StrafingDriveTrain drivetrain;
  private HashMap<String, Runnable> customTasks = new HashMap<>();

  public TaskFactory(StrafingDriveTrain drivetrain) {
    this.drivetrain = drivetrain;
  }

  public void addCustomTask(String command, Runnable task) {
    customTasks.put(command, task);
  }

  public Runnable parseTask(String taskCommand) {
    final String trimmedCommand = taskCommand.trim();

    if (trimmedCommand.length() == 0)
      return () -> {};

    String[] tokens = trimmedCommand.split(" ");

    try {
      switch (tokens[0]) {
        case "move":
          double dx = Double.parseDouble(tokens[1]),
              dy = Double.parseDouble(tokens[2]);
          return new MoveTask(drivetrain, Coordinate.fromXY(dx, dy));

        case "rotate":
          int degrees = Integer.parseInt(tokens[1]);
          return new RotateTask(drivetrain, degrees);

        default:
          if (customTasks.containsKey(trimmedCommand)) {
            return customTasks.get(trimmedCommand);
          }
          throw new NoSuchCommandException(trimmedCommand);
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new MissingArgumentException(
          "Insufficient # of arguments for '" + trimmedCommand + "'");
    }
  }
}
