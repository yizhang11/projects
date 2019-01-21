package edu.neu.cs5010.yahtzee.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * this class contains protocol of the game.
 */
public class YahtzeeProtocol {
  private static final int WAITINGTOSTART = 0;
  private static final int ROLLING = 1;
  private static final int SCORING = 2;
  private static final String[] SLOTS = new String[]{"Aces", "Twos", "Threes", "Fours", "Fives",
      "Sixes", "ThreeOfKind", "FourOfKind", "FullHouse", "SmallStraight", "LargeStraight",
      "Yahtzee", "Chance"};
  private static final Map<String, Integer> SLOT_MAP = createMap();

  private static Map<String, Integer> createMap() {
    Map<String,Integer> myMap = new HashMap<>();
    myMap.put("aces", 1);
    myMap.put("twos", 2);
    myMap.put("threes", 3);
    myMap.put("fours", 4);
    myMap.put("fives", 5);
    myMap.put("sixes", 6);
    myMap.put("threeofkind", 7);
    myMap.put("fourofkind", 8);
    myMap.put("fullhouse", 9);
    myMap.put("smallstraight", 10);
    myMap.put("largestraight", 11);
    myMap.put("yahtzee", 12);
    myMap.put("chance", 13);
    return myMap;
  }

  private int rollNum;
  private String[] theOutput = new String[3];
  private int[] dices;

  private int state = WAITINGTOSTART;

  /**
   * Process the input, check if the input id matches, and keep track of who's playing.
   * @param theInput input message in the form of string array.
   * @param messageId the corresponding id.
   * @param curPlayer the current player.
   * @return output.
   */
  public String[] processInput(String[] theInput, int messageId, GameScore curPlayer) {
    String[] tokens = theInput[1].split("\\s+");
    outerLoop:
    switch (state) {
      case WAITINGTOSTART:
        state = ROLLING;
        rollNum = 1;
        theOutput[2] = "";
        theOutput[0] = "CHOOSE_DICE: ";
        dices = RollDice.rollDice(new int[5], new int[5]);
        theOutput[1] = dices[0] + " " + dices[1] + " " + dices[2] + " " + dices[3] + " " + dices[4];
        System.out.println("First roll: " + theOutput[1]);
        break;

      case ROLLING:
        if (Integer.parseInt(tokens[0]) != messageId || !theInput[0].equals("KEEP_DICE")
            || tokens.length != 6) {
          theOutput[0] = "INVALID_DICE_CHOICE: ";
          System.out.println("Not keeping dice...");
          break;
        }
        int[] keepDice = new int[5];
        boolean keepAll = true;
        for (int i = 1; i < 6; i++) {
          try {
            if (Integer.parseInt(tokens[i]) == 0) {
              keepDice[i - 1] = Integer.parseInt(tokens[i]);
              keepAll = false;
            } else if (Integer.parseInt(tokens[i]) == 1) {
              keepDice[i - 1] = Integer.parseInt(tokens[i]);
            } else {
              theOutput[0] = "INVALID_DICE_CHOICE: ";
              System.out.println("Keep 1, Roll 0...");
              break outerLoop;
            }
          } catch (NumberFormatException e) {
            theOutput[0] = "INVALID_DICE_CHOICE: ";
            System.out.println("Not a number...");
            break outerLoop;
          }
        }
        System.out.println("Dices to keep: " + Arrays.toString(keepDice));
        if (rollNum++ < 2 && !keepAll) {
          dices = RollDice.rollDice(keepDice, dices);
          theOutput[1] = dices[0] + " " + dices[1] + " " + dices[2] + " "
              + dices[3] + " " + dices[4];
          System.out.println("Rolling again: " + theOutput[1]);
          break;
        } else {
          state = SCORING;
          if (!keepAll) {
            dices = RollDice.rollDice(keepDice, dices);
            System.out.println("Last roll...");
          }
          theOutput[1] = dices[0] + " " + dices[1] + " " + dices[2] + " "
              + dices[3] + " " + dices[4];
          System.out.println("Dices: " + theOutput[1]);
          theOutput[0] = "CHOOSE_SCORE: ";
          StringBuilder slots = new StringBuilder();
          int[] scores = curPlayer.getScores();
          for (int i = 0; i < scores.length; i++) {
            if (scores[i] == -1) {
              slots.append(" " + SLOTS[i]);
            }
          }
          theOutput[2] = slots.toString();
          System.out.println("Slots: " + theOutput[2]);
        }
        break;

      case SCORING:
        if (Integer.parseInt(tokens[0]) != messageId || !theInput[0].equals("SCORE_CHOICE")
            || tokens.length != 2) {
          theOutput[0] = "SCORE_CHOICE_INVALID: ";
          System.out.println("Not choosing score type...");
          break;
        }
        int[] scores = curPlayer.getScores();
        if (!SLOT_MAP.containsKey(tokens[1].toLowerCase())) {
          theOutput[0] = "SCORE_CHOICE_INVALID: ";
          System.out.println(tokens[1] + " is not a valid score type...");
          break;
        }
        int type = SLOT_MAP.get(tokens[1].toLowerCase());
        if (scores[type - 1] != -1) {
          theOutput[0] = "SCORE_CHOICE_INVALID: ";
          System.out.println("Score type already chosen...");
          break;
        }
        state = WAITINGTOSTART;
        curPlayer.setScores(type, dices);
        System.out.println("Type of score chosen: " + tokens[1]);
        theOutput[2] = "";
        theOutput[0] = "SCORE_CHOICE_VALID: ";
        theOutput[1] = curPlayer.getScoreCard();
        System.out.println("Player" + curPlayer.getId() + "'s score: " + theOutput[1]);
        System.out.println("Waiting for new turn...");
        break;
      default:
        break;
    }
    return Arrays.copyOf(theOutput, theOutput.length);
  }

  /**
   * Get the current game state for this turn.
   * @return the current turn game state.
   */
  public String getGameState() {
    StringBuilder gameState = new StringBuilder();
    gameState.append("Num Rolls so far: " + rollNum + "\n");
    gameState.append("Current Dice Values: " + dices[0] + " " + dices[1] + " " + dices[2] + " "
        + dices[3] + " " + dices[4]);
    return gameState.toString();
  }

}
