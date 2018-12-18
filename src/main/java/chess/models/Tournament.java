package chess.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * This class is responsible for representing a chess tournament composed of a list of Player and Game.
 */
public class Tournament {

   private static final int MIN_NUM_GAMES_PLAYED_FOR_PERMANENT_RATING = 8;

   private List<Player> players = new ArrayList<>();

   private List<Round> rounds = new ArrayList<>();

   private double[][] resultMatrix;

   private Player[] playersStanding;

   /**
    * Single constructor.
    *
    * @param players List of players supplied to initialize the {@link Tournament} instance.
    */
   public Tournament(List<Player> players) {
      this.players = players;
      this.resultMatrix = new double[players.size()][players.size()];
      this.playersStanding = new Player[players.size()];
   }

   /**
    * Get the square matrix of the players containing the results. The two dimensions of the matrix is represented
    * by the same list of players where the coordinate of a cell contains the cumulated result of all the games
    * played by these two players in a single tournament.
    *
    * @return The matrix or the players and the games containing the results.
    */
   public double[][] getResultMatrix() {
      double[][] copy = new double[resultMatrix.length][resultMatrix[0].length];
      System.arraycopy(resultMatrix, 0, copy, 0, resultMatrix.length);
      return copy;
   }

   /**
    * Getter which returns a defensive copy of the player standings array.
    *
    * @return The player standing array.
    */
   public Player[] getPlayersStanding() {
      return Arrays.copyOf(playersStanding, playersStanding.length);
   }

   /**
    * Adds a {@link Round} instance to the current {@link Tournament} rounds list.
    *
    * @param round The {@link Round} instance to add.
    */
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
   public void computeTournamentRatings() {

      // first compute the ratings of unrated players.
      List<Player> unratedPlayers = getNewPlayers();
      if (unratedPlayers.size() > 0) {
         unratedPlayers.forEach(this::computeRatingForNewPlayer);
      }

      // Then compute ratings for players with temporary rating.
      List<Player> playersWithTemporaryRating = getPlayersWithTemporaryRating();
      // But before, remove the unrated players processed in the previous step.
      for (Player player : unratedPlayers) {
         playersWithTemporaryRating.remove(player);
      }

      if (playersWithTemporaryRating.size() > 0) {
         playersWithTemporaryRating.forEach(this::computeRatingForPlayerWithTemporaryRating);
      }

      // Add all the game results to the result matrix.
      for (Round round : rounds) {
         for (Game game : round.getGames()) {
            addResult(game);
         }
      }

      // Generate a list of remaining players to be processed.
      List<Player> remainingPlayers = new ArrayList<>();
      for (Player player: players){
         if (!unratedPlayers.contains(player) && !playersWithTemporaryRating.contains(player)) {
            remainingPlayers.add(player);
         }
      }

      // Compute rating for all remaining players.
      for (Player player : remainingPlayers) {
         double newRating = player.getRating();
         for (int i = 0; i < remainingPlayers.size(); i++) {
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
      insertionSortOnScore(playersStanding);
   }

   /**
    * Method used to print the Tournament report to the console.
    */
   public void printTournamentReport() {
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
    * Method used to print the {@link Tournament} report to a semi-colon separated CSV file.
    *
    * @param fileName Name of the CSV file generated.
    * @throws IOException Thrown if IO problems with file generation.
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
    * Sorts an array of {@link Player} using a insertion sort algorithm based on their score.
    *
    * @param players Array of {@link Player}
    */
   private static void insertionSortOnScore(Player[] players) {
      //TODO Add a step to sort equal score on their tournament performance rating.
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

   /**
    * Get the list of {@link Player} without a rating.
    *
    * @return List of {@link Player}.
    */
   private List<Player> getNewPlayers() {
      return players.stream()
            .filter(p -> p.getRating() == 0)
            .collect(Collectors.toList());
   }

   private List<Player> getPlayersWithTemporaryRating() {
      return players.stream()
            .filter(p -> p.getRating() > 0)
            .filter(p -> p.getUnratedGamesPlayed() > 0)
            .filter(p -> p.getUnratedGamesPlayed() < MIN_NUM_GAMES_PLAYED_FOR_PERMANENT_RATING)
            .collect(Collectors.toList());
   }

   /**
    * Calculates the rating of a new player without a rating. This step is usualy executed before all other
    * rating calculation steps in a tournament.
    *
    * @param player {@link Player} for which the rating is computed.
    */
   public void computeRatingForNewPlayer(Player player) {

      int[] playerPerformanceData = computePerformanceRating(player);
      int newRating = playerPerformanceData[0];
      int totalGames = playerPerformanceData[1];

      // Adjust the rating for a new player if the rating is below 1200.
      newRating = compensateLowRating(newRating);

      player.setRating(newRating);
      player.setUnratedGamesPlayed(totalGames);
      player.setRatingPermanent(false);

   }

   /**
    * Compensates a rating calculated for a new player if the rating is below 1200. This adjustment
    * is only applicable for a new rating calculation and not for a temporary rating calculation.
    *
    * @param newRating The new player rating value to be compensated if below 1200.
    * @return The compensated rating value.
    */
   public static int compensateLowRating(int newRating) {
      if (newRating < 1200) {
         newRating += (int) ((0.5 * (1200 - newRating)));
      }
      return newRating;
   }

   /**
    * Calculates the performance rating of a {@link Player} but also returns the number of games played
    * to be used subsequently.
    *
    * @param player {@link Player} for which the rating is computed.
    * @return An array containing the rating value [0] and the number of games played [1].
    */
   public int[] computePerformanceRating(Player player) {
      int average = 0;
      int victories = 0;
      int totalGames = 0;


      for (Round round : rounds) {
         for (Game game : round.getGames()) {
            if (game.player1 == player) {
               average += game.player2.getRating();
               victories += game.result;
               totalGames++;
            }

            if (game.player2 == player) {
               average += game.player1.getRating();
               if (game.result == 0) {
                  victories += 1;
               } else if (game.result == 0.5) {
                  victories += game.result;
               }
               totalGames++;
            }
         }
      }
      average = average / totalGames;
      int losses = totalGames - victories;

      double gamemod = (double) (victories - losses) / (double) totalGames;
      int modifier = (int) (400. * gamemod);
      int newRating = average + modifier;

      return new int[]{newRating, totalGames};
   }

   /**
    * Computed the rating for a player without a permanent rating based on the current {@link Tournament}
    * performance rating and the previous rating value ponderated on the games played.
    *
    * @param player The {@link Player} which has a temporary rating value.
    */
   public void computeRatingForPlayerWithTemporaryRating(Player player) {

      int[] playerPerformanceData = computePerformanceRating(player);
      int newRating = playerPerformanceData[0];
      int totalGames = playerPerformanceData[1];

      // compute new compounded rating
      int totalGamePlayedEver = totalGames + player.getUnratedGamesPlayed();
      double newCompoundRating = (newRating * totalGames + player.getUnratedGamesPlayed() * player.getRating()) / totalGamePlayedEver;

      player.setRating(newCompoundRating);
      player.setUnratedGamesPlayed(totalGamePlayedEver);
      player.setRatingPermanent(false);

   }

}
