package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.andoverrobotics.core.drivetrain.DriveTrain;

public class RotateTask implements Task {
  private final DriveTrain drivetrain;
  private final int degrees;

  public RotateTask(DriveTrain drivetrain, int degrees) {
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
