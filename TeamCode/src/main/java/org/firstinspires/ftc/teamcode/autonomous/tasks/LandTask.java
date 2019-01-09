package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;

public class LandTask implements Task {
  private static final double POWER = 0.5;

  private Bot bot = Bot.getInstance();

  @Override
  public void run() throws InterruptedException {
    lowerBot();
    exitHook();
    startLowerGrabber();
    strafeBot();
  }

  private void exitHook() throws InterruptedException {
    bot.drivetrain.setStrafe(-1, 0, POWER);
    Thread.sleep(200);
    bot.drivetrain.stop();
  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(0, -1, POWER);
    Thread.sleep(150);
    bot.drivetrain.setStrafe(1, 0, POWER);
    Thread.sleep(200);
    bot.drivetrain.stop();
  }

  private void lowerBot() throws InterruptedException {
    bot.hookArm.setLiftPower(-1.0);
    Thread.sleep(1900);
    bot.hookArm.setLiftPower(0);
  }

  private void startLowerGrabber() {
    Thread thread = new Thread(() -> {
      try {
        bot.hookArm.setLiftPower(1.0);
        Thread.sleep(1900);
        bot.hookArm.setLiftPower(0);
      } catch (Exception e) {}
    });

    thread.start();
  }
}
