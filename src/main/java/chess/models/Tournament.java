package chess.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import chess.utils.TournamentReport;


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
      double resultForPlayer1 = game.result;
      double resultForPlayer2 = game.result;

      if (resultForPlayer1 == 1) {
         resultForPlayer2 = 0;
         game.player1.addWin();
         game.player2.addLoss();
      } else if (resultForPlayer1 == 0) {
         resultForPlayer2 = 1;
         game.player1.addLoss();
         game.player2.addWin();
      } else {
         game.player1.addTie();
         game.player2.addTie();
      }

      resultMatrix[coordPlayer1][coordPlayer2] += Game.getDeltaFromGame(game.player1, game.player2, resultForPlayer1);
      resultMatrix[coordPlayer2][coordPlayer1] += Game.getDeltaFromGame(game.player2, game.player1, resultForPlayer2);
   }

   /**
    * Method used to aggregate all the rating adjustments for each player and compute the final rating after
    * a completed tournament. The bonus is also calculated based on the number of rounds played.
    */
   public void computeTournamentRatings() {

      List<Player> unratedPlayers = Player.getNewPlayers(players);
      List<Player> playersWithTemporaryRating = Player.getPlayersWithTemporaryRating(players);
      List<Player> permanentPlayers = Player.getPermanentPlayers(players);

      // first compute the ratings of unrated players.
      if (unratedPlayers.size() > 0) {
         unratedPlayers.forEach(this::computeRatingForNewPlayer);
      }

      // Then compute ratings for players with temporary rating.
      if (playersWithTemporaryRating.size() > 0) {
         playersWithTemporaryRating.forEach(this::computeRatingForPlayerWithTemporaryRating);
      }

      addGameResultsToResultMatrix();
      computeRatingForPermanentPlayers(permanentPlayers);
      playersStanding = computePlayerStanding();
   }

   private void addGameResultsToResultMatrix() {
      for (Round round : rounds) {
         for (Game game : round.getGames()) {
            addResult(game);
         }
      }
   }

   private void computeRatingForPermanentPlayers(List<Player> permanentPlayers) {
      for (Player player : permanentPlayers) {
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
   }

   /**
    * Method used to compute the standing of the player and sort the list.
    */
   private Player[] computePlayerStanding() {
      IntStream.range(0, players.size()).forEach(i -> playersStanding[i] = players.get(i));
      return Player.insertionSortOnScore(playersStanding);
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
      if (totalGamePlayedEver > MIN_NUM_GAMES_PLAYED_FOR_PERMANENT_RATING) {
         player.setUnratedGamesPlayed(0);
         player.setRatingPermanent(true);
      } else {
         player.setUnratedGamesPlayed(totalGamePlayedEver);
         player.setRatingPermanent(false);
      }
   }

}
