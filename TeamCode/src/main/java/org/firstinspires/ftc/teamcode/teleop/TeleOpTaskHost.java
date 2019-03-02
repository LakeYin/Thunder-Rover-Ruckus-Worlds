package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.Bot;

public class TeleOpTaskHost {

  public static final Runnable raiseHook = () -> {
    Bot bot = Bot.getInstance();
    DcMotor liftMotor = bot.hookArm.liftMotor;

    liftMotor.setMode(RunMode.RUN_TO_POSITION);
    liftMotor.setTargetPosition(liftMotor.getCurrentPosition() - 6361);
    bot.hookArm.setLiftPower(1.0);
    while (liftMotor.getCurrentPosition() - liftMotor.getTargetPosition() > 10 && !Thread
        .interrupted()) {
      ;
    }
    bot.hookArm.setLiftPower(0);
    liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
  };

  public static Runnable getLowerArm(Arm arm) {
    return () -> {
      arm.startRunningLiftToPosition(-780, 0.15);
      while (arm.isLiftRunningToPosition() && !Thread.interrupted()) {
      }
      arm.setLiftPower(0);
      arm.liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    };
  }

  public static Runnable getRaiseArm(Arm arm) {
    return () -> {
      arm.startRunningLiftToPosition(-350, 0.23);
      while (arm.isLiftRunningToPosition() && !Thread.interrupted()) {
      }
      arm.setLiftPower(0);
      arm.liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
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
