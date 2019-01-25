package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;

public class LandTask implements Task {
  private static final double POWER = 0.5;
  private static final int LAND_WAIT = 2000;

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
    Thread.sleep(500);
    bot.drivetrain.stop();
  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(0, -1, POWER);
    Thread.sleep(250);
    bot.drivetrain.setStrafe(1, 0, POWER);
    Thread.sleep(500);
    bot.drivetrain.stop();
  }

  private void lowerBot() throws InterruptedException {
    bot.hookArm.setLiftPower(-1.0);
    Thread.sleep(LAND_WAIT);
    bot.hookArm.setLiftPower(0);
  }

  private void startLowerGrabber() {
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(2000);
        bot.hookArm.setLiftPower(1.0);
        Thread.sleep(LAND_WAIT);
        bot.hookArm.setLiftPower(0);
      } catch (Exception e) {}
    });

    thread.start();
  }
}
