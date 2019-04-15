package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

public class TeamMarkerTask implements Task {

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();

    bot.intake.extend(0.9).begin().waitUntilDone();
    bot.intake.orientToTransit();
    Bot.sleep(400);
    bot.intake.orientToTransfer();
    bot.intake.runSweeperIn();
    Bot.sleep(1000);
    bot.intake.stopSweeper();
    bot.intake.retractFully().begin().waitUntilDone();
  }
}
