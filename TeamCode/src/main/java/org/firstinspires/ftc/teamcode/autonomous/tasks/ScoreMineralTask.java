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
      runArmsToPosition(bot, downPos);
      bot.drivetrain.strafeInches(1, 2, 0.2);
      bot.leftArm.closeGrabber();
      bot.rightArm.closeGrabber();
      AutonomousBot.sleep(400);

      if (AutonomousBot.secondsRemaining() < 4)
        break;

      runArmsToPosition(bot, upPos + 40);
      bot.drivetrain.strafeInches(-1, -2, 0.4);

      bot.leftArm.openGrabber();
      bot.rightArm.openGrabber();
      AutonomousBot.sleep(1000);
    }

    runArmsToPosition(bot, downPos);
  }

  private void runArmsToPosition(Bot bot, int position) {
    bot.leftArm.startRunningLiftToPosition(position, kRotationSpeed);
    bot.rightArm.startRunningLiftToPosition(position, kRotationSpeed);
    while (bot.leftArm.isLiftRunningToPosition() && bot.rightArm.isLiftRunningToPosition()
        && AutonomousBot.isActive()) {
    }
  }
}
