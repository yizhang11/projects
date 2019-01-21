package edu.neu.cs5010.yahtzee.server;

import java.util.HashSet;
import java.util.Set;

/**
 * this class has different combinations of dice results.
 */
public class Combination {

  /**
   * Check if is a yahtzee.
   * @param dices dice numbers.
   * @return true of false.
   */
  public static boolean isYahtzee(int[] dices) {
    Set<Integer> set = new HashSet<>();
    for (int dice : dices) {
      set.add(dice);
    }
    return set.size() == 1;
  }

  /**
   * Check if is a large straight.
   * @param dices dice numbers.
   * @return true of false.
   */
  public static boolean isLargeStraight(int[] dices) {
    Set<Integer> set = new HashSet<>();
    for (int dice : dices) {
      set.add(dice);
    }
    return set.size() == 5 && !(set.contains(1) && set.contains(6));
  }

  /**
   * Check if is a small straight.
   * @param dices dice numbers.
   * @return true of false.
   */
  public static boolean isSmallStraight(int[] dices) {
    Set<Integer> set = new HashSet<>();
    for (int dice : dices) {
      set.add(dice);
    }
    if (!(set.contains(3) && set.contains(4))) {
      return false;
    }
    if (set.contains(2) && set.contains(5)) {
      return true;
    }
    if (set.contains(1) && set.contains(2)) {
      return true;
    }
    if (set.contains(5) && set.contains(6)) {
      return true;
    }
    return false;
  }

  /**
   * Check if is a full house.
   * @param dices dice numbers.
   * @return true of false.
   */
  public static boolean isFullHouse(int[] dices) {
    Set<Integer> set = new HashSet<>();
    int[] count = new int[6];
    for (int dice : dices) {
      set.add(dice);
      count[dice - 1]++;
    }
    if (set.size() > 2) {
      return false;
    }
    for (int dice: set) {
      if (count[dice - 1] == 3) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if is a four of kind.
   * @param dices dice numbers.
   * @return true of false.
   */
  public static boolean isFourOfKind(int[] dices) {
    Set<Integer> set = new HashSet<>();
    int[] count = new int[6];
    for (int dice : dices) {
      set.add(dice);
      count[dice - 1]++;
    }
    if (set.size() > 2) {
      return false;
    }
    for (int dice : set) {
      if (count[dice - 1] >= 4) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if is a three of kind.
   * @param dices dice numbers.
   * @return true of false.
   */
  public static boolean isThreeOfKind(int[] dices) {
    Set<Integer> set = new HashSet<>();
    int[] count = new int[6];
    for (int dice : dices) {
      set.add(dice);
      count[dice - 1]++;
    }
    if (set.size() > 3) {
      return false;
    }
    for (int dice : set) {
      if (count[dice - 1] >= 3) {
        return true;
      }
    }
    return false;
  }
}
