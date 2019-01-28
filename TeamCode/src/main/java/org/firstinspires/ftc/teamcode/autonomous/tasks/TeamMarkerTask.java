package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;

public class TeamMarkerTask implements Task {

  public TeamMarkerTask() {
    Bot.getInstance().rightArm.setGrabberPosition(0.7);
  }

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();
    bot.rightArm.closeGrabber();
    Thread.sleep(400);
    bot.rightArm.setLiftPower(-0.3);
    Thread.sleep(800);
    bot.rightArm.setLiftPower(0);
  }
}
