package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import org.firstinspires.ftc.teamcode.Bot;

public class LandTask implements Task {

  private static final double kStrafePower = 0.2;

  public LandTask() {
    bot.hookLift.liftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
  }

  private Bot bot = Bot.getInstance();

  @Override
  public void run() throws InterruptedException {
    lowerBot();
    exitHook();
    strafeBot();
  }

  private void exitHook() throws InterruptedException {
    bot.drivetrain.setStrafe(0, -1, kStrafePower);
    Thread.sleep(150);
    bot.drivetrain.stop();
  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(0, -1, kStrafePower);
    Thread.sleep(200);
    bot.drivetrain.setStrafe(1, 0, kStrafePower);
    Thread.sleep(400);
    bot.drivetrain.stop();
  }

  private void lowerBot() throws InterruptedException {
    bot.hookLift.liftToHook();
  }
}
