package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

public class TeamMarkerTask implements Task {

  public TeamMarkerTask() {
    Bot.getInstance().rightArm.setGrabberPosition(0.2);
  }

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();

    bot.rightArm.startRunningLiftToPosition(-100, 0.3);

    startExtendingArm(bot, bot.rightArm);

    while (bot.rightArm.isLiftRunningToPosition() && AutonomousBot.isActive());
    bot.rightArm.openGrabber();

    AutonomousBot.sleep(1000);
    int targetPosition = bot.mainConfig.getInt("armDownPosition") + 100;
    bot.rightArm.startRunningLiftToPosition(targetPosition, 0.2);
    bot.leftArm.startRunningLiftToPosition(targetPosition, 0.2);
    startExtendingArm(bot, bot.leftArm);
  }

  private void startExtendingArm(Bot bot, Arm arm) {
    arm.setExtenderPower(0.8);
    double startSeconds = AutonomousBot.secondsRemaining();
    new Thread(() -> {
      while (startSeconds - AutonomousBot.secondsRemaining() < 13 && AutonomousBot.secondsRemaining() > 0 && AutonomousBot.isActive());
      bot.rightArm.setExtenderPower(0);
    }).start();
  }
}
