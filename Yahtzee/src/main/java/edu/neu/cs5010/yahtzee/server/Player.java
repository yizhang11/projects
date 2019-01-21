package edu.neu.cs5010.yahtzee.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * contains information about player.
 * gets message from player with caution.
 * sends message to player.
 */
public class Player {
  private Socket socket;
  private BufferedReader input;
  private PrintWriter output;
  private int playId;
  private GameScore scoreCard;

  /**
   * Construct a new player.
   * @param socket the socket to connect this player.
   * @param playerId the player's id.
   */
  public Player(Socket socket, int playerId) {
    this.playId = playerId;
    this.socket = socket;
    try {
      output = new PrintWriter(new OutputStreamWriter(
          socket.getOutputStream(), StandardCharsets.UTF_8), true);
      input = new BufferedReader(
          new InputStreamReader(socket.getInputStream(), "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.scoreCard = new GameScore(playerId);
  }

  /**
   * Get message from player with caution.
   * Check for player's input every 0.2 seconds for quit or print game state.
   * Check whether player is disconnected.
   * @return the input string if there is one, or null if no input, or not connected.
   */
  public String getMessage() {
    String inputMessage = null;
    try {
      socket.setSoTimeout(200);
      inputMessage = input.readLine();
    } catch (SocketTimeoutException s) {
      return "";
    } catch (SocketException s) {
      return "Player not connected";
    } catch (IOException e) {
      e.printStackTrace();
    }
    return inputMessage;
  }

  public void sendMessage(String message) {
    output.println(message);
  }

  /**
   * Gracefully disconnect this player.
   */
  public void close() {
    try {
      input.close();
      output.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int getId() {
    return playId;
  }

  public GameScore getGameScore() {
    return scoreCard;
  }
}
