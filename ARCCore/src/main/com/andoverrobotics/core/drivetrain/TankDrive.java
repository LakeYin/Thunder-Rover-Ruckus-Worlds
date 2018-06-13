package com.andoverrobotics.core.drivetrain;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.andoverrobotics.core.utilities.Converter;
import com.andoverrobotics.core.utilities.IMotor;
import com.andoverrobotics.core.utilities.MotorAdapter;
import com.andoverrobotics.core.utilities.MotorPair;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class TankDrive extends DriveTrain {

  private IMotor motorL;
  private IMotor motorR;
  private final int ticksPerInch;
  private final int ticksPer360;

  // Verify that the motor(s) on one side is reversed if the motors point in opposite directions!
  public TankDrive(IMotor motorL, IMotor motorR, OpMode opMode,
      int ticksPerInch, int ticksPer360) {
    super(opMode);

    this.motorL = motorL;
    this.motorR = motorR;
    this.ticksPerInch = ticksPerInch;
    this.ticksPer360 = ticksPer360;
  }

  public static TankDrive fromMotors(DcMotor left, DcMotor right, OpMode opMode,
      int ticksPerInch, int ticksPer360) {

    return new TankDrive(new MotorAdapter(left),
        new MotorAdapter(right), opMode, ticksPerInch, ticksPer360);
  }

  public static TankDrive fromMotors(DcMotor left1, DcMotor left2, DcMotor right1, DcMotor right2,
      OpMode opMode, int ticksPerInch, int ticksPer360) {

    return new TankDrive(
        MotorPair.of(left1, left2),
        MotorPair.of(right1, right2), opMode, ticksPerInch, ticksPer360);
  }


  @Override
  public void driveForwards(double distanceInInches, double power) {
    driveWithEncoder(Math.abs(distanceInInches), Math.abs(power));
  }

  private void driveWithEncoder(double displacementInInches, double power) {

    if (power == 0) {
      stop();
      return;
    }

    power = Range.clip(power, -1, 1);
    power = Math.abs(power);

    if (displacementInInches < 0) {
      power *= -1;
    }

    double robotTurn = displacementInInches * ticksPerInch;

    runWithEncoder((int) robotTurn, (int) robotTurn, power, power);
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

    runWithEncoder(
        (int) Math.round(leftDegrees / 360.0 * ticksPer360),
        (int) Math.round(rightDegrees / 360.0 * ticksPer360),
        leftPower, rightPower);
  }

  private void runWithEncoder(int leftTickOffset, int rightTickOffset,
      double leftPower, double rightPower) {

    // Fails unit tests
    /*Log.d("TankDrive Encoder",
        String.format("leftTickOffset=%d rightTickOffset=%d leftPower=%.3f rightPower=%.3f",
        leftTickOffset, rightTickOffset, leftPower, rightPower));*/

    motorL.startRunToPosition(leftTickOffset, leftPower);
    motorR.startRunToPosition(rightTickOffset, rightPower);

    while (isBusy() && opModeIsActive()) {
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
  public void setMovementAndRotation(double movePower, double rotatePower) {
    double leftPower = movePower + rotatePower,
        rightPower = movePower - rotatePower,
        maxAbsPower = Math.max(Math.abs(leftPower), Math.abs(rightPower));

    if (maxAbsPower > 1) {
      leftPower /= maxAbsPower;
      rightPower /= maxAbsPower;
    }

    motorL.setPower(leftPower);
    motorR.setPower(rightPower);
  }

  @Override
  protected IMotor[] getMotors() {
    return new IMotor[]{motorL, motorR};
  }

}
