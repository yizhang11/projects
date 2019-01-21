package edu.neu.cs5010.yahtzee.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class CombinationTest {

  @Test
  public void isYahtzee() {
    assertTrue(Combination.isYahtzee(new int[]{1, 1, 1, 1, 1}));
    assertTrue(!Combination.isYahtzee(new int[]{1, 1, 2, 1, 1}));
  }

  @Test
  public void isLargeStraight() {
    assertTrue(Combination.isLargeStraight(new int[]{1, 2, 3, 4, 5}));
    assertTrue(Combination.isLargeStraight(new int[]{2, 3, 4, 5, 6}));
    assertTrue(!Combination.isLargeStraight(new int[]{1, 3, 4, 5, 6}));
  }

  @Test
  public void isSmallStraight() {
    assertTrue(Combination.isSmallStraight(new int[]{1, 2, 3, 4, 5}));
    assertTrue(Combination.isSmallStraight(new int[]{3, 3, 4, 5, 6}));
    assertTrue(Combination.isSmallStraight(new int[]{1, 3, 4, 5, 6}));
    assertTrue(!Combination.isSmallStraight(new int[]{3, 3, 3, 3, 6}));
    assertTrue(!Combination.isSmallStraight(new int[]{4, 4, 4, 4, 6}));
    assertTrue(!Combination.isSmallStraight(new int[]{5, 2, 6, 5, 6}));
    assertTrue(!Combination.isSmallStraight(new int[]{3, 4, 3, 4, 2}));
    assertTrue(!Combination.isSmallStraight(new int[]{3, 4, 3, 4, 5}));
    assertTrue(!Combination.isSmallStraight(new int[]{3, 4, 3, 4, 1}));
    assertTrue(!Combination.isSmallStraight(new int[]{3, 4, 3, 4, 6}));
  }

  @Test
  public void isFullHouse() {
    assertTrue(Combination.isFullHouse(new int[]{3, 4, 3, 4, 3}));
    assertTrue(Combination.isFullHouse(new int[]{3, 4, 3, 4, 4}));
    assertTrue(!Combination.isFullHouse(new int[]{3, 4, 3, 4, 5}));
    assertTrue(!Combination.isFullHouse(new int[]{3, 3, 3, 4, 5}));
    assertTrue(!Combination.isFullHouse(new int[]{3, 3, 3, 4, 3}));
  }

  @Test
  public void isFourOfKind() {
    assertTrue(Combination.isFourOfKind(new int[]{3, 3, 3, 4, 3}));
    assertTrue(Combination.isFourOfKind(new int[]{3, 4, 4, 4, 4}));
    assertTrue(!Combination.isFourOfKind(new int[]{3, 3, 3, 4, 5}));
    assertTrue(!Combination.isFourOfKind(new int[]{3, 3, 4, 4, 4}));
  }

  @Test
  public void isThreeOfKind() {
    assertTrue(Combination.isThreeOfKind(new int[]{3, 3, 3, 4, 3}));
    assertTrue(Combination.isThreeOfKind(new int[]{3, 3, 5, 4, 3}));
    assertTrue(!Combination.isThreeOfKind(new int[]{2, 3, 5, 4, 3}));
  }
}