package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

public class TeamMarkerTask implements Task {

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();

    bot.intake.extend(0.7).begin().waitUntilDone();
    bot.intake.orientToTransfer();
    bot.intake.runSweeperIn();
    Bot.sleep(2000);
    bot.intake.orientToTransit();
    bot.intake.stopSweeper();
    bot.intake.retractFully().begin().waitUntilDone();
  }
}
