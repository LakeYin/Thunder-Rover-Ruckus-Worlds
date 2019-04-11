package org.firstinspires.ftc.teamcode;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;

public class Intake {
  public static class ConfigSchema {

    public int fullyExtendedTicks;
    public double runningSpeed;

    public double orientatorRangeMin, orientatorRangeMax;
    public double orientatorCollectionPos, orientatorTransitPos, orientatorTransferPos, orientatorClearBotPos;
  }

  public final DcMotor slideMotor;
  public final Servo orientator;
  public final CRServo sweeper;
  private LinearOpMode opMode;
  public ConfigSchema schema;

  public Intake(DcMotor slideMotor, Servo orientator,
      CRServo sweeper, LinearOpMode opMode) {
    this.slideMotor = slideMotor;

    this.slideMotor.setDirection(Direction.REVERSE);
    this.slideMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

    this.orientator = orientator;
    this.sweeper = sweeper;
    this.opMode = opMode;
    loadSchema();
    orientator.scaleRange(schema.orientatorRangeMin, schema.orientatorRangeMax);
  }

  private void loadSchema() {
    try {
      schema = new ConfigSchema();
      Configuration.fromPropertiesFile("intake.properties").loadToSchema(schema);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public RunToPosition extendFully() {
    return extendFully(schema.runningSpeed);
  }
  public RunToPosition extendFully(double speed) {
    return extend(1, speed);
  }

  public RunToPosition extend(double amount) {
    return extend(amount, schema.runningSpeed);
  }
  public RunToPosition extend(double amount, double speed) {
    return new RunToPosition(slideMotor,
        (int) (schema.fullyExtendedTicks * Math.max(Math.abs(amount), 1)), speed);
  }

  public RunToPosition retractFully() {
    return retractFully(schema.runningSpeed);
  }
  public RunToPosition retractFully(double speed) {
    return new RunToPosition(slideMotor, 0, speed * 0.4);
  }

  public boolean isSlideRunningToPosition() {
    return slideMotor.isBusy();
  }

  public void orientManually(float delta) {
    orientator.setPosition(orientator.getPosition() + delta * 0.014);
  }

  public void orientToCollect() {
    orientator.setPosition(schema.orientatorCollectionPos);
  }

  public void orientToTransit() {
    orientator.setPosition(schema.orientatorTransitPos);
  }

  public void orientToTransfer() {
    orientator.setPosition(schema.orientatorTransferPos);
  }

  public void controlSlidesManually(double power) {
    slideMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);

    if (wouldPowerExceedBoundaries(power))
      slideMotor.setPower(0);
    else
      slideMotor.setPower(power);
  }

  public void runSweeperIn() {
    sweeper.setPower(0.8);
  }

  public void stopSweeper() {
    sweeper.setPower(0);
  }

  private boolean wouldPowerExceedBoundaries(double power) {
    return slideMotor.getCurrentPosition() <= 10 && power < 0 ||
        slideMotor.getCurrentPosition() >= schema.fullyExtendedTicks - 10 && power > 0;
  }
}
