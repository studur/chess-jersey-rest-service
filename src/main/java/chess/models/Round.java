package chess.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Round {

   private final List<Game> games;

   public Round() {
      this.games = new ArrayList<>();
   }

   public Round(List<Game> games) {
      this.games = games;
   }

   public void addGame(Game game) {
      games.add(game);
   }

   public int getSize() {
      return games.size();
   }

   public List<Game> getGames() {
      return Collections.unmodifiableList(games);
   }
}
