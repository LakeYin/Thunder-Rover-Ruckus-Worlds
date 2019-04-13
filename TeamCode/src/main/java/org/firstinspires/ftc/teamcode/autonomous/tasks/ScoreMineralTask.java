package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

public class ScoreMineralTask implements Task {

  public static final double kRotationSpeed = 0.2;

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();

    while (AutonomousBot.secondsRemaining() > 4) {
      bot.deposit.retract();
      bot.intake.extend(0.4, 0.7).begin().waitUntilDone();
      bot.intake.orientToCollect();
      bot.intake.extend(0.6, 0.3).begin().waitUntilDone();
      bot.intake.orientToTransit();
      Bot.sleep(500);
      bot.intake.retractFully().begin().waitUntilDone();
      bot.intake.orientToTransfer();
      Bot.sleep(1000);
      bot.intake.orientToTransit();
      bot.deposit.prepareToDeposit();
      bot.deposit.deliverToLander().begin().waitUntilDone();
      bot.deposit.score();


      if (AutonomousBot.secondsRemaining() < 4)
        break;


      // score
      AutonomousBot.sleep(1000);
    }

    bot.intake.orientToTransit();
    bot.intake.extendFully();
  }
}
