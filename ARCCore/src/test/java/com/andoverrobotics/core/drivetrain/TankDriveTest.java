package com.andoverrobotics.core.drivetrain;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.junit.Before;
import org.junit.Test;

public class TankDriveTest {

  private final DcMotor motorL = mock(DcMotor.class),
      motorR = mock(DcMotor.class);
  private DriveTrain driveTrain = new TankDrive(motorL, motorR);

  @Before
  public void setUp() {
    reset(motorL);
    reset(motorR);
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

  private void verifyPowersSet(double left, double right) {
    verify(motorL).setPower(left);
    verify(motorR).setPower(right);
  }
}