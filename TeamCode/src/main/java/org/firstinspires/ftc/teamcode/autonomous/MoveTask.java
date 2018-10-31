package org.firstinspires.ftc.teamcode.autonomous;

import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;
import com.andoverrobotics.core.utilities.Coordinate;

public class MoveTask implements Runnable {
  private final StrafingDriveTrain drivetrain;
  private final Coordinate offset;

  public MoveTask(StrafingDriveTrain drivetrain,
      Coordinate offset) {
    this.drivetrain = drivetrain;
    this.offset = offset;
  }

  @Override
  public void run() {
    drivetrain.strafeInches(offset);
  }
}
