package com.andoverrobotics.core.drivetrain;

import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.andoverrobotics.core.utilities.Coordinate;
import com.andoverrobotics.core.utilities.IMotor;
import com.andoverrobotics.core.utilities.MotorPair;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

public class MecanumDriveTest {
  private OpMode opMode = mock(OpMode.class);
  private MotorPair
      rightDiagonal = mock(MotorPair.class),
      leftDiagonal = mock(MotorPair.class),
      rightSide = mock(MotorPair.class),
      leftSide = mock(MotorPair.class);

  private StrafingDriveTrain driveTrain = new MecanumDrive(
      leftDiagonal, rightDiagonal, leftSide, rightSide,
      opMode, 5, 100);

  @Before
  public void setUp() {
    reset(rightDiagonal, leftDiagonal, rightSide, leftSide, opMode);
  }

  @Test
  public void driveForwards() {
    driveTrain.driveForwards(5, 1);

    int tickOffset = (int) (5 * 5 / Math.sqrt(2));

    verifyRunToPosition(leftDiagonal, tickOffset, 1);
    verifyRunToPosition(rightDiagonal, tickOffset, 1);
  }

  @Test
  public void driveBackwardsPositivePower() {
    driveTrain.driveBackwards(8, 0.6);

    int tickOffset = (int) (-8 * 5 / Math.sqrt(2));

    verifyRunToPosition(leftDiagonal, tickOffset, -0.6);
    verifyRunToPosition(rightDiagonal, tickOffset, -0.6);
  }

  @Test
  public void driveBackwardsNegativePower() {
    driveTrain.driveBackwards(8, -0.4);

    int tickOffset = (int) (-8 * 5 / Math.sqrt(2));

    verifyRunToPosition(leftDiagonal, tickOffset, -0.4);
    verifyRunToPosition(rightDiagonal, tickOffset, -0.4);
  }

  @Test
  public void driveBackwardsNegativeDistance() {
    driveTrain.driveBackwards(-4, 0.6);

    int tickOffset = (int) (-4 * 5 / Math.sqrt(2));

    verifyRunToPosition(leftDiagonal, tickOffset, -0.6);
    verifyRunToPosition(rightDiagonal, tickOffset, -0.6);
  }

  @Test
  public void rotateClockwise() {
    driveTrain.rotateClockwise(50, 0.6);

    verifyRunToPosition(leftSide, (int) (50 / 360.0 * 100), 0.6);
    verifyRunToPosition(rightSide, (int) (-50 / 360.0 * 100), 0.6);
  }

  @Test
  public void rotateClockwiseNegativeDegree() {
    driveTrain.rotateClockwise(-36, 0.2);

    verifyRunToPosition(leftSide, (int) ((360 - 36) / 360.0 * 100), 0.2);
    verifyRunToPosition(rightSide, (int) ((36 - 360) / 360.0 * 100), 0.2);
  }

  @Test
  public void rotateCounterClockwise() {
    driveTrain.rotateCounterClockwise(50, -0.5);

    verifyRunToPosition(leftSide, (int) (-50 / 360.0 * 100), 0.5);
    verifyRunToPosition(rightSide, (int) (50 / 360.0 * 100), 0.5);
  }

  @Test
  public void rotateCounterClockwiseNegativeDegree() {
    driveTrain.rotateCounterClockwise(-70, -0.8);

    verifyRunToPosition(leftSide, (int) ((70 - 360) / 360.0 * 100), 0.8);
    verifyRunToPosition(rightSide, (int) ((360 - 70) / 360.0 * 100), 0.8);
  }

  @Test
  public void strafeRight() {
    driveTrain.strafeRight(20.5, 0.6);

    int tickOffset = (int) (20.5 * 5 / Math.sqrt(2));

    verifyRunToPosition(leftDiagonal, -tickOffset, -0.6);
    verifyRunToPosition(rightDiagonal, tickOffset, 0.6);
  }

  @Test
  public void strafeRightNegativeDistance() {
    driveTrain.strafeRight(-14.8, -0.7);

    int tickOffset = (int) (14.8 * 5 / Math.sqrt(2));

    verifyRunToPosition(leftDiagonal, -tickOffset, -0.7);
    verifyRunToPosition(rightDiagonal, tickOffset, 0.7);
  }

