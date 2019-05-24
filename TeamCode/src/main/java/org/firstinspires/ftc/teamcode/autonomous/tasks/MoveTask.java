package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.andoverrobotics.core.drivetrain.DriveTrain;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.andoverrobotics.core.utilities.Coordinate;

public class MoveTask implements Task {
  private final DriveTrain drivetrain;
  private final Coordinate offset;
  private final double speed;

  public MoveTask(DriveTrain drivetrain, Coordinate offset) {
    this(drivetrain, offset, drivetrain.getDefaultDrivePower());
  }

  public MoveTask(DriveTrain drivetrain,
      Coordinate offset, double speed) {
    this.drivetrain = drivetrain;
    this.offset = offset;
    this.speed = speed;
  }

  @Override
  public void run() {
    int startHeading = RotateByIMUTask.readHeading();

    if (drivetrain instanceof StrafingDriveTrain)
      ((StrafingDriveTrain)drivetrain).strafeInches(offset, speed);
    else {
      int degrees = (int)Math.round(90 - offset.getPolarDirection());

      if (degrees > 0)
        drivetrain.rotateClockwise(degrees, speed);
      else
        drivetrain.rotateCounterClockwise(-degrees, speed);

      drivetrain.driveForwards(offset.getPolarDistance(), speed);

      if (degrees > 0)
        drivetrain.rotateCounterClockwise(degrees, speed);
      else
        drivetrain.rotateClockwise(-degrees, speed);
    }

    int endHeading = RotateByIMUTask.readHeading();

  }
}
