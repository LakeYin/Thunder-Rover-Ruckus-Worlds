package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.firstinspires.ftc.teamcode.Bot;

public class LandTask implements Task {
  public static final double POWER = 0.5;
  public static final int LAND_WAIT = 2000,
    LAND_ENCODER_TICKS = -6000;

  private boolean useEncoders;

  public LandTask(boolean useEncoders) {
    this.useEncoders = useEncoders;
  }

  private Bot bot = Bot.getInstance();
  private Thread positionReporter = new Thread(() -> {
    while (!Thread.interrupted()) {
      bot.telemetry
          .addData("Hook position", bot.hookArm.liftMotor.getCurrentPosition());
      bot.telemetry.update();
    }
  });

  @Override
  public void run() throws InterruptedException {
    positionReporter.start();
    lowerBot();
    exitHook();
    startLowerGrabber();
    strafeBot();
    positionReporter.interrupt();
  }

  private void exitHook() throws InterruptedException {
    bot.drivetrain.setStrafe(-1, 0, POWER);
    Thread.sleep(500);
    bot.drivetrain.stop();
  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(0, -1, POWER);
    Thread.sleep(250);
    bot.drivetrain.setStrafe(1, 0, POWER);
    Thread.sleep(500);
    bot.drivetrain.stop();
  }

  private void lowerBot() throws InterruptedException {
    if (useEncoders) {
      DcMotor liftMotor = bot.hookArm.liftMotor;

      liftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
      liftMotor.setMode(RunMode.RUN_TO_POSITION);
      liftMotor.setTargetPosition(LAND_ENCODER_TICKS);
      liftMotor.setPower(1);
      while (liftMotor.getCurrentPosition() - LAND_ENCODER_TICKS > 10 && !Thread.interrupted());
      liftMotor.setPower(0);
      liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);

    } else {
      bot.hookArm.setLiftPower(-1.0);
      Thread.sleep(LAND_WAIT);
      bot.hookArm.setLiftPower(0);
    }
  }

  private void startLowerGrabber() {
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(2000);
        bot.hookArm.setLiftPower(1.0);
        Thread.sleep(LAND_WAIT);
        bot.hookArm.setLiftPower(0);

        DcMotor liftMotor = bot.hookArm.liftMotor;

        liftMotor.setMode(RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(0);
        liftMotor.setPower(1);
        while (liftMotor.getCurrentPosition() < -10 && !Thread.interrupted());
        liftMotor.setPower(0);
        liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);

      } catch (Exception e) {}
    });

    thread.start();
  }
}
