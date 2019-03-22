package org.firstinspires.ftc.teamcode;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
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

  public void extendFully() {
    extendFully(schema.runningSpeed);
  }
  public void extendFully(double speed) {
    runToPosition(schema.fullyExtendedTicks, speed);
  }

  public void retractFully() {
    retractFully(schema.runningSpeed);
  }
  public void retractFully(double speed) {
    runToPosition(0, speed);
  }

  public boolean isSlideRunning() {
    return slideMotor.isBusy();
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
    sweeper.setPower(0.85);
  }

  public void stopSweeper() {
    sweeper.setPower(0);
  }

  private boolean wouldPowerExceedBoundaries(double power) {
    return slideMotor.getCurrentPosition() <= 10 && power < 0 ||
        slideMotor.getCurrentPosition() >= schema.fullyExtendedTicks - 10 && power > 0;
  }

  private void runToPosition(int position, double speed) {
    slideMotor.setMode(RunMode.RUN_TO_POSITION);
    slideMotor.setTargetPosition(position);
    slideMotor.setPower(Math.abs(speed));
    while (slideMotor.isBusy() && opMode.opModeIsActive());
    slideMotor.setPower(0);
  }
}
