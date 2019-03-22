package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.firstinspires.ftc.teamcode.Bot;

public class TeleOpTaskHost {

  public static final Runnable raiseHook = () -> {
    Bot bot = Bot.getInstance();
    DcMotor liftMotor = bot.hookLift.liftMotor;

    liftMotor.setMode(RunMode.RUN_TO_POSITION);
    liftMotor.setTargetPosition(liftMotor.getCurrentPosition() - 6800);
    bot.hookLift.adjust(1.0);
    while (liftMotor.getCurrentPosition() - liftMotor.getTargetPosition() > 10 && !Thread
        .interrupted()) {
    }
    bot.hookLift.adjust(0);
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
