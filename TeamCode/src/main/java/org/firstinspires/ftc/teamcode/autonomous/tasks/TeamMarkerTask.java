package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

public class TeamMarkerTask implements Task {

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();

    bot.intake.extendFully().begin().waitUntilDone();
    bot.intake.orientToTransfer();
    Bot.sleep(2000);
    bot.intake.orientToTransit();
    bot.intake.retractFully().begin().waitUntilDone();
  }
}
