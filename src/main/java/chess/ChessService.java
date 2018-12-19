package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import chess.models.Game;
import chess.models.Player;
import chess.models.Round;
import chess.models.Tournament;


@Path("chess")
public class ChessService {

   /**
    * Method handling HTTP GET requests. The returned object will be sent
    * to the client as "text/plain" media type.
    *
    * @return String that will be returned as a text/plain response.
    */
   @Path("/players")
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Player[] getIt() {

      Player player1 = new Player("Jimmy", "Forest", 1783);
      Player player2 = new Player("Sylvain", "Mireault", 1711);
      Player player3 = new Player("Roger", "Gendron", 1607);
      Player player4 = new Player("Guillaume", "Levebvre", 1551);
      List<Player> players = new ArrayList<>();

      players.add(player1);
      players.add(player2);
      players.add(player3);
      players.add(player4);

      Tournament tournoi = new Tournament(players);

      // round 1
      Game game1 = new Game(player1, player3, 1);
      Game game2 = new Game(player2, player4, 0);

      Round round1 = new Round();
      round1.addGame(game1);
      round1.addGame(game2);
      tournoi.addRound(round1);

      // round 2
      Game game3 = new Game(player1, player4, 1);
      Game game4 = new Game(player2, player3, 1);
      Round round2 = new Round();


      round2.addGame(game3);
      round2.addGame(game4);
      tournoi.addRound(round2);

      //round 3
      Game game5 = new Game(player1, player2, 1);
      Game game6 = new Game(player3, player4, 0);

      Round round3 = new Round();

      round3.addGame(game5);
      round3.addGame(game6);

      tournoi.addRound(round3);

      tournoi.computeTournamentRatings();

      return tournoi.getPlayersStanding();
   }

   @Path("/test")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String getString() {
      return "Got it !";
   }
}
