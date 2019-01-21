package edu.neu.cs5010.yahtzee.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * a client application that communicates with user on command line.
 * passes information between user and server.
 */
public class YahtzeeClient {
  /**
   * reads response from the server and passes the result to user.
   * formats information from user and sends to server.
   */
  public static void main(String[] args) throws IOException {
    String hostName = args[0];
    int portNumber = Integer.parseInt(args[1]);

    try (
        Socket socket = new Socket(hostName, portNumber);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
            socket.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader input = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), "UTF-8"))
    ) {
      BufferedReader stdIn = new BufferedReader(
          new InputStreamReader(System.in, "UTF-8"));
      String fromServer;
      String fromUser;
      int messageId = 0;

      outerLoop:
      while (true) {
        if (stdIn.ready()) {
          fromUser = stdIn.readLine();
          System.out.println("YahtzeeClient: " + fromUser);
          if (fromUser != null && fromUser.toLowerCase().contains("quit")) {
            out.println("QUIT_GAME: " + messageId++);
            socket.setSoTimeout(0);
            input.readLine();
            System.out.println("FromServer: Waiting for round to finish before quiting.");
          } else if (fromUser != null && fromUser.toLowerCase().contains("print")
              && fromUser.toLowerCase().contains("game")
              && fromUser.toLowerCase().contains("state")) {
            out.println("PRINT_GAME_STATE: " + messageId++);
            socket.setSoTimeout(0);
            fromServer = input.readLine();
            System.out.println("FromServer: " + fromServer);
          }
        }
        try {
          socket.setSoTimeout(200);
          fromServer = input.readLine();
        } catch (SocketTimeoutException s) {
          fromServer = null;
        }
        if (fromServer != null) {
          System.out.println("FromServer: " + fromServer);
          String[] tokens = fromServer.split("\\s+");
          switch (tokens[0].substring(0, tokens[0].length() - 1)) {
            case "GAME_OVER" :
              System.out.println("Games over, here are the final scores: ");
              for (int i = 2; i < tokens.length; i++) {
                System.out.print(tokens[i] + " ");
              }
              out.println("ACK: " + tokens[1] + " Games is over");
              break outerLoop;
            case "START_GAME" :
              out.println("ACK: " + tokens[1] + " Games starts");
              break;
            case "START_ROUND" :
              out.println("ACK: " + tokens[1] + " Round started");
              break;
            case "START_TURN" :
              out.println("ACK: " + tokens[1] + " Turn started");
              break;
            case "CHOOSE_DICE" :
              System.out.println("Here is the dice result: ");
              for (int i = 2; i < 7; i++) {
                System.out.print(tokens[i] + " ");
              }
              System.out.println("\nEnter 1 for the dice you want to keep, 0 for re-rolling.\n"
                  + "(Please separate with space, number of dices is 5.)");
              fromUser = stdIn.readLine();
              while (fromUser != null && !checkKeepDice(fromUser)) {
                System.out.println("\nTry again! \nEnter 1 for the dice you want to keep, "
                    + "0 for re-rolling.\n"
                    + "(Please separate with space, number of dices is 5.)");
                fromUser = stdIn.readLine();
              }
              out.println("KEEP_DICE: " + tokens[1] + " " + fromUser);
              break;
            case "INVALID_DICE_CHOICE" :
              System.out.println("Please make sure your input only includes 1s and 0s");
              fromUser = stdIn.readLine();
              out.println("KEEP_DICE: " + tokens[1] + " " + fromUser);
              break;
            case "CHOOSE_SCORE" :
              System.out.println("Please choose the Score you want to take: ");
              for (int i = 7; i < tokens.length; i++) {
                System.out.print(tokens[i] + " ");
              }
              System.out.println();
              fromUser = stdIn.readLine();
              out.println("SCORE_CHOICE: " + tokens[1] + " " + fromUser);
              break;
            case "SCORE_CHOICE_INVALID" :
              System.out.println("Try again! Please make sure you only select from "
                  + "the choices provided: ");
              for (int i = 7; i < tokens.length; i++) {
                System.out.print(tokens[i] + " ");
              }
              System.out.println();
              fromUser = stdIn.readLine();
              out.println("SCORE_CHOICE: " + tokens[1] + " " + fromUser);
              break;
            case "SCORE_CHOICE_VALID" :
              System.out.println("Here is your current total score: " + tokens[tokens.length - 1]);
              out.println("ACK: " + tokens[1] + " Score presented to user");
              break;
            case "TURN_OVER" :
              System.out.println("Your turn is over");
              out.println("ACK: " + tokens[1] + " Turn is over");
              break;
            case "ROUND_OVER" :
              System.out.println("Round " + tokens[2] + " is over");
              out.println("ACK: " + tokens[1] + " Round is over");
              break;
            case "ACK" :
              break;
            case "INFO" :
              out.println("ACK: " + tokens[1] + " Information received");
              break;
            case "GAME_STATE" :
              System.out.println("Here is the current game state: ");
              System.out.print(tokens[2]);
              out.println("ACK: " + tokens[1] + " Game state received");
              break;
            default: break;
          }
        }
      }
      socket.close();
      input.close();
      out.close();
      stdIn.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * checks if the user input for keep dice meets requirement.
   * @param input input from user.
   * @return boolean value of whether the input meets requirement or not.
   */
  public static boolean checkKeepDice(String input) {
    String[] numbers = input.split("\\s+");
    if (numbers.length != 5) {
      return false;
    }
    for (String num: numbers) {
      if (num.charAt(0) - '0' != 0 && num.charAt(0) - '1' != 0) {
        return false;
      }
    }
    return true;
  }
}

