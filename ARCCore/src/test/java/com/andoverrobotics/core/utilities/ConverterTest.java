package com.andoverrobotics.core.utilities;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConverterTest {

  @Test
  public void degreesToRadians() {
    assertEquals(0.0, Converter.degreesToRadians(0), 1e-7);
    assertEquals(Math.PI / 4, Converter.degreesToRadians(45), 1e-7);
    assertEquals(Math.PI / 2, Converter.degreesToRadians(90), 1e-7);
    assertEquals(Math.PI, Converter.degreesToRadians(-180), 1e-7);
    assertEquals(Math.PI / 2, Converter.degreesToRadians(-270), 1e-7);

    assertEquals(0.0, Converter.degreesToRadians(720), 1e-7);
    assertEquals(Math.PI, Converter.degreesToRadians(-180-360), 1e-7);
  }

  @Test
  public void radiansToDegrees() {
    assertEquals(0, Converter.radiansToDegrees(0.0));
    assertEquals(45, Converter.radiansToDegrees(Math.PI / 4));
    assertEquals(90, Converter.radiansToDegrees(Math.PI / 2));
    assertEquals(180, Converter.radiansToDegrees(-Math.PI));
    assertEquals(90, Converter.radiansToDegrees(-Math.PI * 1.5));
  }
}