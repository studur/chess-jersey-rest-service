package chess;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import chess.models.Player;


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
   public List<Player> getIt() {

      List<Player> players = new ArrayList<>();

      players.add(new Player("Jimmy", "Forest", 1783));
      players.add(new Player("Sylvain", "Mireault", 1711));
      players.add(new Player("Roger", "Gendron", 1607));
      players.add(new Player("Guillaume", "Levebvre", 1551));

      return players;
   }

   @Path("/test")
   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String getTournamentResults() {
      return "OK";
   }
}
