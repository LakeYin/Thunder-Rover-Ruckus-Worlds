package org.firstinspires.ftc.teamcode.autonomous.tasks;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RotateByIMUTaskTest {
  @Test
  public void targetHeading() {
    assertEquals(-180, RotateByIMUTask.targetHeading(150, 30));
    assertEquals(40, RotateByIMUTask.targetHeading(90, -50));
    assertEquals(-160, RotateByIMUTask.targetHeading(170, 30));
    assertEquals(150, RotateByIMUTask.targetHeading(-140, -70));
    assertEquals(10, RotateByIMUTask.targetHeading(-170, -180));
  }

  @Test
  public void normalize() {
    assertEquals(0, RotateByIMUTask.normalize(-180));
    assertEquals(0, RotateByIMUTask.normalize(180));
    assertEquals(50, RotateByIMUTask.normalize(-130));
    assertEquals(200, RotateByIMUTask.normalize(380));
    assertEquals(240, RotateByIMUTask.normalize(-300));
  }

  @Test
  public void denormalize() {
    assertEquals(-180, RotateByIMUTask.denormalize(0));
    assertEquals(150, RotateByIMUTask.denormalize(330));
    assertEquals(-150, RotateByIMUTask.denormalize(30 - 360 * 10));
    assertEquals(179, RotateByIMUTask.denormalize(359 + 360 * 3));
  }

  @Test
  public void normDenorm() {
    for (int i = 0; i < 360; i++) {
      assertEquals(i, RotateByIMUTask.normalize(RotateByIMUTask.denormalize(i)));
    }
    for (int i = -180; i < 180; i++) {
      assertEquals(i, RotateByIMUTask.denormalize(RotateByIMUTask.normalize(i)));
    }
  }

  @Test
  public void error() {
    assertEquals(20, RotateByIMUTask.error(140, 160));
    assertEquals(30, RotateByIMUTask.error(170, -160));
    assertEquals(-50, RotateByIMUTask.error(150, 100));
    assertEquals(-60, RotateByIMUTask.error(-160, 140));
    assertEquals(20, RotateByIMUTask.error(170, -170));
    assertEquals(-20, RotateByIMUTask.error(-170, 170));
  }
}