package org.firstinspires.ftc.teamcode;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.andoverrobotics.core.drivetrain.MecanumDrive;
import com.andoverrobotics.core.drivetrain.TankDrive;
import com.andoverrobotics.core.utilities.Converter;
import com.andoverrobotics.core.utilities.Coordinate;
import org.junit.Before;
import org.junit.Test;

public class TaskFactoryTest {
  private MecanumDrive strafingDrivetrain = mock(MecanumDrive.class);
  private TankDrive tankDrive = mock(TankDrive.class);
  private TaskFactory strafingFactory = new TaskFactory(strafingDrivetrain),
    tankDriveFactory = new TaskFactory(tankDrive);

  @Before
  public void setUp() {
    reset(strafingDrivetrain, tankDrive);
  }

  @Test
  public void moveCommandWithStrafingDrivetrain() {
    Runnable task = strafingFactory.parseTask("move 2.52 1.24");
    task.run();
    verify(strafingDrivetrain).strafeInches(Coordinate.fromXY(2.52, 1.24));
  }

  @Test
  public void moveInClockwiseWithTankDrive() {
    Runnable task = tankDriveFactory.parseTask("move 1.25 5.21");
    task.run();

    int rotationDegrees = (int) Math.round(Converter.radiansToDegrees(Math.atan2(1.25, 5.21)));

    verify(tankDrive).rotateClockwise(rotationDegrees);
    verify(tankDrive).driveForwards(Math.hypot(1.25, 5.21));
    verify(tankDrive).rotateCounterClockwise(rotationDegrees);
    verifyNoMoreInteractions(tankDrive);
  }

  @Test
  public void moveInCounterClockwiseWithTankDrive() {
    Runnable task = tankDriveFactory.parseTask("move -2 -10");
    task.run();

    int rotationDegrees = (int) -Math.round(Math.atan2(-2, -10)/Math.PI*180);

    verify(tankDrive).rotateCounterClockwise(rotationDegrees);
    verify(tankDrive).driveForwards(Math.hypot(-2, -10));
    verify(tankDrive).rotateClockwise(rotationDegrees);
  }

  @Test
  public void reverseWithTankDrive() {
    Runnable task = tankDriveFactory.parseTask("move 0 -10");
    task.run();

    verify(tankDrive).driveBackwards(10);
  }

  @Test
  public void emptyCommand() {
    Runnable task = strafingFactory.parseTask("\n  \r \t   \n");
    assertNotNull("Even if the input is empty, the output shouldn't be null", task);
  }

  @Test
  public void rotateCommandWithStrafingDrivetrain() {
    Runnable task = strafingFactory.parseTask("rotate 40");
    task.run();
    verify(strafingDrivetrain).rotateClockwise(40);

    task = tankDriveFactory.parseTask("rotate -20");
    task.run();
    verify(tankDrive).rotateCounterClockwise(20);
  }

  @Test(expected = MissingArgumentException.class)
  public void commandMissingArguments() {
    strafingFactory.parseTask("move 1.2");
  }

  @Test(expected = NumberFormatException.class)
  public void commandWithBadArgumentFormat() {
    strafingFactory.parseTask("rotate 20.5");
  }

  @Test(expected = NoSuchCommandException.class)
  public void unknownCommand() {
    strafingFactory.parseTask("hello world");
  }

  @Test
  public void customTasks() {
    Runnable task = mock(Runnable.class);
    String arcExpertise = "get into states by hosting qualifier";

    strafingFactory.addCustomTask(arcExpertise, task);
    strafingFactory.parseTask(arcExpertise).run();

    verify(task).run();
  }
}
