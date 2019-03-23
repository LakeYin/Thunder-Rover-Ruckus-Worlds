package org.firstinspires.ftc.teamcode;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
  private LinearOpMode opMode;
  public final ConfigSchema schema = new ConfigSchema();

  public DepositSystem(DcMotor slideMotor, Servo orientator, LinearOpMode opMode) {
    this.slideMotor = slideMotor;
    this.orientator = orientator;
    this.opMode = opMode;
    loadSchema();
    orientator.scaleRange(schema.orientatorRangeMin, schema.orientatorRangeMax);
  }

  public void prepareToDeposit() {
    orientator.setPosition(schema.orientatorPreparationPos);
    opMode.sleep(300);
  }

  public void deposit() {
    slideMotor.setMode(RunMode.RUN_TO_POSITION);
    slideMotor.setTargetPosition(schema.fullyExtendedTicks);
    slideMotor.setPower(Math.abs(schema.slideSpeed));

    while (slideMotor.isBusy() && opMode.opModeIsActive());

    slideMotor.setPower(0);

    orientator.setPosition(schema.orientatorScorePos);
    opMode.sleep(schema.scoreDelayMs);
  }

  public void retract() {
    if (Math.abs(slideMotor.getCurrentPosition()) > Math.abs(schema.fullyExtendedTicks * 0.2)) {
      orientator.setPosition(schema.orientatorTransitPos);
      opMode.sleep(100);
    }

    slideMotor.setMode(RunMode.RUN_TO_POSITION);
    slideMotor.setTargetPosition(0);
    slideMotor.setPower(Math.abs(schema.slideSpeed));

    while (slideMotorAboveFrame() && opMode.opModeIsActive());
    orientator.setPosition(schema.orientatorFlatPos);
    while (slideMotor.isBusy() && opMode.opModeIsActive());

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