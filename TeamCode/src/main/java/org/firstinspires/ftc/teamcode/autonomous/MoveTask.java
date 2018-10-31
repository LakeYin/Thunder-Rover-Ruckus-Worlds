package org.firstinspires.ftc.teamcode.autonomous;

import com.andoverrobotics.core.drivetrain.DriveTrain;
import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.andoverrobotics.core.utilities.Converter;
import com.andoverrobotics.core.utilities.Coordinate;

public class MoveTask implements Runnable {
  private final DriveTrain drivetrain;
  private final Coordinate offset;

  public MoveTask(DriveTrain drivetrain,
      Coordinate offset) {
    this.drivetrain = drivetrain;
    this.offset = offset;
  }

  @Override
  public void run() {
    if (drivetrain instanceof StrafingDriveTrain)
      ((StrafingDriveTrain)drivetrain).strafeInches(offset);
    else {
      int degrees = (int)Math.round(90 - offset.getPolarDirection());

      if (degrees > 0)
        drivetrain.rotateClockwise(degrees);
      else
        drivetrain.rotateCounterClockwise(-degrees);

      drivetrain.driveForwards(offset.getPolarDistance());

      if (degrees > 0)
        drivetrain.rotateCounterClockwise(degrees);
      else
        drivetrain.rotateClockwise(-degrees);
    }
  }
}
