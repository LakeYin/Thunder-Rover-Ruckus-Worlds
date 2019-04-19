package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.RunToPosition;

public class TeamMarkerTask implements Task {

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();

    bot.intake.orientToTransfer();
    RunToPosition intakeOut = bot.intake.extend(0.9).begin();
    bot.drivetrain.driveForwards(12);
    intakeOut.waitUntilDone();
    bot.intake.orientToTransit();
    bot.intake.runSweeperOut();
    Bot.sleep(2000);
    bot.intake.stopSweeper();
    bot.intake.orientToTransfer();
    Bot.sleep(500);
    bot.intake.retractFully().begin().waitUntilDone();
  }
}
