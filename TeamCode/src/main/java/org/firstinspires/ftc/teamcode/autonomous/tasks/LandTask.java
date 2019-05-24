package org.firstinspires.ftc.teamcode.autonomous.tasks;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.GOLD;
import static org.firstinspires.ftc.teamcode.autonomous.MineralDetector.Mineral.SILVER;

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
    if (AutonomousBot.centerMineral.orElse(SILVER) != GOLD)
      rotateToReadRightMineral();
  }

  private void rotateToReadRightMineral() {
    RotateByIMUTask.rotate(30);
    Bot.sleep(1000);
    AutonomousBot.rightMineral = SampleMineralTask.detector.rightmostRecognition();
    Log.d("Minerals", "Right: " + AutonomousBot.rightMineral);
    SampleMineralTask.detector.shutdown();
    RotateByIMUTask.rotate(-30);
  }

  private void exitHook() throws InterruptedException {
    bot.drivetrain.setStrafe(0, 1, 1);
    Thread.sleep(130);
    bot.drivetrain.stop();
  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(-1, 0, 1);
    Thread.sleep(550);
    bot.drivetrain.setStrafe(0, -1, 1);
    Thread.sleep(130);
    bot.drivetrain.stop();
  }

  private void lowerBot() {
    bot.hookLift.liftToHook().begin().waitUntilDone();
  }
}
