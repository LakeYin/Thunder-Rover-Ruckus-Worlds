package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

public class ScoreMineralTask implements Task {

  public static final double kRotationSpeed = 0.2;

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();
    int downPos = bot.mainConfig.getInt("armDownPosition"),
        upPos = bot.mainConfig.getInt("armUpPosition");

    while (AutonomousBot.secondsRemaining() > 3) {
      // TODO Score minerals
    }


  }
}
