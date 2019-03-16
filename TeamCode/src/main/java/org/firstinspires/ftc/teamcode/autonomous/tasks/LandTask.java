package org.firstinspires.ftc.teamcode.autonomous.tasks;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import java.io.IOException;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.autonomous.AutonomousBot;

public class LandTask implements Task {

  public static class ConfigSchema {
    public double strafePower;
    public int landWaitMs;
    public int landTicks;
    public int exitHookWaitMs;
  }

  private boolean useEncoders;
  private ConfigSchema schema;

  public LandTask(boolean useEncoders) {
    this.useEncoders = useEncoders;
    populateSchema();
    bot.hookArm.liftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    bot.hookArm.setLiftPower(0);
  }

  private Bot bot = Bot.getInstance();

  @Override
  public void run() throws InterruptedException {
    raiseRightArm();
    lowerBot();
    startLowerRightArm();
    exitHook();
    startLowerGrabber();
    strafeBot();
  }

  private void startLowerRightArm() {
    bot.rightArm.startRunningLiftToPosition(-10, 0.08);
  }

  private void raiseRightArm() {
    bot.rightArm.startRunningLiftToPosition(-240, 0.1);
    while (bot.rightArm.liftMotor.getCurrentPosition() > -150 && AutonomousBot.isActive());
  }

  private void exitHook() throws InterruptedException {
    bot.drivetrain.setStrafe(-1, 0, schema.strafePower);
    Thread.sleep(schema.exitHookWaitMs);
    bot.drivetrain.stop();
  }

  private void strafeBot() throws InterruptedException {
    bot.drivetrain.setStrafe(0, -1, schema.strafePower);
    Thread.sleep(250);
    bot.drivetrain.setStrafe(1, 0, schema.strafePower);
    Thread.sleep(500);
    bot.drivetrain.stop();
  }

  private void lowerBot() throws InterruptedException {
    if (useEncoders) {
      DcMotor liftMotor = bot.hookArm.liftMotor;

      liftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
      while (liftMotor.getCurrentPosition() != 0 && AutonomousBot.isActive());
      liftMotor.setMode(RunMode.RUN_TO_POSITION);
      liftMotor.setTargetPosition(schema.landTicks);
      liftMotor.setPower(1);
      while (liftMotor.getCurrentPosition() - schema.landTicks > 10 && AutonomousBot.isActive());
      liftMotor.setPower(0);
      liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);

    } else {
      bot.hookArm.setLiftPower(-1.0);
      Thread.sleep(schema.landWaitMs);
      bot.hookArm.setLiftPower(0);
    }
  }

  private void startLowerGrabber() {
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(2000);
        bot.hookArm.setLiftPower(1.0);
        Thread.sleep(schema.landWaitMs);
        bot.hookArm.setLiftPower(0);

        DcMotor liftMotor = bot.hookArm.liftMotor;

        liftMotor.setMode(RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(0);
        liftMotor.setPower(0.7);
        while (liftMotor.isBusy() && AutonomousBot.isActive());
        liftMotor.setPower(0);
        liftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);

      } catch (Exception e) {}
    });

    thread.start();
  }

  private void populateSchema() {
    try {
      schema = new ConfigSchema();
      Configuration.fromPropertiesFile("land.properties").loadToSchema(schema);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
