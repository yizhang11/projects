package edu.neu.cs5010.yahtzee.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GameScoreTest {
  private GameScore gameScore;

  @Before
  public void setUp() throws Exception {
    this.gameScore = new GameScore(2);
  }

  @Test
  public void setScores() {
    this.gameScore.setScores(1, new int[]{1, 1, 1, 2, 3});
    assertEquals(3, this.gameScore.getScores()[0]);
    this.gameScore.setScores(2, new int[]{2, 2, 2, 2, 3});
    assertEquals(8, this.gameScore.getScores()[1]);
    this.gameScore.setScores(3, new int[]{3, 3, 3, 2, 3});
    assertEquals(12, this.gameScore.getScores()[2]);
    this.gameScore.setScores(4, new int[]{4, 4, 4, 4, 3});
    assertEquals(16, this.gameScore.getScores()[3]);
    this.gameScore.setScores(5, new int[]{5, 5, 5, 5, 3});
    assertEquals(20, this.gameScore.getScores()[4]);
    this.gameScore.setScores(6, new int[]{6, 6, 6, 6, 3});
    assertEquals(24, this.gameScore.getScores()[5]);
    this.gameScore.setScores(7, new int[]{1, 1, 1, 2, 3});
    assertEquals(8, this.gameScore.getScores()[6]);
    this.gameScore.setScores(7, new int[]{1, 1, 2, 2, 3});
    assertEquals(0, this.gameScore.getScores()[6]);
    this.gameScore.setScores(8, new int[]{1, 1, 1, 1, 3});
    assertEquals(7, this.gameScore.getScores()[7]);
    this.gameScore.setScores(8, new int[]{1, 3, 1, 2, 2});
    assertEquals(0, this.gameScore.getScores()[7]);
    this.gameScore.setScores(9, new int[]{1, 1, 1, 2, 2});
    assertEquals(25, this.gameScore.getScores()[8]);
    this.gameScore.setScores(9, new int[]{1, 3, 1, 2, 2});
    assertEquals(0, this.gameScore.getScores()[8]);
    this.gameScore.setScores(10, new int[]{1, 4, 1, 2, 3});
    assertEquals(30, this.gameScore.getScores()[9]);
    this.gameScore.setScores(10, new int[]{1, 4, 1, 1, 3});
    assertEquals(0, this.gameScore.getScores()[9]);
    this.gameScore.setScores(11, new int[]{1, 4, 5, 2, 3});
    assertEquals(40, this.gameScore.getScores()[10]);
    this.gameScore.setScores(11, new int[]{1, 2, 5, 2, 3});
    assertEquals(0, this.gameScore.getScores()[10]);

    this.gameScore.setScores(12, new int[]{5, 5, 5, 5, 5});
    assertEquals(50, this.gameScore.getScores()[11]);
    this.gameScore.setScores(1, new int[]{1, 1, 1, 1, 1});
    assertEquals(5, this.gameScore.getScores()[0]);
    assertEquals(150, this.gameScore.getScores()[11]);
    this.gameScore.setScores(12, new int[]{1, 1, 1, 2, 1});
    assertEquals(0, this.gameScore.getScores()[11]);
    this.gameScore.setScores(13, new int[]{1, 1, 1, 2, 3});
    assertEquals(8, this.gameScore.getScores()[12]);

    assertEquals(128, this.gameScore.getTotalScore());
  }

  @Test
  public void getScoreCard() {
    assertEquals("Aces -1 Twos -1 Threes -1 Fours -1 Fives -1 Sixes -1 ThreeOfKind -1 "
        + "FourOfKind -1 FullHouse -1 SmallStraight -1 LargeStraight -1 Yahtzee -1 Chance -1 "
        + "Total 0", this.gameScore.getScoreCard());
  }

  @Test
  public void getId() {
    assertEquals(2, this.gameScore.getId());
  }
}