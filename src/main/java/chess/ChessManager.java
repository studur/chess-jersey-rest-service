package chess;

import java.util.ArrayList;
import java.util.List;

import chess.models.Player;
import chess.models.Round;
import chess.models.Tournament;

public class ChessManager {

   public static void main(String[] args) {


      Player player1 = new Player("Jimmy", "Forest", 1756);
      Player player2 = new Player("Carl", "Bergeron", 1561);
      Player player3 = new Player("Robert", "Fortin", 1424);
      Player player4 = new Player("Etienne", "Lavoie", 1405);
      Player player5 = new Player("Robert", "Blanchard", 1236);
      Player player6 = new Player("Louis", "Bergeron", 1209);
      Player player7 = new Player("Louis", "Poirier", 1052,6);
      Player player8 = new Player("Richard", "Marquis", 1171,3);

      List<Player> players = new ArrayList<>();

      players.add(player1);
      players.add(player2);
      players.add(player3);
      players.add(player4);
      players.add(player5);
      players.add(player6);
      players.add(player7);
      players.add(player8);

      Tournament tournoi = new Tournament(players);


      // round 1

      Round round1 = new Round();

      round1.addGame(player1, player5, 1);
      round1.addGame(player2, player6, 1);
      round1.addGame(player3, player7, 1);
      round1.addGame(player4, player8, 1);

      tournoi.addRound(round1);

      // round 2

      Round round2 = new Round();

      round2.addGame(player1, player4, 1);
      round2.addGame(player2, player3, 0);
      round2.addGame(player5, player8, 1);
      round2.addGame(player6, player7, 1);


      tournoi.addRound(round2);

      //round 3

      Round round3 = new Round();

      round3.addGame(player1, player3, 1);
      round3.addGame(player2, player4, 1);
      round3.addGame(player5, player6, 1);
      round3.addGame(player7, player8, 0);

      tournoi.addRound(round3);

      tournoi.computeTournamentRatings();
      tournoi.printTournamentReport();

      try {
         String filename = "result-13";
         tournoi.printTournamentReportToCsvFile(filename);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
}
