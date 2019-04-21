package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RunToPosition {
  private static ExecutorService executor = Executors.newCachedThreadPool();

  private DcMotor motor;
  private int targetPosition;
  private double speed;

  public RunToPosition(DcMotor motor, int targetPosition, double speed) {
    this.motor = motor;
    this.targetPosition = targetPosition;
    this.speed = speed;
  }

  public RunToPosition begin() {
    motor.setTargetPosition(targetPosition);
    motor.setMode(RunMode.RUN_TO_POSITION);
    motor.setPower(Math.abs(speed));
    return this;
  }

  public void waitUntilDone() {
    while (!isDone() && Bot.getInstance().opMode.opModeIsActive()) {
      Thread.yield();
    }
  }

  public void abort() {
    if (!isDone()) {
      motor.setPower(0);
      motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    }
  }

  public Future whenDone(Runnable action) {
    return executor.submit(() -> {
      waitUntilDone();
      action.run();
    });
  }

  public boolean isDone() {
    return !motor.isBusy() && Math.abs(motor.getCurrentPosition() - targetPosition) < 20;
  }
}
