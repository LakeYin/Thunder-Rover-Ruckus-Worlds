package com.andoverrobotics.core.utilities;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConverterTest {

  @Test
  public void degreesToRadiansInRange() {
    assertEquals(0.0, Converter.degreesToRadians(0), 1e-7);
    assertEquals(Math.PI / 4, Converter.degreesToRadians(45), 1e-7);
    assertEquals(Math.PI / 2, Converter.degreesToRadians(90), 1e-7);
  }

  @Test
  public void degreesToRadiansUnderRange() {
    assertEquals(Math.PI, Converter.degreesToRadians(-180), 1e-7);
    assertEquals(Math.PI / 2, Converter.degreesToRadians(-270), 1e-7);
    assertEquals(Math.PI, Converter.degreesToRadians(-180 - 360), 1e-7);
  }

  @Test
      public void degreesToRadiansAboveRange() {
    assertEquals(0.0, Converter.degreesToRadians(720), 1e-7);
    assertEquals(Math.PI, Converter.degreesToRadians(180 + 360 * 3), 1e-7);
  }

  @Test
  public void radiansToDegreesInRange() {
    assertEquals(0, Converter.radiansToDegrees(0.0), 1e-7);
    assertEquals(45, Converter.radiansToDegrees(Math.PI / 4), 1e-7);
    assertEquals(90, Converter.radiansToDegrees(Math.PI / 2), 1e-7);
  }

  @Test
  public void radiansToDegreesUnderRange() {
    assertEquals(180, Converter.radiansToDegrees(-Math.PI), 1e-7);
    assertEquals(90, Converter.radiansToDegrees(-Math.PI * 1.5), 1e-7);
  }

  @Test
  public void radiansToDegreesAboveRange() {
    assertEquals(135, Converter.radiansToDegrees(Math.PI * 2.75), 1e-7);
    assertEquals(270, Converter.radiansToDegrees(Math.PI * 5.5), 1e-7);
  }
}