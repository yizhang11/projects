package edu.neu.cs5010.yahtzee.server;

import java.util.Arrays;

/**
 * this class keeps track of game scores for users.
 */
public class GameScore {
  private int[] scores = new int[13];
  private int playerId;

  public GameScore(int playerId) {
    this.playerId = playerId;
    Arrays.fill(this.scores, -1);
  }

  /**
   * Set the score according to type choice and dices.
   * Multiple yahtzee can get bonus.
   * @param type type of choice.
   * @param dices dice numbers.
   */
  public void setScores(int type, int[] dices) {
    if (Combination.isYahtzee(dices) && scores[11] != -1) {
      scores[11] += 100;
    }
    switch (type) {
      case 1:
        scores[0] = this.upperSection(1, dices);
        break;
      case 2:
        scores[1] = this.upperSection(2, dices);
        break;
      case 3:
        scores[2] = this.upperSection(3, dices);
        break;
      case 4:
        scores[3] = this.upperSection(4, dices);
        break;
      case 5:
        scores[4] = this.upperSection(5, dices);
        break;
      case 6:
        scores[5] = this.upperSection(6, dices);
        break;
      case 7:
        if (Combination.isThreeOfKind(dices)) {
          scores[6] = this.total(dices);
        } else {
          scores[6] = 0;
        }
        break;
      case 8:
        if (Combination.isFourOfKind(dices)) {
          scores[7] = this.total(dices);
        } else {
          scores[7] = 0;
        }
        break;
      case 9:
        if (Combination.isFullHouse(dices)) {
          scores[8] = 25;
        } else {
          scores[8] = 0;
        }
        break;
      case 10:
        if (Combination.isSmallStraight(dices)) {
          scores[9] = 30;
        } else {
          scores[9] = 0;
        }
        break;
      case 11:
        if (Combination.isLargeStraight(dices)) {
          scores[10] = 40;
        } else {
          scores[10] = 0;
        }
        break;
      case 12:
        if (Combination.isYahtzee(dices)) {
          scores[11] = 50;
        } else {
          scores[11] = 0;
        }
        break;
      case 13:
        scores[12] = this.total(dices);
        break;
      default:
        break;
    }
  }

  /**
   * Calculates the total number of dices.
   * @param dices dice numbers.
   * @return score.
   */
  public int total(int[] dices) {
    int score = 0;
    for (int dice : dices) {
      score += dice;
    }
    return score;
  }

  /**
   * Calculates the score of upper section types.
   * @param type aces, twos, etc.
   * @param dices dice numbers.
   * @return score.
   */
  public int upperSection(int type, int[] dices) {
    int score = 0;
    for (int dice : dices) {
      if (dice == type) {
        score += dice;
      }
    }
    return score;
  }

  /**
   * If the total score of upper section is more than 63, add bonus.
   * @return the upper bonus.
   */
  public int getUpperBonus() {
    int tempUpperTotal = 0;
    boolean upperFilled = true;
    for (int i = 0; i < 6; i++) {
      upperFilled &= scores[i] > -1;
      tempUpperTotal += scores[i];
    }
    if (upperFilled && tempUpperTotal >= 63) {
      return 35;
    } else {
      return 0;
    }
  }

  /**
   * Calculates total score.
   * @return total score.
   */
  public int getTotalScore() {
    int totalScore = 0;
    for (int score : scores) {
      totalScore += score == -1 ? 0 : score;
    }
    totalScore += this.getUpperBonus();
    return totalScore;
  }

  /**
   * Displays scores and calculates total.
   * @return score card.
   */
  public String getScoreCard() {
    return "Aces " + scores[0] + " Twos " + scores[1] + " Threes " + scores[2]
        + " Fours " + scores[3] + " Fives " + scores[4] + " Sixes " + scores[5] + " ThreeOfKind "
        + scores[6] + " FourOfKind " + scores[7] + " FullHouse " + scores[8] + " SmallStraight "
        + scores[9] + " LargeStraight " + scores[10] + " Yahtzee " + scores[11] + " Chance "
        + scores[12] + " Total " + this.getTotalScore();
  }

  public int[] getScores() {
    return Arrays.copyOf(scores, scores.length);
  }

  public int getId() {
    return playerId;
  }
}
