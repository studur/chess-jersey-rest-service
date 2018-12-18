package chess;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import chess.models.Game;
import chess.models.Player;
import chess.models.Round;
import chess.models.Tournament;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TournamentTest {

   private static Tournament tournament;
   private static Player player1;
   private static Player player2;
   private static Game game1;

   @Before
   public void setUp() {
      player1 = new Player("John", "Doe", 1500);
      player2 = new Player("Jane", "Doe", 1800);

      List<Player> players = new ArrayList<>();
      players.add(player1);
      players.add(player2);
      tournament = new Tournament(players);

      game1 = new Game(player1, player2, 0);
      Round round1 = new Round();
      round1.addGame(game1);
      tournament.addResult(game1);
   }

   @Test
   public void getResultMatrix() {
      double[][] result = tournament.getResultMatrix();
      double[][] expected = new double[][]{{0, -4.800000000000001}, {4.800000000000001, 0}};
      assertArrayEquals(expected, result);
   }

   @Test
   public void addResult() throws Exception {
      assertEquals(0, player1.getWins());
      assertEquals(1, player1.getLosses());
      assertEquals(1, player2.getWins());
      assertEquals(0, player2.getLosses());
      assertEquals(0, player1.getScore(), 0.0);
      assertEquals(1, player2.getScore(), 0.0);
   }

   @Test
   public void testCompensationOfLowRatingForNewPlayer() {
      assertEquals(1000, Tournament.compensateLowRating(800));
      assertEquals(1100, Tournament.compensateLowRating(1000));
      assertEquals(1200, Tournament.compensateLowRating(1200));
      assertEquals(1300, Tournament.compensateLowRating(1300));
   }

   @Test
   public void computeNewRatings() throws Exception {
      tournament.computeTournamentRatings();
      assertEquals(1500, player1.getOldRating(), 0.0);
      assertEquals(1800, player2.getOldRating(), 0.0);
      assertEquals(1495.2, player1.getRating(), 0.0);
      assertEquals(1804.8, player2.getRating(), 0.0);
   }

   @Test
   public void computePlayerStanding() throws Exception {

      Player[] expectedBefore = new Player[]{null, null};
      Player[] expectedAfter = new Player[]{player2, player1};

      assertArrayEquals(expectedBefore, tournament.getPlayersStanding());

      tournament.computeTournamentRatings();
      assertArrayEquals(expectedAfter, tournament.getPlayersStanding());
   }


   @Test
   public void computeNewPlayerRating() throws Exception {

      Player player1 = new Player("Jean", "Morissette", 1888);
      Player player2 = new Player("Jimmy", "Forest", 1755);
      Player player3 = new Player("Roger", "Gendron", 1542);
      Player player4 = new Player("Robert", "Fortin", 1400);
      Player player5 = new Player("Etienne", "Lavoie", 1382);
      Player player6 = new Player("Yves", "Gosselin", 1215);
      Player player7 = new Player("Louis", "Bergeron", 1206);
      Player player8 = new Player("Robert", "Blanchard", 1216);
      Player player9 = new Player("Louis", "Poirier", 0);
      Player player10 = new Player("Michael", "Regnier", 1502, 3);
      Player player11 = new Player("Alex", "Provencher", 1228, 3);
      Player player12 = new Player("Sylvain", "Mireault", 1687);


      List<Player> players = new ArrayList<>();

      players.add(player1);
      players.add(player2);
      players.add(player3);
      players.add(player4);
      players.add(player5);
      players.add(player6);
      players.add(player7);
      players.add(player8);
      players.add(player9);
      players.add(player10);
      players.add(player11);
      players.add(player12);

      Tournament tournoi = new Tournament(players);


      // round 1
      Game game1 = new Game(player1, player4, 1);
      Game game2 = new Game(player2, player5, 1);
      Game game3 = new Game(player3, player6, 1);
      Game game4 = new Game(player7, player9, 1);
      Game game5 = new Game(player8, player10, 1);
      Game game6 = new Game(player11, player12, 0);

      Round round1 = new Round();
      round1.addGame(game1);
      round1.addGame(game2);
      round1.addGame(game3);
      round1.addGame(game4);
      round1.addGame(game5);
      round1.addGame(game6);

      tournoi.addRound(round1);

      // round 2

      Game game7 = new Game(player1, player8, 1);
      Game game8 = new Game(player2, player7, 1);
      Game game9 = new Game(player3, player12, 0);
      Game game10 = new Game(player4, player10, 1);
      Game game11 = new Game(player5, player6, 1);
      Game game12 = new Game(player9, player11, 0);

      Round round2 = new Round();

      round2.addGame(game7);
      round2.addGame(game8);
      round2.addGame(game9);
      round2.addGame(game10);
      round2.addGame(game11);
      round2.addGame(game12);


      tournoi.addRound(round2);

      //round 3

      Game game13 = new Game(player1, player12, 0);
      Game game14 = new Game(player2, player3, 1);
      Game game15 = new Game(player4, player11, 1);
      Game game16 = new Game(player5, player7, 1);
      Game game17 = new Game(player6, player8, 0);
      Game game18 = new Game(player9, player10, 1);


      Round round3 = new Round();

      round3.addGame(game13);
      round3.addGame(game14);
      round3.addGame(game15);
      round3.addGame(game16);
      round3.addGame(game17);
      round3.addGame(game18);

      tournoi.addRound(round3);
      tournoi.computeRatingForNewPlayer(player9);

      assertEquals((int) player9.getRating(), 1189);
      assertEquals(player9.getUnratedGamesPlayed(), 3);

      tournoi.computeRatingForPlayerWithTemporaryRating(player10);
      assertEquals((int) player10.getRating(), 1185);
      assertEquals(player10.getUnratedGamesPlayed(), 6);


      tournoi.computeRatingForPlayerWithTemporaryRating(player11);
      assertEquals((int) player11.getRating(), 1260);
      assertEquals(player11.getUnratedGamesPlayed(), 6);
   }

}