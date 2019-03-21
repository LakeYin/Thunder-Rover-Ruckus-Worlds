package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.firstinspires.ftc.teamcode.Bot;

public class TeleOpTaskHost {

  public static final Runnable raiseHook = () -> {
    Bot bot = Bot.getInstance();
    DcMotor liftMotor = bot.hookArm.liftMotor;

    liftMotor.setMode(RunMode.RUN_TO_POSITION);
    liftMotor.setTargetPosition(liftMotor.getCurrentPosition() - 6800);
    bot.hookArm.setLiftPower(1.0);
    while (liftMotor.getCurrentPosition() - liftMotor.getTargetPosition() > 10 && !Thread
        .interrupted()) {
    }
    bot.hookArm.setLiftPower(0);
    liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
  };

  public static Runnable getLowerArm(Arm arm) {
    return () -> {
      arm.startRunningLiftToPosition(-850, 0.1);
      runArmToPositionAndLetDrop(arm);
    };
  }

  private static void runArmToPositionAndLetDrop(Arm arm) {
    while (arm.isLiftRunningToPosition() && !Thread.interrupted()) {
    }
    arm.setLiftPower(0);
    arm.liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    try {
      Thread.sleep(800);
      arm.liftMotor.setTargetPosition(arm.liftMotor.getCurrentPosition());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static Runnable getRaiseArm(Arm arm) {
    return () -> {
      arm.startRunningLiftToPosition(-520, 0.11);
      while (Math.abs(arm.liftMotor.getTargetPosition() - arm.liftMotor.getCurrentPosition()) > 100 && !Thread.interrupted()) {
      }
      arm.liftMotor.setPower(0.04);
      while (arm.isLiftRunningToPosition() && !Thread.interrupted()) {
      }
    };
  }

  private Thread thread;

  public void beginAsync(Runnable runnable) {
    if (!isRunning()) {
      thread = new Thread(runnable);
      thread.start();
    }
  }

  public boolean isRunning() {
    return thread != null && thread.isAlive();
  }

  public void abort() {
    thread.interrupt();
  }
}
