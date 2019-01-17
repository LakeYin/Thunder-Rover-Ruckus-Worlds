package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;

public class TeamMarkerTask implements Task {

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();
    bot.rightArm.setLiftPower(-0.5);
    Thread.sleep(500);
    bot.rightArm.setLiftPower(0);

    bot.teamMarker.setPosition(0.0);
    Thread.sleep(1000);
    bot.teamMarker.setPosition(0.5);
    Thread.sleep(1000);
  }
}
