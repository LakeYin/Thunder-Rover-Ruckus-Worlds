package org.firstinspires.ftc.teamcode;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.Diagnosable;

public class DepositSystem extends Diagnosable {

  public static class ConfigSchema {
    public int fullyExtendedTicks;
    public double slideSpeed;

    public double orientatorRangeMin, orientatorRangeMax;
    public double orientatorFlatPos, orientatorPreparationPos,
        orientatorTransitPos, orientatorScorePos;
    public int scoreDelayMs;

    public int ticksWhenBelowFrame;
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

  public RunToPosition deliverToLander() {
    return new RunToPosition(slideMotor, schema.fullyExtendedTicks, schema.slideSpeed);
  }

  public void score() {
    orientator.setPosition(schema.orientatorScorePos);
    opMode.sleep(schema.scoreDelayMs);
  }

  public void adjust(double power) {
    slideMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    slideMotor.setPower(wouldPowerExceedLimit(power) ? 0 : power);
  }

  public boolean isRunningToPosition() {
    return slideMotor.getMode() == RunMode.RUN_TO_POSITION && slideMotor.isBusy();
  }

  public void retract() {
    if (slideMotorAboveFrame()) {
      orientator.setPosition(schema.orientatorTransitPos);
    }

    new RunToPosition(slideMotor, 0, schema.slideSpeed).begin();

    new Thread(() -> {
      while (slideMotorAboveFrame() && !Thread.interrupted());
      orientToLevel();
    }).start();
  }

  public void orientToTransit() {
    orientator.setPosition(schema.orientatorTransitPos);
  }

  public void orientToLevel() {
    orientator.setPosition(schema.orientatorFlatPos);
  }

  public void orientToScore() {
    orientator.setPosition(schema.orientatorScorePos);
  }

  private boolean slideMotorAboveFrame() {
    return slideMotor.getCurrentPosition() > schema.ticksWhenBelowFrame;
  }

  private boolean wouldPowerExceedLimit(double power) {
    return (power < 0 && slideMotor.getCurrentPosition() < 10) ||
            (power > 0 && slideMotor.getCurrentPosition() > schema.fullyExtendedTicks * .98);
  }

  private void loadSchema() {
    try {
      Configuration.fromPropertiesFile("depositSystem.properties").loadToSchema(schema);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void addData(Telemetry telemetry) {
    addMotorData(telemetry, "depositSlides", slideMotor);
    addServoData(telemetry, "sorterRotation", orientator);
  }
}
