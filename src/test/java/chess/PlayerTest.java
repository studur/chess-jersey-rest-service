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
      Player player4 = new Player("Jimmy", "Doe", 1700);

      List<Player> roster = new ArrayList<>();
      roster.add(player1);
      roster.add(player2);
      roster.add(player3);

      roster.sort(Player::compareTo);

      assertTrue(roster.get(0) == player2);
      assertTrue(roster.get(1) == player3);
      assertTrue(roster.get(2) == player1);

      assertEquals(0, player3.compareTo(player4));
      assertEquals(1, player3.compareTo(player2));
      assertEquals(-1, player3.compareTo(player1));

   }

   @Test
   public void nameGettersAndSetters() {
      Player player1 =  new Player("John", "Doe", 1800);
      assertEquals("John Doe", player1.getFullName());

      player1.setFirstName("jim");
      assertEquals("jim", player1.getFirstName());

      player1.setLastName("jones");
      assertEquals("jones", player1.getLastName());

      player1.setFullName("jim jones");
      assertEquals("jim jones", player1.getFullName());
   }

   @Test
   public void scoreGettersAndSetters() {
      Player player1 =  new Player("John", "Doe", 1800);

      player1.setLosses(10);
      player1.setTies(20);
      player1.setWins(30);

      assertEquals(10, player1.getLosses());
      assertEquals(20, player1.getTies());
      assertEquals(30, player1.getWins());
      assertEquals(0, player1.getScore(), 0.0001);

      player1.addLoss();
      player1.addTie();
      player1.addWin();

      assertEquals(11, player1.getLosses());
      assertEquals(21, player1.getTies());
      assertEquals(31, player1.getWins());
      assertEquals(1.5, player1.getScore(), 0.0001);

      player1.setScore(50.0);
      assertEquals(50, player1.getScore(), 0.0001);

   }

   @Test
   public void toStringTest() {
      Player player1 =  new Player("John", "Doe", 1800);
      assertEquals("Name=John Doe, rating=1800.0", player1.toString());

      player1.setRating(1900.0);
      assertEquals("Name=John Doe, rating=1900.0", player1.toString());

      player1.setFullName("Magnus Carlsen");
      player1.setRating(2900.0);
      assertEquals("Name=Magnus Carlsen, rating=2900.0", player1.toString());
   }
}