package org.firstinspires.ftc.teamcode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.utilities.Coordinate;
import org.firstinspires.ftc.teamcode.autonomous.TaskFactory;
import org.firstinspires.ftc.teamcode.autonomous.TaskFactory.MissingArgumentException;
import org.firstinspires.ftc.teamcode.autonomous.TaskFactory.NoSuchCommandException;
import org.junit.Before;
import org.junit.Test;

public class TaskFactoryTest {
  private MecanumDrive drivetrain = mock(MecanumDrive.class);
  private TaskFactory factory = new TaskFactory(drivetrain);

  @Before
  public void setUp() throws Exception {
    reset(drivetrain);
  }

  @Test
  public void moveCommand() {
    Runnable task = factory.parseTask("move 2.52 1.24");
    task.run();
    verify(drivetrain).strafeInches(Coordinate.fromXY(2.52, 1.24));
  }

  @Test
  public void emptyCommand() {
    Runnable task = factory.parseTask("\n  \r \t   \n");
    assertNotNull("Even if the input is empty, the output shouldn't be null", task);
  }

  @Test
  public void rotateCommand() {
    Runnable task = factory.parseTask("rotate 40");
    task.run();
    verify(drivetrain).rotateClockwise(40);

    task = factory.parseTask("rotate -20");
    task.run();
    verify(drivetrain).rotateCounterClockwise(20);
  }

  @Test(expected = MissingArgumentException.class)
  public void commandMissingArguments() {
    factory.parseTask("move 1.2");
  }

  @Test(expected = NumberFormatException.class)
  public void name() {
    factory.parseTask("rotate 20.5");
  }

  @Test(expected = NoSuchCommandException.class)
  public void unknownCommand() {
    factory.parseTask("hello world");
  }

  @Test
  public void customTasks() {
    Runnable task = mock(Runnable.class);
    String arcExpertise = "get into states by hosting qualifier";

    factory.addCustomTask(arcExpertise, task);
    factory.parseTask(arcExpertise).run();

    verify(task).run();
  }
}
