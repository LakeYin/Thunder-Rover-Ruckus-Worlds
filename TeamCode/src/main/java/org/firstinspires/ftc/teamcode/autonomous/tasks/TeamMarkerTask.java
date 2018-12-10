package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;

public class TeamMarkerTask implements Runnable {

  @Override
  public void run() {
    Bot bot = Bot.getInstance();
    bot.teamMarker.setPosition(0.0);
    bot.teamMarker.setPosition(1.0);
  }
}
