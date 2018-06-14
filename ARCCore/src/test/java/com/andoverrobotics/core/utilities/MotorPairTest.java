package com.andoverrobotics.core.utilities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.junit.Before;
import org.junit.Test;

public class MotorPairTest {
  private DcMotor one = mock(DcMotor.class),
      two = mock(DcMotor.class);

  private MotorPair pair = MotorPair.of(one, two);

  @Before
  public void setUp() {
    reset(one, two);
  }

  @Test
  public void setPower() {
    pair.setPower(0.2);

    verify(one).setPower(0.2);
    verify(two).setPower(0.2);
  }

  @Test
  public void addTargetPosition() {
    when(one.getCurrentPosition()).thenReturn(50);
    when(two.getCurrentPosition()).thenReturn(-40);

    pair.addTargetPosition(20);

    verify(one).setTargetPosition(70);
    verify(two).setTargetPosition(-20);

    pair.addTargetPosition(-10);

    verify(one).setTargetPosition(40);
    verify(two).setTargetPosition(-50);
  }

  @Test
  public void setMode() {
    pair.setMode(RunMode.RUN_USING_ENCODER);

    verify(one).setMode(RunMode.RUN_USING_ENCODER);
    verify(two).setMode(RunMode.RUN_USING_ENCODER);
  }

  @Test
  public void isBusy() {
    when(one.isBusy()).thenReturn(true);

    assertTrue(pair.isBusy());
  }

  @Test
  public void isNotBusy() {
    when(one.isBusy()).thenReturn(false);
    when(two.isBusy()).thenReturn(false);

    assertFalse(pair.isBusy());
  }
}