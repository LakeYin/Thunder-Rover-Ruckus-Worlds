package com.andoverrobotics.core.utilities;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoordinateTest {
  private static Coordinate testee;

  @Test
  public void getPolarDirection() {
    givenEuclidean(1, 0);
    assertEquals(0, testee.getPolarDirection());

    givenEuclidean(2, 2);
    assertEquals(45, testee.getPolarDirection());

    givenEuclidean(3, -1);
    assertEquals(342, testee.getPolarDirection());
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

  private void given(Coordinate coord) {
    testee = coord;
  }
  private void givenEuclidean(double x, double y) {
    testee = Coordinate.fromXY(x, y);
  }
  private void givenPolar(double distance, int degrees) {
    testee = Coordinate.fromPolar(distance, degrees);
  }
}