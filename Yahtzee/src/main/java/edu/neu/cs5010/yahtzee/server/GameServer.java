package edu.neu.cs5010.yahtzee.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * this class receives information from client and responds with information.
 */
public class GameServer{

  /**
   * The whole controller of game.
   * Connects with client.
   * Controls game order.
   * Listens to client request.
   * @param args port number and game rounds.
   */
  public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);
    int roundNum = 13;
    if (args.length > 1 && args[1].equals("DEV")) {
      roundNum = 3;
    }
    int messageId = 1;
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println(InetAddress.getLocalHost());
      List<Player> players = new ArrayList<>();
      int playerId = 0;
      System.out.println("Waiting for players to join...");
      Socket socket;
      long start = System.currentTimeMillis();
      System.out.println("Wait for 30 seconds before starting the game...");
      while ((System.currentTimeMillis() - start) / 1000 < 30 && players.size() < 6) {
        try {
          serverSocket.setSoTimeout(1000);
          socket = serverSocket.accept();
        } catch (SocketTimeoutException s) {
          continue;
        }
        System.out.println(playerId + 1 + " player connected...");
        players.add(new Player(socket, playerId));
        players.get(playerId).sendMessage("INFO: " + messageId++ + " Joining the game.");
        System.out.println(players.get(playerId).getMessage());
        players.get(playerId).sendMessage("INFO: " + messageId++
            + " Waiting for more players to join.");
        System.out.println(players.get(playerId).getMessage());
        playerId++;
      }
      System.out.println("Got enough players, starting the game now.");
      for (Player player : players) {
        player.sendMessage("INFO: " + messageId++ + " Game Started. Waiting for my turn.");
        System.out.println(player.getMessage());
      }
      int roundStarted = 1;
      boolean noQuitter = true;
      while (roundStarted != roundNum + 1 && noQuitter) {
        System.out.println("Starting round " + roundStarted);
        for (Player player : players) {
          player.sendMessage("START_ROUND: " + messageId++ + " " + roundStarted);
          System.out.println(player.getMessage());
        }
        YahtzeeProtocol protocol = new YahtzeeProtocol();
        for (Player player : players) {
          player.sendMessage("START_TURN: " + messageId++);
          System.out.println("Starting turn for player" + player.getId());
          String playerInput;
          String[] theOutput;
          while (true) {
            while ((playerInput = player.getMessage()).equals("")) {
              for (Player otherPlayer : players) {
                if (otherPlayer.equals(player)) {
                  continue;
                }
                String otherPlayerInput = otherPlayer.getMessage();
                serverSocket.setSoTimeout(200);
                if (otherPlayerInput.equals("")) {
                  continue;
                }
                System.out.println(otherPlayerInput);
                String[] token = otherPlayerInput.split(":+\\s*");
                if (token[0].equals("QUIT_GAME")) {
                  noQuitter = false;
                  otherPlayer.sendMessage("ACK: " + token[1].split("\\s+")[0]);
                  System.out.println("Player" + otherPlayer.getId() + " wants to quit...");
                }
                if (token[0].equals("PRINT_GAME_STATE")) {
                  StringBuilder gameState = new StringBuilder();
                  gameState.append("\n******** YAHTZEE Game State *******\n");
                  gameState.append("Current Round: " + roundStarted + "\n");
                  gameState.append("Current Player: Player" + (player.getId() + 1) + "\n");
                  gameState.append(protocol.getGameState());
                  otherPlayer.sendMessage("GAME_STATE: " + -messageId + " "
                      + gameState.toString());
                  System.out.println(gameState.toString());
                }
                if (token[0].equals("Player not connected")) {
                  System.out.println(otherPlayer.getId() + " not connected.");
                  noQuitter = false;
                }
              }
            }
            System.out.println(playerInput);
            String[] tokens = playerInput.split(":+\\s*");

            if (tokens[0].equals("QUIT_GAME")) {
              noQuitter = false;
              player.sendMessage("ACK: " + tokens[1].split("\\s+")[0]);
              System.out.println("Player" + player.getId() + " wants to quit...");
            }
            theOutput = protocol.processInput(tokens, messageId - 1, player.getGameScore());
            if (theOutput[0].equals("SCORE_CHOICE_VALID: ")) {
              player.sendMessage(theOutput[0] + messageId++ + " " + theOutput[1]);
              break;
            }
            player.sendMessage(theOutput[0] + messageId++ +  " " + theOutput[1] + theOutput[2]);

          }
          player.sendMessage("TURN_OVER: "  + messageId++);
          String[] tokens = player.getMessage().split(":+\\s*");
          if (tokens[0].equals("QUIT_GAME")) {
            noQuitter = false;
            player.sendMessage("ACK: " + tokens[1].split("\\s+")[0]);
          }
        }
        for (Player player : players) {
          player.sendMessage("ROUND_OVER: " + messageId++ + " " + roundStarted);
          String playerMessage = player.getMessage();
          while (playerMessage.equals("")) {
            playerMessage = player.getMessage();
          }
          String[] tokens = playerMessage.split(":+\\s*");
          if (tokens[0].equals("QUIT_GAME")) {
            noQuitter = false;
            player.sendMessage("ACK: " + tokens[1].split("\\s+")[0]);
          }
        }
        roundStarted++;
      }
      StringBuilder finalScoresBuilder = new StringBuilder();
      for (Player player : players) {
        finalScoresBuilder.append(" Player" + (player.getId() + 1) + " "
            + player.getGameScore().getTotalScore());
      }
      String finalScores = finalScoresBuilder.toString();
      for (Player player : players) {
        player.sendMessage("GAME_OVER: " + messageId++  + finalScores);
        player.getMessage();
        player.close();
      }
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
