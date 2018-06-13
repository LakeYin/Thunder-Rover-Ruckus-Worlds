package com.andoverrobotics.core.drivetrain;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.andoverrobotics.core.utilities.Converter;
import com.andoverrobotics.core.utilities.Coordinate;
import com.andoverrobotics.core.utilities.IMotor;
import com.andoverrobotics.core.utilities.MotorPair;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

public class MecanumDrive extends StrafingDriveTrain {

  private final IMotor leftDiagonal, rightDiagonal, leftSide, rightSide;

  private final int ticksPerInch;
  private final int ticksPer360;

  public MecanumDrive(IMotor leftDiagonal, IMotor rightDiagonal, IMotor leftSide, IMotor rightSide,
      OpMode opMode, int ticksPerInch, int ticksPer360) {

    super(opMode);

    this.leftDiagonal = leftDiagonal;
    this.rightDiagonal = rightDiagonal;
    this.leftSide = leftSide;
    this.rightSide = rightSide;

    this.ticksPerInch = ticksPerInch;
    this.ticksPer360 = ticksPer360;
  }

  public static MecanumDrive fromOctagonalMotors(DcMotor motorFL, DcMotor motorFR, DcMotor motorBL, DcMotor motorBR,
      OpMode opMode, int ticksPerInch, int ticksPer360) {
    // /-\
    // | |
    // \-/

    return new MecanumDrive(
        MotorPair.of(motorFR, motorBL),
        MotorPair.of(motorFL, motorBR),
        MotorPair.of(motorFL, motorBL),
        MotorPair.of(motorFR, motorBR), opMode, ticksPerInch, ticksPer360);
  }

  public static MecanumDrive fromCrossedMotors(DcMotor motorFL, DcMotor motorFR, DcMotor motorBL, DcMotor motorBR,
      OpMode opMode, int ticksPerInch, int ticksPer360) {
    // \-/
    // | |
    // /-\

    return new MecanumDrive(
        MotorPair.of(motorFL, motorBR),
        MotorPair.of(motorFR, motorBL),
        MotorPair.of(motorFL, motorBL),
        MotorPair.of(motorFR, motorBR), opMode, ticksPerInch, ticksPer360);
  }

  private void driveWithEncoder(Coordinate displacement, double power) {
    double clippedPower = Range.clip(power, -1, 1);

    if (displacement.getPolarDistance() < 1e-5) {
      return;
    }

    Coordinate diagonalOffsets = displacement.rotate(-45);
    double maxOffset = Math.max(diagonalOffsets.getX(), diagonalOffsets.getY());

    int leftOffset = (int) (diagonalOffsets.getY() * ticksPerInch),
        rightOffset = (int) (diagonalOffsets.getX() * ticksPerInch);

    double leftPower = clippedPower * (diagonalOffsets.getY() / maxOffset),
        rightPower = clippedPower * (diagonalOffsets.getX() / maxOffset);

    leftDiagonal.startRunToPosition(leftOffset, leftPower);
    rightDiagonal.startRunToPosition(rightOffset, rightPower);

    while (isBusy() && opModeIsActive()) {
    }

    stop();
    setMotorMode(RUN_USING_ENCODER);
  }

  @Override
  public void rotateClockwise(int degrees, double power) {
    rotateWithEncoder(-Converter.normalizedDegrees(degrees), -Math.abs(power));
  }

  @Override
  public void rotateCounterClockwise(int degrees, double power) {
    rotateWithEncoder(Converter.normalizedDegrees(degrees), Math.abs(power));
  }

  // Positive input means counter-clockwise
  private void rotateWithEncoder(int degrees, double power) {
    double clippedPower = Math.abs(Range.clip(power, -1, 1));
    double rotationTicks = degrees / 360.0 * ticksPer360;

    leftSide.startRunToPosition((int) -rotationTicks, -clippedPower);
    rightSide.startRunToPosition((int) rotationTicks, clippedPower);

    while (isBusy() && opModeIsActive()) {
    }

    stop();
    setMotorMode(RUN_USING_ENCODER);
  }

  @Override
  public void strafeInches(Coordinate inchOffset, double power) {
    driveWithEncoder(inchOffset, power);
  }

  // -- TeleOp methods --

  @Override
  public void setMovementPower(double power) {
    double clippedPower = Range.clip(power, -1, 1);

    setMotorMode(RUN_WITHOUT_ENCODER);

    leftSide.setPower(clippedPower);
    rightSide.setPower(clippedPower);
  }

  @Override
  public void setRotationPower(double power) { //clockwise if power is positive
    double clippedPower = Range.clip(power, -1, 1);

    setMotorMode(RUN_WITHOUT_ENCODER);

    leftSide.setPower(clippedPower);
    rightSide.setPower(-clippedPower);
  }

  @Override
  public void setStrafe(int polarDirection, double power) {
    double direction = Converter.degreesToRadians(polarDirection - 45);

    setMotorMode(RUN_WITHOUT_ENCODER);

    leftDiagonal.setPower(Math.sin(direction) * Math.abs(power));
    rightDiagonal.setPower(Math.cos(direction) * Math.abs(power));
  }

  @Override
  public void setMovementAndRotation(double movePower, double rotatePower) {
    setMotorMode(RUN_WITHOUT_ENCODER);

    double leftPower = movePower + rotatePower,
        rightPower = movePower - rotatePower;

    double maxPower = Math.max(Math.abs(leftPower), Math.abs(rightPower));
    if (maxPower > 1) {
      leftPower /= maxPower;
      rightPower /= maxPower;
    }

    leftSide.setPower(leftPower);
    rightSide.setPower(rightPower);
  }

  @Override
  protected IMotor[] getMotors() {
    return new IMotor[]{leftDiagonal, rightDiagonal, leftSide, rightSide};
  }
}