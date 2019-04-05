package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import org.firstinspires.ftc.teamcode.Bot;

public class LandTask implements Task {

  public LandTask() {
    bot.hookLift.liftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
  }

  private Bot bot = Bot.getInstance();

  @Override
  public void run() throws InterruptedException {
    lowerBot();
    exitHook();
    strafeBot();
    startLoweringHook();
  }

  private void exitHook() throws InterruptedException {
    bot.drivetrain.setStrafe(0, 1, 1);
    Thread.sleep(180);
    bot.drivetrain.stop();
  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(-1, 0, 1);
    Thread.sleep(650);
    bot.drivetrain.setStrafe(0, -1, 1);
    Thread.sleep(250);
    bot.drivetrain.stop();
  }

  private void lowerBot() throws InterruptedException {
    bot.hookLift.liftToHook();
  }

  private void startLoweringHook() {
    new Thread(() -> {
      try {
        bot.hookLift.lowerToBottom();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }
}
