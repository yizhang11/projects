package edu.neu.cs5010.yahtzee.client;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class YahtzeeClientTest {

  @Test
  public void checkKeepDice() {
    String correct = "1 0 1 1 0";
    String tooLong = "1 1 1 1 1 1";
    String notNum = "1 2 t y 6";
    String hasSpace = "1  1 0  1 0";
    Assert.assertTrue(YahtzeeClient.checkKeepDice(correct));
    Assert.assertTrue(YahtzeeClient.checkKeepDice(hasSpace));
    Assert.assertFalse(YahtzeeClient.checkKeepDice(tooLong));
    Assert.assertFalse(YahtzeeClient.checkKeepDice(notNum));

  }
}