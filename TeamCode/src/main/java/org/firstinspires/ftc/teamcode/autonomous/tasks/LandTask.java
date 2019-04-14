package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import org.firstinspires.ftc.teamcode.Bot;

public class LandTask implements Task {

  public LandTask() {
    bot.hookLift.liftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
  }

  private Bot bot = Bot.getInstance();

  @Override
  public void run() throws InterruptedException {
    lowerBot();
    exitHook();
    strafeBot();
    startLoweringHook();
  }

  private void exitHook() throws InterruptedException {
    bot.drivetrain.setStrafe(0, 1, 0.5);
    Thread.sleep(200);
    bot.drivetrain.stop();
  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(-1, 0, 0.5);
    Thread.sleep(700);
    bot.drivetrain.setStrafe(0, -1, 0.5);
    Thread.sleep(280);
    bot.drivetrain.stop();
  }

  private void lowerBot() {
    bot.hookLift.liftToHook().begin().waitUntilDone();
  }

  private void startLoweringHook() {
    bot.hookLift.lowerToBottom().begin();
  }
}
