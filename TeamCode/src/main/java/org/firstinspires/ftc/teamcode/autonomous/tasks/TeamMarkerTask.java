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
    bot.rightArm.openGrabber();
    AutonomousBot.sleep(300);
    bot.rightArm.setLiftPower(-0.3);
    AutonomousBot.sleep(1100);
    bot.rightArm.setLiftPower(0);
  }
}
