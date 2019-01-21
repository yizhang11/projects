package edu.neu.cs5010.yahtzee.server;


import org.junit.Before;
import org.junit.Test;

public class YahtzeeProtocolTest {
  private YahtzeeProtocol protocol;
  private GameScore gameScore;

  @Before
  public void setUp() throws Exception {
    this.protocol = new YahtzeeProtocol();
    this.gameScore = new GameScore(2);
  }

  @Test
  public void processInput() {
    String[] start = new String[]{"Ack", "13"};
    this.protocol.processInput(start, 13, this.gameScore);

    String[] chooseDice0 = new String[]{"KEEP_DICE", "13 0 0 1 0 0"};
    this.protocol.processInput(chooseDice0, 14, this.gameScore);
    String[] chooseDice1 = new String[]{"KE_DICE", "13 0 0 1 0 0"};
    this.protocol.processInput(chooseDice1, 13, this.gameScore);
    String[] chooseDice2 = new String[]{"KEEP_DICE", "13 0 0 1 0 0 0"};
    this.protocol.processInput(chooseDice2, 13, this.gameScore);
    String[] chooseDice3 = new String[]{"KEEP_DICE", "13 0 2 1 0 0"};
    this.protocol.processInput(chooseDice3, 13, this.gameScore);
    String[] chooseDice = new String[]{"KEEP_DICE", "13 0 0 1 0 0"};
    this.protocol.processInput(chooseDice, 13, this.gameScore);

    String[] keepDice = new String[]{"KEEP_DICE", "13 1 1 1 1 1"};
    this.protocol.processInput(keepDice, 13, this.gameScore);

    String[] chance0 = new String[]{"SCORE_CHOICE", "13 CHANCE"};
    this.protocol.processInput(chance0, 14, this.gameScore);
    String[] chance1 = new String[]{"SCE_CHOICE", "13 CHANCE"};
    this.protocol.processInput(chance1, 13, this.gameScore);
    String[] chance2 = new String[]{"SCORE_CHOICE", "13 CHANCE Fullhouse"};
    this.protocol.processInput(chance2, 13, this.gameScore);
    String[] chance3 = new String[]{"SCORE_CHOICE", "13 blast"};
    this.protocol.processInput(chance3, 13, this.gameScore);
    String[] chance = new String[]{"SCORE_CHOICE", "13 CHANCE"};
    this.protocol.processInput(chance, 13, this.gameScore);

    String[] start1 = new String[]{"Ack", "14"};
    this.protocol.processInput(start1, 14, this.gameScore);
    System.out.println(this.protocol.getGameState());

    String[] chooseDicer1 = new String[]{"KEEP_DICE", "14 0 0 0 0 0"};
    this.protocol.processInput(chooseDicer1, 14, this.gameScore);
    String[] chooseDicer2 = new String[]{"KEEP_DICE", "14 0 0 0 0 0"};
    this.protocol.processInput(chooseDicer2, 14, this.gameScore);

    String[] chanceAgain = new String[]{"SCORE_CHOICE", "13 CHANCE"};
    this.protocol.processInput(chanceAgain, 13, this.gameScore);

    System.out.println(this.protocol.getGameState());
  }


}