package org.firstinspires.ftc.teamcode.teleop;

import android.util.Log;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.tasks.LandTask;

public class TeleOpTaskHost {
  public static final Runnable raiseHook = () -> {
    Bot bot = Bot.getInstance();
    DcMotor liftMotor = bot.hookArm.liftMotor;

    liftMotor.setMode(RunMode.RUN_TO_POSITION);
    liftMotor.setTargetPosition(liftMotor.getCurrentPosition() - 6361);
    bot.hookArm.setLiftPower(1.0);
    while (liftMotor.getCurrentPosition() - liftMotor.getTargetPosition() > 10 && !Thread.interrupted());
    bot.hookArm.setLiftPower(0);
    liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
  };

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
