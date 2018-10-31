package org.firstinspires.ftc.teamcode.autonomous;

import com.andoverrobotics.core.drivetrain.StrafingDriveTrain;

public class RotateTask implements Runnable {
  private final StrafingDriveTrain drivetrain;
  private final int degrees;

  public RotateTask(StrafingDriveTrain drivetrain, int degrees) {
    this.drivetrain = drivetrain;
    this.degrees = degrees;
  }

  @Override
  public void run() {
    if (degrees > 0)
      drivetrain.rotateClockwise(degrees);
    else
      drivetrain.rotateCounterClockwise(-degrees);
  }
}
