package com.andoverrobotics.core.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoordinateTest {
  private static Coordinate testee;

  @Test
  public void getPolarDirection() {
    givenEuclidean(1, 0);
    assertEquals(0, testee.getPolarDirection(), 1e-7);

    givenEuclidean(2, 2);
    assertEquals(45, testee.getPolarDirection(), 1e-7);

    givenEuclidean(3, -1);
    assertEquals(341.565051177, testee.getPolarDirection(), 1e-7);
  }

  @Test
  public void getPolarDistance() {
    givenEuclidean(3, 4);
    assertEquals(5, testee.getPolarDistance(), 1e-7);

    givenPolar(4.24, 120);
    assertEquals(4.24, testee.getPolarDistance(), 1e-7);
  }

  @Test
  public void fromPolar() {
    givenPolar(10, 45);
    assertEquals(10 / Math.sqrt(2), testee.getX(), 1e-7);
    assertEquals(10 / Math.sqrt(2), testee.getY(), 1e-7);

    givenPolar(20, 140);
    assertEquals(20 * Math.cos(2.44346), testee.getX(), 1e-4);
    assertEquals(20 * Math.sin(2.44346), testee.getY(), 1e-4);
  }

  @Test
  public void rotate() {
    givenPolar(5, 24);

    testee = testee.rotate(30);

    assertEquals(5, testee.getPolarDistance(), 1e-5);
    assertEquals(54, testee.getPolarDirection(), 1e-5);
    assertEquals(5 * Math.cos(54 / 180.0 * Math.PI), testee.getX(), 1e-5);
    assertEquals(5 * Math.sin(54 / 180.0 * Math.PI), testee.getY(), 1e-5);

    givenPolar(5, 350);

    testee = testee.rotate(20);

    assertEquals(5, testee.getPolarDistance(), 1e-5);
    assertEquals(10, testee.getPolarDirection(), 1e-5);

    testee = testee.rotate(-35);

    assertEquals(5 * Math.cos(25 / 180.0 * Math.PI), testee.getX(), 1e-5);
    assertEquals(-5 * Math.sin(25 / 180.0 * Math.PI), testee.getY(), 1e-5);
  }

  private void given(Coordinate coord) {
    testee = coord;
  }
  private void givenEuclidean(double x, double y) {
    given(Coordinate.fromXY(x, y));
  }
  private void givenPolar(double distance, int degrees) {
    given(Coordinate.fromPolar(distance, degrees));
  }
}