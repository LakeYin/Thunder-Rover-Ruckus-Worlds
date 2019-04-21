package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.RunToPosition;

public class TeamMarkerTask implements Task {

  private double extension;

  public TeamMarkerTask(double extension) {
    this.extension = extension;
  }

  public TeamMarkerTask() {
    this(1);
  }

  @Override
  public void run() {
    Bot bot = Bot.getInstance();

    bot.intake.orientToTransit();
    RunToPosition intakeOut = bot.intake.extend(0.5 * extension).begin();
    intakeOut.waitUntilDone();
    bot.intake.runSweeperOut();
    Bot.sleep(1500);
    bot.intake.stopSweeper();
    bot.intake.orientToTransit();
    bot.intake.retractFully().begin().waitUntilDone();
  }
}
