package chess;

import java.util.List;

import org.junit.Test;

import chess.models.Game;
import chess.models.Player;
import chess.models.Round;

import static org.junit.Assert.assertEquals;

public class RoundTest {


   @Test
   public void roundTest() {
      Round round1 = new Round();
      assertEquals(0, round1.getSize());
      Player player1 = new Player("John","Doe", 1800);
      Player player2 = new Player("Jane","Doe", 1900);

      round1.addGame(new Game(player1, player2, 1));
      assertEquals(1, round1.getSize());

      round1.addGame(player1, player2, 0);
      assertEquals(2, round1.getSize());

      List<Game> games = round1.getGames();
      assertEquals(2, games.size());

      Round round2 = new Round(games);
      assertEquals(2, round2.getSize());

   }

}
