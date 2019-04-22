package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;

public class ParkTask implements Task {
  @Override
  public void run() {
    Bot bot = Bot.getInstance();

    bot.intake.orientToTransit();
    bot.intake.extend(0.8);
  }
}
