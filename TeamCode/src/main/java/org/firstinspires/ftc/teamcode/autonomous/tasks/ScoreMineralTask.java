package org.firstinspires.ftc.teamcode.autonomous.tasks;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

public class ScoreMineralTask implements Task {

  public static final double kRotationSpeed = 0.2;

  @Override
  public void run() throws InterruptedException {
    Bot bot = Bot.getInstance();

    while (AutonomousBot.secondsRemaining() > 4) {
      new Thread(bot.deposit::retract).start();
      bot.intake.extend(0.4, 0.7);
      bot.intake.orientToCollect();
      bot.intake.extend(0.6, 0.3);
      bot.intake.orientToTransit();
      Bot.sleep(500);
      bot.intake.retractFully();
      bot.intake.orientToTransfer();
      Bot.sleep(600);
      bot.intake.orientToTransit();
      bot.deposit.prepareToDeposit();
      bot.deposit.deposit();


      if (AutonomousBot.secondsRemaining() < 4)
        break;


      // score
      AutonomousBot.sleep(1000);
    }

    bot.intake.orientToTransit();
    bot.intake.extendFully();
  }
}
