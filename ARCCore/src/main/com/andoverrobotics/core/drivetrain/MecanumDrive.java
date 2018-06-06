package com.andoverrobotics.core.drivetrain;

import com.andoverrobotics.core.utilities.Converter;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

public class MecanumDrive extends StrafingDriveTrain {

  private DcMotor motorFL;
  private DcMotor motorFR;
  private DcMotor motorBL;
  private DcMotor motorBR;
  private final int ticksPerInch;
  private final int ticksPer360;

  public MecanumDrive(DcMotor motorL, DcMotor motorR, OpMode opMode,
                      int ticksPerInch, int ticksPer360) {
    super(opMode);

    this.motorFL = motorFL;
    this.motorFR = motorFR;
    this.motorBL = motorBL;
    this.motorBR = motorBR;
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

    motorFL.setTargetPosition((int) (robotTurn));
    motorFR.setTargetPosition((int) (robotTurn));
    motorBL.setTargetPosition((int) (robotTurn));
    motorBR.setTargetPosition((int) (robotTurn));

    motorFL.setPower(power);
    motorFR.setPower(power);
    motorBL.setPower(power);
    motorBR.setPower(power);

    while (motorFL.isBusy() && motorFR.isBusy() && motorBL.isBusy() && motorBR.isBusy() && opModeIsActive()) {
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

    motorFL.setTargetPosition((int) (leftDegrees / 360.0 * ticksPer360));
    motorFR.setTargetPosition((int) (rightDegrees / 360.0 * ticksPer360));
    motorBL.setTargetPosition((int) (leftDegrees / 360.0 * ticksPer360));
    motorBR.setTargetPosition((int) (rightDegrees / 360.0 * ticksPer360));

    motorFL.setPower(leftPower);
    motorFR.setPower(rightPower);
    motorBL.setPower(leftPower);
    motorBR.setPower(rightPower);

    while (motorFL.isBusy() && motorFR.isBusy() && motorBL.isBusy() && motorBR.isBusy() && opModeIsActive()) {
      reportMotorPositions();
    }

    stop();
    setMotorMode(RUN_USING_ENCODER);
  }

  public void strafeRight(double power, double distanceInInches)
  {

    // Set the encoder mode to 3 (STOP_AND_RESET_ENCODERS)
    setMotorMode(STOP_AND_RESET_ENCODER);

    // Sets the power range
    power = Range.clip(power, -1, 1);

    // Setting the target positions
    motorFL.setTargetPosition((int)(distanceInInches * -ticksPerInch));
    motorBL.setTargetPosition((int)(distanceInInches * ticksPerInch));
    motorFR.setTargetPosition((int)(distanceInInches * ticksPerInch));
    motorBR.setTargetPosition((int)(distanceInInches * -ticksPerInch));

    // Set encoder mode to RUN_TO_POSITION
    setMotorMode(RUN_TO_POSITION);

    motorFR.setPower(power);
    motorBL.setPower(power);
    motorFL.setPower(power);
    motorBR.setPower(power);

    // While loop for updating telemetry
    while(motorFL.isBusy() && motorFR.isBusy() && opModeIsActive()){

      // Updates the position of the motors
      double LPos = motorFL.getCurrentPosition();
      double RPos = motorFR.getCurrentPosition();

      while (motorFL.isBusy() && motorFR.isBusy() && motorBL.isBusy() && motorBR.isBusy() && opModeIsActive()) {
        reportMotorPositions();
      }

    }

    // Stops the motors
    stop();

    // Resets to run using encoders mode
    setMotorMode(RUN_USING_ENCODER);
  }

  public void strafeLeft(double power, double distanceInInches){
    strafeRight(power, -distanceInInches);
  }

  // -- TeleOp methods --

  @Override
  public void setMovementPower(double power) {
    setMotorMode(RUN_WITHOUT_ENCODER);

    motorFL.setPower(power);
    motorFR.setPower(power);
    motorBL.setPower(power);
    motorBR.setPower(power);
  }

  @Override
  public void setRotationPower(double power) { //clockwise if power is positive
    setMotorMode(RUN_WITHOUT_ENCODER);

    motorFL.setPower(power);
    motorFR.setPower(-power);
    motorBL.setPower(power);
    motorBR.setPower(-power);
  }

  @Override
  protected DcMotor[] getMotors() {
    return new DcMotor[]{motorFL, motorBR, motorBL, motorBR};
  }

  private void reportMotorPositions() {
    double FLPos = motorFL.getCurrentPosition();
    double FRPos = motorFR.getCurrentPosition();
    double BLPos = motorBL.getCurrentPosition();
    double BRPos = motorBR.getCurrentPosition();

    opMode.telemetry.addData("motorFL Pos:", FLPos);
    opMode.telemetry.addData("motorFR Pos:", FRPos);
    opMode.telemetry.addData("motorBL Pos:", BLPos);
    opMode.telemetry.addData("motorBR Pos:", BRPos);

    opMode.telemetry.update();
  }
}
