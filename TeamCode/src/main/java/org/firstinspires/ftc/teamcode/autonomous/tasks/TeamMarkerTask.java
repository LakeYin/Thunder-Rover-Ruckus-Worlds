package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.RunToPosition;

public class TeamMarkerTask implements Task {

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();

    bot.intake.orientToTransit();
    RunToPosition intakeOut = bot.intake.extend(0.9).begin();
    bot.drivetrain.driveForwards(6);
    intakeOut.waitUntilDone();
    bot.intake.runSweeperOut();
    Bot.sleep(1500);
    bot.intake.stopSweeper();
    bot.intake.orientToTransit();
    bot.intake.retractFully().begin().waitUntilDone();
  }
}
