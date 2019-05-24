package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.andoverrobotics.core.drivetrain.DriveTrain;
import com.andoverrobotics.core.utilities.Coordinate;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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

  private final DriveTrain drivetrain;
  private HashMap<String, Task> customTasks = new HashMap<>();

  public TaskFactory(DriveTrain drivetrain) {
    this.drivetrain = drivetrain;
  }

  public void addCustomTask(String command, Task task) {
    customTasks.put(command, task);
  }

  public String[] commandsInFile(String filename) throws FileNotFoundException {
    Scanner scanner = new Scanner(new File(filename));
    List<String> commands = new ArrayList<>();

    while (scanner.hasNextLine())
      commands.add(scanner.nextLine());

    return commands.toArray(new String[0]);
  }

  public Task parseTask(String taskCommand) {
    final String trimmedCommand = taskCommand.trim();

    if (trimmedCommand.length() == 0)
      return () -> {};

    String[] tokens = trimmedCommand.split(" ");

    try {
      switch (tokens[0]) {
        case "move":
          double dx = Double.parseDouble(tokens[1]),
              dy = Double.parseDouble(tokens[2]);

          if (tokens.length > 3) {
            return new MoveTask(drivetrain, Coordinate.fromXY(dx, dy), Double.parseDouble(tokens[3]));
          }
          return new MoveTask(drivetrain, Coordinate.fromXY(dx, dy));

        case "rotate":
          int degrees = Integer.parseInt(tokens[1]);
          return new RotateTask(drivetrain, degrees);

        case "rotateIMU":
          int deg = Integer.parseInt(tokens[1]);
          return new RotateByIMUTask(drivetrain, deg);

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
