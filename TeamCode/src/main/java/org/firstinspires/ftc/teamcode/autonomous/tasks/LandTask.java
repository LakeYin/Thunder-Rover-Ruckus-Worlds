package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;

public class LandTask implements Runnable {

  private Bot bot = Bot.getInstance();

  @Override
  public void run() {
    try {

      lowerBot();
      strafeBot();
      lowerGrabber();

    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(1, 0, 0.5);
    Thread.sleep(500);

  }

  private void lowerBot() throws InterruptedException {
    bot.hookArm.setLiftPower(0.3);
    Thread.sleep(1000);
    bot.hookArm.setLiftPower(0);
  }

  private void lowerGrabber() throws InterruptedException {
    bot.hookArm.setLiftPower(-0.3);
    Thread.sleep(1500);
    bot.hookArm.setLiftPower(0);
  }
}
