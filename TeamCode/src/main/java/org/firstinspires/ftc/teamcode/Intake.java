package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.Diagnosable;

public class Intake extends Diagnosable {

  public static class ConfigSchema {

    public int fullyExtendedTicks;
    public double runningSpeed;

    public double orientatorRangeMin, orientatorRangeMax;
    public double orientatorCollectionPos, orientatorTransitPos, orientatorTransferPos,
      orientatorKnockMineralPos;
  }

  private static final ExecutorService executor = Executors.newSingleThreadExecutor();

  public final DcMotor slideMotor;
  public final Servo orientator;
  public final CRServo sweeper;
  public ConfigSchema schema;

  public Intake(DcMotor slideMotor, Servo orientator,
      CRServo sweeper) {
    this.slideMotor = slideMotor;

    this.slideMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

    this.orientator = orientator;
    this.sweeper = sweeper;
    loadSchema();
    orientator.scaleRange(schema.orientatorRangeMin, schema.orientatorRangeMax);
    orientator.setPosition(schema.orientatorTransitPos);
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
        (int) (schema.fullyExtendedTicks * Math.min(Math.abs(amount), 1)), speed);
  }

  public RunToPosition retractFully() {
    return retractFully(schema.runningSpeed);
  }
  public RunToPosition retractFully(double speed) {
    return new RunToPosition(slideMotor, 0, speed * 0.4);
  }

  public boolean isSlideRunningToPosition() {
    return slideMotor.getMode() == RunMode.RUN_TO_POSITION && slideMotor.isBusy();
  }

  public void orientManually(float delta) {
    orientator.setPosition(Range.clip(orientator.getPosition(), 0, 1) + delta * 0.1);
  }

  public void orientToCollect() {
    incrementallyOrientTo(schema.orientatorCollectionPos);
  }

  public void orientToTransit() {
    incrementallyOrientTo(schema.orientatorTransitPos);
  }

  public void orientToTransfer() {
    incrementallyOrientTo(schema.orientatorTransferPos);
  }

  public void orientToKnockMineral() {
    incrementallyOrientTo(schema.orientatorKnockMineralPos);
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

  public void runSweeperOut() {
    sweeper.setPower(-0.8);
  }

  public void stopSweeper() {
    sweeper.setPower(0);
  }

  private boolean wouldPowerExceedBoundaries(double power) {
    return slideMotor.getCurrentPosition() <= 10 && power < 0 ||
        slideMotor.getCurrentPosition() >= schema.fullyExtendedTicks - 10 && power > 0;
  }

  private void incrementallyOrientTo(double position) {
    executor.submit(() -> {
      double pos = orientator.getPosition(),
          increment = Math.copySign(0.05, position - pos);
      while (Math.abs(pos - position) > 1e-2) {
        pos += Math.abs(increment) > Math.abs(position - pos) ? (position - pos) : increment;
        orientator.setPosition(pos);
        Log.d("intakeOrientatorPos", Double.toString(pos));
        try {
          Thread.sleep(20);
        } catch (InterruptedException e) {
          e.printStackTrace();
          break;
        }
      }
    });
  }

  public void addData(Telemetry telemetry) {
    addMotorData(telemetry, "intakeSlides", slideMotor);
    addServoData(telemetry, "bucketRotation", orientator);
    telemetry.addData("sweeper", "power=%.3f", sweeper.getPower());
  }
}
