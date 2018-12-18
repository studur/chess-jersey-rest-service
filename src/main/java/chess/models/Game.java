package chess.models;

import java.util.NavigableMap;
import java.util.TreeMap;

public class Game {

   /**
    * This map is used to fetch the probability value based on the rating difference between two chess players.
    */
   private final static NavigableMap<Integer, Double> map;

   static {
      map = new TreeMap<>();
      map.put(0, 0.50);
      map.put(4, 0.51);
      map.put(11, 0.52);
      map.put(18, 0.53);
      map.put(26, 0.54);
      map.put(33, 0.55);
      map.put(40, 0.56);
      map.put(47, 0.57);
      map.put(54, 0.58);
      map.put(62, 0.59);
      map.put(69, 0.60);
      map.put(77, 0.61);
      map.put(84, 0.62);
      map.put(92, 0.63);
      map.put(99, 0.64);
      map.put(107, 0.65);
      map.put(114, 0.66);
      map.put(122, 0.67);
      map.put(130, 0.68);
      map.put(138, 0.69);
      map.put(146, 0.70);
      map.put(154, 0.71);
      map.put(163, 0.72);
      map.put(171, 0.73);
      map.put(180, 0.74);
      map.put(189, 0.75);
      map.put(198, 0.76);
      map.put(207, 0.77);
      map.put(216, 0.78);
      map.put(226, 0.79);
      map.put(236, 0.80);
      map.put(246, 0.81);
      map.put(257, 0.82);
      map.put(268, 0.83);
      map.put(279, 0.84);
      map.put(291, 0.85);
      map.put(303, 0.86);
      map.put(316, 0.87);
      map.put(329, 0.88);
      map.put(345, 0.89);
      map.put(358, 0.90);
      map.put(375, 0.91);
      map.put(392, 0.92);
      map.put(412, 0.93);
      map.put(433, 0.94);
      map.put(457, 0.95);
      map.put(485, 0.96);
      map.put(518, 0.97);
      map.put(560, 0.98);
      map.put(620, 0.99);
      map.put(735, 1.00);
   }

   public double result;

   public Player player1;

   public Player player2;

   /**
    * Game constructor.
    *
    * @param player1 First {@link Player}
    * @param player2 Second {@link Player}
    * @param result  Result : 1 if first player wins, 0 if first player loses, 0.5 in case of a draw.
    */
   public Game(Player player1, Player player2, double result) {
      this.player1 = player1;
      this.player2 = player2;
      this.result = result;
   }

   /**
    * This method is used to calculate a rating adjustment (delta) based on the result from a game
    * between two chess players.
    *
    * @param player1      First {@link Player}
    * @param player2      Second {@link Player}
    * @param actualResult Result : 1 if first player wins, 0 if first player loses, 0.5 in case of a draw.
    * @return The rating variation.
    */
   public static double getDeltaFromGame(Player player1, Player player2, double actualResult) {

      int diff = Math.toIntExact(Math.round(player1.getRating() - player2.getRating()));
      double expectedResult;

      // To do a lookup for some value in 'key'
      if (diff < 0) {
         expectedResult = 1 - map.floorEntry(Math.abs(diff)).getValue();
      } else {
         expectedResult = map.floorEntry(diff).getValue();
      }

      return 32 * (actualResult - expectedResult);
   }

}
