package com.andoverrobotics.core.drivetrain;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.andoverrobotics.core.utilities.IMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

public class TankDriveTest {

  private final OpMode opMode = mock(OpMode.class);
  private final IMotor motorL = mock(IMotor.class),
      motorR = mock(IMotor.class);
  private DriveTrain driveTrain = new TankDrive(motorL, motorR, opMode,50, 720);

  @Before
  public void setUp() {
    reset(motorL);
    reset(motorR);
  }

  @Test
  public void driveForwards() {
    driveTrain.driveForwards(5, 0.5);
    verifyDrivenDisplacementWithPower(5, 0.5);
  }

  @Test
  public void driveForwardsWithNegativeDistance() {
    driveTrain.driveForwards(-5, 0.5);
    verifyDrivenDisplacementWithPower(5, 0.5);
  }

  @Test
  public void driveForwardsWithNegativePower() {
    driveTrain.driveForwards(5, -0.5);
    verifyDrivenDisplacementWithPower(5, 0.5);
  }

  @Test
  public void driveForwardsWithNegativeDistanceAndPower() {
    driveTrain.driveForwards(-5, -0.5);
    verifyDrivenDisplacementWithPower(5, 0.5);
  }

  @Test
  public void driveForwardsWithZeroPower() {
    driveTrain.driveForwards(2, 0);
    verifyPowersSet(0, 0);
  }

  @Test
  public void driveBackwards() {
    driveTrain.driveBackwards(5, 0.7);
    verifyDrivenDisplacementWithPower(-5, -0.7);
  }

  @Test
  public void driveBackwardsWithNegativeDistance() {
    driveTrain.driveBackwards(-5, 0.7);
    verifyDrivenDisplacementWithPower(-5, -0.7);
  }

  @Test
  public void driveBackwardsWithNegativePower() {
    driveTrain.driveBackwards(5, -0.7);
    verifyDrivenDisplacementWithPower(-5, -0.7);
  }

  @Test
  public void driveBackwardsWithZeroPower() {
    driveTrain.driveBackwards(2, 0);
    verifyPowersSet(0, 0);
  }

  @Test
  public void rotateClockwise() {
    driveTrain.rotateClockwise(270, 0.6);

    verifyRunToPosition(270 * 2, -270 * 2, 0.6, -0.6);
  }

  @Test
  public void rotateClockwiseNegativeDegree() {
    driveTrain.rotateClockwise(-45, 0.4);

    verifyRunToPosition((360-45) * 2, (45-360) * 2, 0.4, -0.4);
  }

  @Test
  public void rotateClockwiseNegativePower() {
    driveTrain.rotateClockwise(45, -0.4);

    verifyRunToPosition(45 * 2, -45 * 2, 0.4, -0.4);
  }

  @Test
  public void rotateCounterClockwise() {
    driveTrain.rotateCounterClockwise(70, 0.2);

    verifyRunToPosition(-70 * 2, 70 * 2, -0.2, 0.2);
  }

  @Test
  public void rotateCounterClockwiseNegativePower() {
    driveTrain.rotateCounterClockwise(80, -0.3);

    verifyRunToPosition(-80 * 2, 80 * 2, -0.3, 0.3);
  }

  @Test
  public void rotateCounterClockwiseNegativeDegree() {
    driveTrain.rotateCounterClockwise(-50, 0.5);

    verifyRunToPosition((50-360) * 2, (360-50) * 2, -0.5, 0.5);
  }

  @Test
  public void setMovementPower() {
    driveTrain.setMovementPower(1.0);
    verifyPowersSet(1.0, 1.0);

    driveTrain.setMovementPower(-0.6);
    verifyPowersSet(-0.6, -0.6);

    driveTrain.setMovementPower(0.0);
    verifyPowersSet(0.0, 0.0);
  }

  @Test
  public void setRotationPower() {
    driveTrain.setRotationPower(0.2);
    verifyPowersSet(0.2, -0.2);

    driveTrain.setRotationPower(-0.4);
    verifyPowersSet(-0.4, 0.4);

    driveTrain.setRotationPower(0.8);
    verifyPowersSet(0.8, -0.8);
  }

  @Test
  public void setMovementAndRotation() {
    driveTrain.setMovementAndRotation(0.5, 0.2);
    verifyPowersSet(0.7, 0.3);

    driveTrain.setMovementAndRotation(0.5, 0.8);
    verifyPowersSet(1, -0.3/1.3);

    driveTrain.setMovementAndRotation(-0.4, -0.3);
    verifyPowersSet(-0.7, -0.1);

    driveTrain.setMovementAndRotation(-0.9, -0.2);
    verifyPowersSet(-1, -0.7/1.1);
  }

  // -- Start verification methods --

  private void verifyDrivenDisplacementWithPower(
      int inches, double power) {
    verifyRunToPosition(inches * 50, inches * 50, power, power);
  }

  private void verifyRunToPosition(int ticksLeft, int ticksRight, double powerLeft, double powerRight) {
    verify(motorL).startRunToPosition(
        eq(ticksLeft), AdditionalMatchers.eq(powerLeft, 1e-4));
    verify(motorR).startRunToPosition(
        eq(ticksRight), AdditionalMatchers.eq(powerRight, 1e-4));
  }

  private void verifyPowersSet(double left, double right) {
    verify(motorL).setPower(AdditionalMatchers.eq(left, 1e-4));
    verify(motorR).setPower(AdditionalMatchers.eq(right, 1e-4));
  }
}