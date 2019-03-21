package org.firstinspires.ftc.teamcode;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;
import org.firstinspires.ftc.teamcode.teleop.TeleOpBot;

public class DepositSystem {
  public static class ConfigSchema {
    public int fullyExtendedTicks;
    public double slideSpeed;

    public double orientatorRangeMin, orientatorRangeMax;
    public double orientatorFlatPos, orientatorPreparationPos,
        orientatorTransitPos, orientatorScorePos;
    public int scoreDelayMs;
  }

  public final DcMotor slideMotor;
  public final Servo orientator;
  private Bot bot;
  public final ConfigSchema schema = new ConfigSchema();

  public DepositSystem(DcMotor slideMotor, Servo orientator, Bot bot) {
    this.slideMotor = slideMotor;
    this.orientator = orientator;
    this.bot = bot;
    loadSchema();
    orientator.scaleRange(schema.orientatorRangeMin, schema.orientatorRangeMax);
  }

  public void prepareToDeposit() {
    orientator.setPosition(schema.orientatorPreparationPos);
  }

  public void deposit() {
    slideMotor.setMode(RunMode.RUN_TO_POSITION);
    slideMotor.setTargetPosition(schema.fullyExtendedTicks);
    slideMotor.setPower(Math.abs(schema.slideSpeed));

    while (slideMotor.isBusy() && TeleOpBot.isActive());

    slideMotor.setPower(0);

    orientator.setPosition(schema.orientatorScorePos);
    bot.opMode.sleep(schema.scoreDelayMs);
  }

  public void retract() {
    if (Math.abs(slideMotor.getCurrentPosition()) > Math.abs(schema.fullyExtendedTicks * 0.2)) {
      orientator.setPosition(schema.orientatorTransitPos);
      bot.opMode.sleep(400);
    }

    slideMotor.setMode(RunMode.RUN_TO_POSITION);
    slideMotor.setTargetPosition(0);
    slideMotor.setPower(Math.abs(schema.slideSpeed));

    while (slideMotorAboveFrame() && TeleOpBot.isActive());
    orientator.setPosition(schema.orientatorFlatPos);
    while (slideMotor.isBusy() && TeleOpBot.isActive());

    slideMotor.setPower(0);
  }

  private boolean slideMotorAboveFrame() {
    return Math.abs(slideMotor.getTargetPosition() - slideMotor.getCurrentPosition()) > Math.abs(schema.fullyExtendedTicks * 0.2);
  }

  private void loadSchema() {
    try {
      Configuration.fromPropertiesFile("depositSystem.properties").loadToSchema(schema);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
