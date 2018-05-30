package com.andoverrobotics.core.drivetrain;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.andoverrobotics.core.utilities.Converter;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class TankDrive extends DriveTrain {

  private DcMotor motorL;
  private DcMotor motorR;
  private final int ticksPerInch;
  private final int ticksPer360;

  public TankDrive(DcMotor motorL, DcMotor motorR, OpMode opMode,
      int ticksPerInch, int ticksPer360) {
    super(opMode);

    this.motorL = motorL;
    this.motorR = motorR;
    this.ticksPerInch = ticksPerInch;
    this.ticksPer360 = ticksPer360;
  }

  @Override
  public void driveForwards(double distanceInInches, double power) {
    driveWithEncoder(Math.abs(distanceInInches), Math.abs(power));
  }

  private void driveWithEncoder(double displacementInInches, double power) {
    power = Range.clip(power, -1, 1);
    power = Math.abs(power);

    if (displacementInInches < 0) {
      power *= -1;
    }

    setMotorMode(STOP_AND_RESET_ENCODER);
    setMotorMode(RUN_TO_POSITION);

    double robotTurn = displacementInInches * ticksPerInch;

    motorL.setTargetPosition((int) (robotTurn));
    motorR.setTargetPosition((int) (robotTurn));

    motorL.setPower(power);
    motorR.setPower(power);

    while (motorL.isBusy() && motorR.isBusy() && opModeIsActive()) {
      reportMotorPositions();
    }

    stop();
    setMotorMode(RUN_USING_ENCODER);
  }

  @Override
  public void driveBackwards(double distanceInInches, double power) {
    driveWithEncoder(-Math.abs(distanceInInches), -Math.abs(power));
  }

  @Override
  public void rotateClockwise(int degrees, double power) {
    power = Range.clip(power, -1, 1);
    power = Math.abs(power);
    degrees = Converter.normalizedDegrees(degrees);

    rotateWithEncoder(degrees, -degrees, power, -power);
  }

  @Override
  public void rotateCounterClockwise(int degrees, double power) {
    power = Range.clip(power, -1, 1);
    power = Math.abs(power);
    degrees = Converter.normalizedDegrees(degrees);

    rotateWithEncoder(-degrees, degrees, -power, power);
  }

  private void rotateWithEncoder(int leftDegrees, int rightDegrees,
      double leftPower, double rightPower) {

    setMotorMode(STOP_AND_RESET_ENCODER);
    setMotorMode(RUN_TO_POSITION);

    motorL.setTargetPosition((int) (leftDegrees / 360.0 * ticksPer360));
    motorR.setTargetPosition((int) (rightDegrees / 360.0 * ticksPer360));

    motorL.setPower(leftPower);
    motorR.setPower(rightPower);

    while (motorL.isBusy() && motorR.isBusy() && opModeIsActive()) {
      reportMotorPositions();
    }

    stop();
    setMotorMode(RUN_USING_ENCODER);
  }

  // -- TeleOp methods --

  @Override
  public void setMovementPower(double power) {
    setMotorMode(RUN_WITHOUT_ENCODER);

    motorL.setPower(power);
    motorR.setPower(power);
  }

  @Override
  public void setRotationPower(double power) { //clockwise if power is positive
    setMotorMode(RUN_WITHOUT_ENCODER);

    motorL.setPower(power);
    motorR.setPower(-power);
  }

  @Override
  protected DcMotor[] getMotors() {
    return new DcMotor[]{motorL, motorR};
  }

  private void reportMotorPositions() {
    double LPos = motorL.getCurrentPosition();
    double RPos = motorR.getCurrentPosition();

    opMode.telemetry.addData("motorL Pos:", LPos);
    opMode.telemetry.addData("motorR Pos:", RPos);

    opMode.telemetry.update();
  }
}
