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
    assertEquals(341.56505, testee.getPolarDirection(), 1e-3);
  }

  @Test
  public void getPolarDistance() {

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