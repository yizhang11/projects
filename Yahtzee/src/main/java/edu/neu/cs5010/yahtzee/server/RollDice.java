package edu.neu.cs5010.yahtzee.server;

import java.util.Random;

/**
 * rolls the dice to get random results.
 */
public class RollDice {

  /**
   * Roll dice according to the keep dice requirement.
   * @param keep which of the dices to keep.
   * @param dices the current dices.
   * @return the re-rolled dices.
   */
  public static int[] rollDice(int[] keep, int[] dices) {
    for (int i = 0; i < 5; i++) {
      if (keep[i] == 0) {
        dices[i] = new Random().nextInt(6) + 1;
      }
    }
    return dices;
  }
}