  @Test
  public void strafeLeft() {
    driveTrain.strafeLeft(23.1, 0.4);

    int tickOffset = (int) (23.1 * 5 / Math.sqrt(2));

    verifyRunToPosition(leftDiagonal, tickOffset, 0.4);
    verifyRunToPosition(rightDiagonal, -tickOffset, -0.4);
  }

  @Test
  public void strafeLeftNegativeDistance() {
    driveTrain.strafeLeft(-24.5, -0.64);

    int tickOffset = (int) (24.5 * 5 / Math.sqrt(2));

    verifyRunToPosition(leftDiagonal, tickOffset, 0.64);
    verifyRunToPosition(rightDiagonal, -tickOffset, -0.64);
  }

  @Test
  public void strafeInches() {
    driveTrain.strafeInches(10, 15, 0.4);

    verifyRunToPosition(leftDiagonal, (int) (3.5355339 * 5), 0.08);
    verifyRunToPosition(rightDiagonal, (int) (17.67766953 * 5), 0.4);
  }

  @Test
  public void strafeInchesNullVector() {
    driveTrain.strafeInches(0, 0, 0.4);

    verifyNoMoreInteractions(leftDiagonal, rightDiagonal);
  }

  @Test
  public void setMovementPower() {
    driveTrain.setMovementPower(0.7);
    verifyPowersWithoutEncoder(0.7, 0.7);
  }

  @Test
  public void setRotationPower() {
    driveTrain.setRotationPower(0.4);
    verifyPowersWithoutEncoder(0.4, -0.4);
  }

  @Test
  public void setRotationPowerNegative() {
    driveTrain.setRotationPower(-1.2);
    verifyPowersWithoutEncoder(-1, 1);
  }

  @Test
  public void setMovementAndRotation() {
    driveTrain.setMovementAndRotation(0.5, 0.2);
    verifyPowersWithoutEncoder(0.7, 0.3);
  }

  @Test
  public void setMovementAndRotationWithSingleOverflow() {
    driveTrain.setMovementAndRotation(0.8, -0.6);
    verifyPowersWithoutEncoder(0.2 / 1.4, 1);
  }

  @Test
  public void setMovementAndRotationWithMultipleOverflows() {
    driveTrain.setMovementAndRotation(0.8, 5);
    verifyPowersWithoutEncoder(1, -4.2 / 5.8);
  }

  @Test
  public void setStrafeWithinUnitCircle() {
    driveTrain.setStrafe(Coordinate.fromXY(0.5, 0.7), 1);

    verifyPairPower(leftDiagonal, 0.141421356);
    verifyPairPower(rightDiagonal, 0.8485281374);
  }

  @Test
  public void setStrafeOutsideUnitCircle() {
    driveTrain.setStrafe(Coordinate.fromXY(525, -1441), 0.5);

    verifyPairPower(leftDiagonal, -0.45322105262);
    verifyPairPower(rightDiagonal, -0.211165047915);
  }

  @Test
  public void setStrafeWithOrigin() {
    driveTrain.setStrafe(0, 0, Double.MAX_VALUE);

    verifyPairPower(leftDiagonal, 0);
    verifyPairPower(rightDiagonal, 0);
  }

  private void verifyPowersWithoutEncoder(double leftPower, double rightPower) {
    verify(leftSide).setMode(RunMode.RUN_WITHOUT_ENCODER);
    verify(rightSide).setMode(RunMode.RUN_WITHOUT_ENCODER);

    verifyPairPower(leftSide, leftPower);
    verifyPairPower(rightSide, rightPower);
  }

  private void verifyRunToPosition(IMotor mock, int tickOffset, double power) {
    verify(mock).startRunToPosition(eq(tickOffset),
        or(AdditionalMatchers.eq(power, 1e-4),
            AdditionalMatchers.eq(-power, 1e-4)));
  }

  private void verifyPairPower(MotorPair pair, double power) {
    verify(pair).setPower(AdditionalMatchers.eq(power, 1e-3));
  }
}