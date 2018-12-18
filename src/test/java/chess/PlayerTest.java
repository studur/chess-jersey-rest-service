package chess;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import chess.models.Player;

import static org.junit.Assert.*;

public class PlayerTest {


   @Test
   public void compareTo() throws Exception {
      Player player1 = new Player("John", "Doe", 1800);
      Player player2 = new Player("Jane", "Doe", 1500);
      Player player3 = new Player("Jim", "Doe", 1700);

      List<Player> roster = new ArrayList<>();
      roster.add(player1);
      roster.add(player2);
      roster.add(player3);

      roster.sort(Player::compareTo);

      assertTrue(roster.get(0) == player2);
      assertTrue(roster.get(1) == player3);
      assertTrue(roster.get(2) == player1);

   }

}