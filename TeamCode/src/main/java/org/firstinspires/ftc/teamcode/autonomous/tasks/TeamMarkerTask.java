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
    while (bot.rightArm.isLiftRunningToPosition() && AutonomousBot.isActive());
    bot.rightArm.openGrabber();
  }
}
