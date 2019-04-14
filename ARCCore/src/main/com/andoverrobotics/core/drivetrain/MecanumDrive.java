package com.andoverrobotics.core.drivetrain;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.andoverrobotics.core.utilities.Converter;
import com.andoverrobotics.core.utilities.Coordinate;
import com.andoverrobotics.core.utilities.IMotor;
import com.andoverrobotics.core.utilities.MotorAdapter;
import com.andoverrobotics.core.utilities.MotorPair;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Implements the {@link StrafingDriveTrain} for a Mecanum drivetrain. <p> See {@link
 * #fromOctagonalMotors(DcMotor, DcMotor, DcMotor, DcMotor, OpMode, int, int)} and {@link
 * #fromCrossedMotors(DcMotor, DcMotor, DcMotor, DcMotor, OpMode, int, int)} for instructions about
 * easier construction.
 */
public class MecanumDrive extends StrafingDriveTrain {

  private final MotorAdapter motorFL, motorFR, motorBL, motorBR;
  private final int ticksPerInch, ticksPer360;
  private final MotorAdapter[] leftDiagonal, rightDiagonal;

  /**
   * Constructs a new <code>MecanumDrive</code> instance with the given {@link IMotor}s and encoder
   * parameters.
   *
   * @param motorFL The {@link MotorAdapter} that represents front left motor
   * @param motorFR The {@link MotorAdapter} that represents the front right motor
   * @param motorBL The {@link MotorAdapter} that represents the back left motor
   * @param motorBR The {@link MotorAdapter} that represents the back right motor
   * @param leftDiagonal The array of DcMotors that, when given positive power, causes the robot to
   * move toward the front-left diagonal
   * @param rightDiagonal The array of DcMotors that, when given positive power, causes the robot to
   * move toward the front-right diagonal
   * @param opMode The main {@link OpMode}
   * @param ticksPerInch The number of encoder ticks required to cause a diagonal displacement of 1
   * inch for the robot
   * @param ticksPer360 The number of encoder ticks required to cause a full rotation for the robot,
   * when this amount is applied to the left and right sides in opposite directions
   */
  public MecanumDrive(MotorAdapter motorFL, MotorAdapter motorFR, MotorAdapter motorBL,
                      MotorAdapter motorBR,
                      MotorAdapter[] leftDiagonal, MotorAdapter[] rightDiagonal,
                      OpMode opMode, int ticksPerInch, int ticksPer360) {

    super(opMode);

    this.motorFL = motorFL;
    this.motorFR = motorFR;
    this.motorBL = motorBL;
    this.motorBR = motorBR;
    this.leftDiagonal = leftDiagonal;
    this.rightDiagonal = rightDiagonal;

    this.ticksPerInch = ticksPerInch;
    this.ticksPer360 = ticksPer360;
  }

  /**
   * Constructs a new MecanumDrive instance that uses the given physical motors, which are arranged
   * in an octagonal manner. <p> <h2>The Octagonal Configuration</h2> In the octagonal
   * configuration, the front left and the back right motors cause the robot to move forward and to
   * the right (diagonally), and the other two motors cause the robot to move forward and to the
   * left.
   *
   * It is named to be "octagonal" because of its diagram:
   * <pre>
   *   /-\
   *   | |
   *   \-/
   * </pre>
   *
   * @param motorFL The motor located at the front-left of the robot
   * @param motorFR The motor located at the front-right of the robot
   * @param motorBL The motor located at the rear-left of the robot
   * @param motorBR The motor located at the rear-right of the robot
   * @param opMode The main {@link OpMode}
   * @param ticksPerInch The number of encoder ticks required to cause a diagonal displacement of 1
   * inch for the robot
   * @param ticksPer360 The number of encoder ticks required to cause a full rotation for the robot,
   * when this amount is applied to the left and right sides in opposite directions
   * @return The new MecanumDrive instance
   */
  public static MecanumDrive fromOctagonalMotors(DcMotor motorFL, DcMotor motorFR, DcMotor motorBL,
                                                 DcMotor motorBR,
                                                 OpMode opMode, int ticksPerInch, int ticksPer360) {
    // /-\
    // | |
    // \-/

    MotorAdapter fl = new MotorAdapter(motorFL), fr = new MotorAdapter(
            motorFR), bl = new MotorAdapter(motorBL), br = new MotorAdapter(motorBR);

    return new MecanumDrive(fl,
            fr,
            bl,
            br,
            new MotorAdapter[]{
                    fr,
                    bl
            },
            new MotorAdapter[]{
                    fl,
                    br
            }, opMode, ticksPerInch, ticksPer360);
  }

  /**
   * Constructs a new MecanumDrive instance that uses the given physical motors, which are arranged
   * in a a crossed manner. <p> <b>The Crossed Configuration</b> In the crossed configuration, the
   * front left and the back right motors cause the robot to move forward and to the left
   * (diagonally), and the other two motors cause the robot to move forward and to the right.
   *
   * It is named to be "crossed" because of its diagram:
   * <pre>
   *   \-/
   *   | |
   *   /-\
   * </pre>
   *
   * @param motorFL The motor located at the front-left of the robot
   * @param motorFR The motor located at the front-right of the robot
   * @param motorBL The motor located at the rear-left of the robot
   * @param motorBR The motor located at the rear-right of the robot
   * @param opMode The main {@link OpMode}
   * @param ticksPerInch The number of encoder ticks required to cause a diagonal displacement of 1
   * inch for the robot
   * @param ticksPer360 The number of encoder ticks required to cause a full rotation for the robot,
   * when this amount is applied to the left and right sides in opposite directions
   * @return The new MecanumDrive instance
   */
  public static MecanumDrive fromCrossedMotors(DcMotor motorFL, DcMotor motorFR, DcMotor motorBL,
                                               DcMotor motorBR,
                                               OpMode opMode, int ticksPerInch, int ticksPer360) {
    // \-/
    // | |
    // /-\

    MotorAdapter fl = new MotorAdapter(motorFL), fr = new MotorAdapter(
            motorFR), bl = new MotorAdapter(motorBL), br = new MotorAdapter(motorBR);

    return new MecanumDrive(fl,
            fr,
            bl,
            br,
            new MotorAdapter[]{
                    fl,
                    br
            },
            new MotorAdapter[]{
                    fr,
                    bl
            }, opMode, ticksPerInch, ticksPer360);
  }

  // Rotates the given displacement by 45deg clockwise, assigns its components to the diagonals as
  // a tick offset (setTargetPosition), then scales its components down such that the greatest
  // component is equal to the power given, followed by assigning these components to the diagonals
  // as power.
  private void driveWithEncoder(Coordinate displacement, double power) {
    double clippedPower = Range.clip(power, -1, 1);

    if (displacement.getPolarDistance() < 1e-5) {
      return;
    }

    Coordinate diagonalOffsets = displacement.rotate(-45);
    double maxOffset = Math.max(diagonalOffsets.getX(), diagonalOffsets.getY());

    int leftOffset = (int) (diagonalOffsets.getY() * ticksPerInch),
            rightOffset = (int) (diagonalOffsets.getX() * ticksPerInch);

    double leftPower = Math.abs(clippedPower * (diagonalOffsets.getY() / maxOffset)),
            rightPower = Math.abs(clippedPower * (diagonalOffsets.getX() / maxOffset));

    for (MotorAdapter motor : leftDiagonal) {
      motor.startRunToPosition(leftOffset, leftPower);
    }
    for (MotorAdapter motor : rightDiagonal) {
      motor.startRunToPosition(rightOffset, rightPower);
    }

    while (opModeIsActive() && meanVarianceFromTargetPosition() > 20) {
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
  private void rotateWithEncoder(double degrees, double power) {
    double clippedPower = Math.abs(Range.clip(power, -1, 1));
    double rotationTicks = degrees / 360.0 * ticksPer360;

    for (MotorAdapter motor : leftSide()) {
      motor.startRunToPosition((int) -rotationTicks, clippedPower);
    }
    for (MotorAdapter motor : rightSide()) {
      motor.startRunToPosition((int) rotationTicks, clippedPower);
    }

    while (opModeIsActive() && meanVarianceFromTargetPosition() > 20) {
    }

    stop();
    setMotorMode(RUN_USING_ENCODER);
  }

  private MotorAdapter[] leftSide() {
    return new MotorAdapter[]{
            motorFL,
            motorBL
    };
  }

  private MotorAdapter[] rightSide() {
    return new MotorAdapter[]{
            motorFR,
            motorBR
    };
  }

  private MotorAdapter[] allMotors() {
    return new MotorAdapter[]{
            motorFL,
            motorFR,
            motorBL,
            motorBR
    };
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

    for (MotorAdapter motor : allMotors()) {
      motor.setPower(clippedPower);
    }
  }

  private int[] memoPosition = {0, 0, 0, 0};

  public void memorizeCurrentPosition() {
    MotorAdapter[] motors = allMotors();

    for (int i = 0; i < 4; i++) {
      memoPosition[i] = motors[i].getMotor().getCurrentPosition();
    }
  }

  public void startGoingToMemorizedPosition(double power) {
    MotorAdapter[] motors = allMotors();

    for (int i = 0; i < 4; i++) {
      DcMotor motor = motors[i].getMotor();
      motor.setMode(RUN_TO_POSITION);
      motor.setTargetPosition(memoPosition[i]);
      motor.setPower(Math.abs(power));
    }

    while (!isRunningToPosition());
  }

  public int[] getMemorizedPosition() {
    return Arrays.copyOf(memoPosition, memoPosition.length);
  }

  @Override
  public void setRotationPower(double power) { //clockwise if power is positive
    double clippedPower = Range.clip(power, -1, 1);

    setMotorMode(RUN_WITHOUT_ENCODER);

    for (MotorAdapter motor : leftSide()) {
      motor.setPower(clippedPower);
    }
    for (MotorAdapter motor : rightSide()) {
      motor.setPower(-clippedPower);
    }
  }

  @Override
  public void setStrafe(Coordinate offset, double unscaledPower) {
    double direction = Converter.degreesToRadians(offset.getPolarDirection() - 45);
    double magnitude = Math.min(1, offset.getPolarDistance());
    double power = Range.clip(unscaledPower, -1, 1);

    setMotorMode(RUN_WITHOUT_ENCODER);

    for (MotorAdapter motor : leftDiagonal) {
      motor.setPower(Math.sin(direction) * magnitude * Math.abs(power));
    }
    for (MotorAdapter motor : rightDiagonal) {
      motor.setPower(Math.cos(direction) * magnitude * Math.abs(power));
    }
  }

  public void setStrafeAndRotation(Coordinate offset, double rotatePower, double unscaledPower) {
    double direction = Converter.degreesToRadians(offset.getPolarDirection() - 45);
    double magnitude = Math.min(1, offset.getPolarDistance());
    double power = Range.clip(unscaledPower, -1, 1);

    setMotorMode(RUN_WITHOUT_ENCODER);

    for (MotorAdapter motor : leftDiagonal) {
      motor.setPower(Math.sin(direction) * magnitude * Math.abs(power));
    }
    for (MotorAdapter motor : rightDiagonal) {
      motor.setPower(Math.cos(direction) * magnitude * Math.abs(power));
    }
    for (MotorAdapter motor : leftSide()) {
      motor.setPower(motor.getPower() + rotatePower);
    }
    for (MotorAdapter motor : rightSide()) {
      motor.setPower(motor.getPower() - rotatePower);
    }
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

    for (MotorAdapter motor : leftSide()) {
      motor.setPower(leftPower);
    }
    for (MotorAdapter motor : rightSide()) {
      motor.setPower(rightPower);
    }
  }

  @Override
  protected IMotor[] getMotors() {
    return allMotors();
  }

  private double meanVarianceFromTargetPosition() {
    int varianceSum = 0;
    for (MotorAdapter motor : allMotors()) {
      varianceSum += Math.abs(motor.getMotor().getTargetPosition() - motor.getMotor().getCurrentPosition());
    }

    return varianceSum / 4.0;
  }

  public boolean isRunningToPosition() {
    return isBusy();
  }
}
