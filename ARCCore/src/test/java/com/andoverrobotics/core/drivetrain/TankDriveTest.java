package com.andoverrobotics.core.drivetrain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.junit.Before;
import org.junit.Test;

public class TankDriveTest {

  private final OpMode opMode = mock(OpMode.class);
  private final DcMotor motorL = mock(DcMotor.class),
      motorR = mock(DcMotor.class);
  private DriveTrain driveTrain = new TankDrive(motorL, motorR, opMode,50, 720);

  @Before
  public void setUp() {
    reset(motorL);
    reset(motorR);
    when(motorL.getTargetPosition()).thenReturn(0);
    when(motorR.getTargetPosition()).thenReturn(0);
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
  public void rotateClockwise() {
    driveTrain.rotateClockwise(270, 0.6);

    verifyTargetPositionOffset(270 * 2, -270 * 2);
    verifyPowersSet(0.6, -0.6);
  }

  @Test
  public void rotateClockwiseNegativeDegree() {
    driveTrain.rotateClockwise(-45, 0.4);

    verifyTargetPositionOffset((360-45) * 2, (45-360) * 2);
    verifyPowersSet(0.4, -0.4);
  }

  @Test
  public void rotateClockwiseNegativePower() {
    driveTrain.rotateClockwise(45, -0.4);

    verifyTargetPositionOffset(45 * 2, -45 * 2);
    verifyPowersSet(0.4, -0.4);
  }

  @Test
  public void rotateCounterClockwise() {
    driveTrain.rotateCounterClockwise(70, 0.2);

    verifyTargetPositionOffset(-70 * 2, 70 * 2);
    verifyPowersSet(-0.2, 0.2);
  }

  @Test
  public void rotateCounterClockwiseNegativePower() {
    driveTrain.rotateCounterClockwise(80, -0.3);

    verifyTargetPositionOffset(-80 * 2, 80 * 2);
    verifyPowersSet(-0.3, 0.3);
  }

  @Test
  public void rotateCounterClockwiseNegativeDegree() {
    driveTrain.rotateCounterClockwise(-50, 0.5);

    verifyTargetPositionOffset((50-360) * 2, (360-50) * 2);
    verifyPowersSet(-0.5, 0.5);
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

  // -- Start verification methods --

  private void verifyDrivenDisplacementWithPower(int displacementInInches, double power) {
    verifyTargetPositionOffset(displacementInInches * 50, displacementInInches * 50);
    verifyPowersSet(power, power);
  }

  private void verifyPowersSet(double left, double right) {
    verify(motorL).setPower(left);
    verify(motorR).setPower(right);
  }

  private void verifyTargetPositionOffset(int leftTicks, int rightTicks) {
    verify(motorL).setTargetPosition(leftTicks);
    verify(motorR).setTargetPosition(rightTicks);
  }
}