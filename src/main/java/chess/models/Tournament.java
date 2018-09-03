package chess.models;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


/**
 * This class is responsible for representing a chess tournament composed of a list of Player and Game.
 */
public class Tournament {


   private List<Player> players = new ArrayList<>();

   private List<Round> rounds = new ArrayList<>();

   private double[][] resultMatrix;

   private Player[] playersStanding;

   public Tournament(List<Player> players) {
      this.players = players;
      this.resultMatrix = new double[players.size()][players.size()];
      this.playersStanding = new Player[players.size()];
   }

   public double[][] getResultMatrix() {
      double[][] copy = new double[resultMatrix.length][resultMatrix[0].length];
      System.arraycopy(resultMatrix, 0, copy, 0, resultMatrix.length);
      return copy;
   }

   public Player[] getPlayersStanding() {
      return Arrays.copyOf(playersStanding, playersStanding.length);
   }


   public void addRound(Round round) {
      rounds.add(round);
   }

   /**
    * Method used to add a game result to each player metrics and the computed rating adjustments for the
    * respective players in the resultMatrix of the tournament.
    *
    * @param game The added game.
    */
   public void addResult(Game game) {
      int coordPlayer1 = players.indexOf(game.player1);
      int coordPlayer2 = players.indexOf(game.player2);
      double result2 = game.result;

      if (game.result == 1) {
         result2 = 0;
         game.player1.setWins(game.player1.getWins() + 1);
         game.player2.setLosses(game.player2.getLosses() + 1);
         game.player1.setScore(game.player1.getScore() + 1);

      } else if (game.result == 0) {
         game.player1.setLosses(game.player1.getLosses() + 1);
         game.player2.setWins(game.player2.getWins() + 1);
         game.player2.setScore(game.player2.getScore() + 1);
         result2 = 1;

      } else {
         game.player1.setTies(game.player1.getTies() + 1);
         game.player2.setTies(game.player2.getTies() + 1);
         game.player1.setScore(game.player1.getScore() + 0.5);
         game.player2.setScore(game.player2.getScore() + 0.5);
      }

      resultMatrix[coordPlayer1][coordPlayer2] += Game.getDeltaFromGame(game.player1, game.player2, game.result);
      resultMatrix[coordPlayer2][coordPlayer1] += Game.getDeltaFromGame(game.player2, game.player1, result2);
   }

   /**
    * Method used to aggregate all the rating adjustments for each player and compute the final rating after
    * a completed tournament. The bonus is also calculated based on the number of rounds played.
    */
   public void computeNewRatings() {

      // first compute the ratings of unrated players


      for (Round round : rounds) {
         for (Game game : round.getGames()) {
            if (game.player1.getRating() == 0 || game.player2.getRating() == 0) {

            }

            addResult(game);
         }
      }

      for (Player player : players) {
         double newRating = player.getRating();
         for (int i = 0; i < players.size(); i++) {
            newRating += resultMatrix[players.indexOf(player)][i];
         }

         // Calculate bonus if 4 rounds or more were played

         player.setOldRating(player.getRating());
         double delta = newRating - player.getOldRating();
         double bonus = 0;
         if (rounds.size() > 3) {
            bonus = delta - (double) (24 + 2 * (rounds.size() - 4));
            if (bonus < 0) {
               bonus = 0;
            }
         }
         player.setRating(newRating + bonus);
      }
      computePlayerStanding();
   }

   /**
    * Method used to compute the standing of the player and sort the list.
    */
   private void computePlayerStanding() {
      IntStream.range(0, players.size()).forEach(i -> playersStanding[i] = players.get(i));
      insertionsortOnScore(playersStanding);
   }

   void printTournamentReport() {
      System.out.println("*********************");
      System.out.println("* Tournament report *");
      System.out.println("*********************");
      for (Player aPlayersStanding : playersStanding) {
         System.out.print(aPlayersStanding.getFullName() + " : ");
         System.out.print(aPlayersStanding.getWins() + " wins ");
         System.out.print(aPlayersStanding.getLosses() + " losses ");
         System.out.print(aPlayersStanding.getTies() + " ties ");
         System.out.print("New rating is " + new DecimalFormat("##").format(aPlayersStanding.getRating()));
         System.out.println();
      }
   }

   /**
    * @param fileName
    * @throws IOException
    */
   public void printTournamentReportToCsvFile(String fileName) throws IOException {

      try (BufferedWriter bufferedWriter = new BufferedWriter(
            new FileWriter("./" + fileName + ".csv"))) {
         bufferedWriter.write("Nom;Ancienne cote;Gains;Nulles;Pertes;Nouvelle cote");
         bufferedWriter.newLine();
         for (Player aPlayersStanding : playersStanding) {
            bufferedWriter.write(aPlayersStanding.getFullName() + ";");
            bufferedWriter.write(new DecimalFormat("##").format(aPlayersStanding.getOldRating()) + ";");
            bufferedWriter.write(aPlayersStanding.getWins() + ";");
            bufferedWriter.write(aPlayersStanding.getTies() + ";");
            bufferedWriter.write(aPlayersStanding.getLosses() + ";");
            bufferedWriter.write(new DecimalFormat("##").format(aPlayersStanding.getRating()));
            bufferedWriter.newLine();
         }
      }
   }

   /**
    * @param players
    */
   private static void insertionsortOnScore(Player[] players) {
      int length = players.length;
      for (int playerNumber = 1; playerNumber < length; playerNumber++) {
         Player key = players[playerNumber];
         int otherPlayerNumber = playerNumber - 1;
         while ((otherPlayerNumber >= 0) && (players[otherPlayerNumber].getScore() < key.getScore())) {
            players[otherPlayerNumber + 1] = players[otherPlayerNumber];
            otherPlayerNumber--;
         }
         players[otherPlayerNumber + 1] = key;
      }
   }

}
