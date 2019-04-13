package org.firstinspires.ftc.teamcode;

import com.andoverrobotics.core.config.Configuration;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.Servo;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.Diagnosable;
import org.firstinspires.ftc.teamcode.teleop.TeleOpBot;

public class DepositSystem extends Diagnosable {

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

  public RunToPosition deliverToLander() {
    return new RunToPosition(slideMotor, schema.fullyExtendedTicks, schema.slideSpeed);
  }

  public void score() {
    orientator.setPosition(schema.orientatorScorePos);
    opMode.sleep(schema.scoreDelayMs);
  }

  public void adjust(double power) {
    slideMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    slideMotor.setPower(power);
  }

  public boolean isRunningToPosition() {
    return slideMotor.isBusy();
  }

  public Thread retract() {
    if (Math.abs(slideMotor.getCurrentPosition()) > Math.abs(schema.fullyExtendedTicks * 0.2)) {
      orientator.setPosition(schema.orientatorTransitPos);
      opMode.sleep(200);
    }

    slideMotor.setMode(RunMode.RUN_TO_POSITION);
    slideMotor.setTargetPosition(0);
    slideMotor.setPower(Math.abs(schema.slideSpeed));

    Thread thread = new Thread(() -> {
      while (slideMotorAboveFrame() && opMode.opModeIsActive())
        ;
      orientator.setPosition(schema.orientatorFlatPos);
    });
    thread.start();
    return thread;
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

  public void addData(Telemetry telemetry) {
    addMotorData(telemetry, "depositSlides", slideMotor);
    addServoData(telemetry, "sorterRotation", orientator);
  }
}
