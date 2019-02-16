package chess.models;


/**
 * This class is responsible for representing a chess player.
 */
public class Player implements Comparable<Player> {

   private String lastName;
   private String firstName;
   private String fullName;
   private double rating;
   private boolean isRatingPermanent = true;
   private double oldRating;
   private double score = 0;
   private int unratedGamesPlayed = 0;
   private int wins = 0;
   private int losses = 0;
   private int ties = 0;


   /**
    * Simple Player constructor for a player with a permanent rating.
    *
    * @param lastName  First name.
    * @param firstName Last name.
    * @param rating    Rating value.
    */
   public Player(String lastName, String firstName, double rating) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.fullName = lastName + " " + firstName;
      this.rating = rating;
      this.oldRating = rating;
   }

   /**
    * Constructor for a new player without a rating or a player with a temporary rating.
    *
    * @param lastName
    * @param firstName
    * @param rating
    * @param unratedGamesPlayed
    */
   public Player(String lastName, String firstName, double rating, int unratedGamesPlayed) {
      this(lastName, firstName, rating);
      this.isRatingPermanent = false;
      this.unratedGamesPlayed = unratedGamesPlayed;
   }

   @Override
   public String toString() {
      return "Name=" + fullName + ", rating=" + rating;
   }

   @Override
   public int compareTo(Player otherPlayer) {
      if (this.rating > otherPlayer.rating) {
         return 1;
      } else if (this.rating < otherPlayer.rating) {
         return -1;
      }
      return 0;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getFullName() {
      return fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   public double getRating() {
      return rating;
   }

   public void setRating(double rating) {
      this.rating = rating;
   }

   public boolean isRatingPermanent() {
      return isRatingPermanent;
   }

   public void setRatingPermanent(boolean ratingPermanent) {
      isRatingPermanent = ratingPermanent;
   }

   public double getOldRating() {
      return oldRating;
   }

   public void setOldRating(double oldRating) {
      this.oldRating = oldRating;
   }

   public double getScore() {
      return score;
   }

   public void setScore(double score) {
      this.score = score;
   }

   public int getUnratedGamesPlayed() {
      return unratedGamesPlayed;
   }

   public void setUnratedGamesPlayed(int unratedGamesPlayed) {
      this.unratedGamesPlayed = unratedGamesPlayed;
   }

   public int getWins() {
      return wins;
   }

   public void setWins(int wins) {
      this.wins = wins;
   }

   public void addWin() {
      this.wins++;
      this.score++;
   }

   public void addLoss() {
      this.losses++;
   }

   public void addTie() {
      this.ties++;
      this.score += 0.5;
   }

   public int getLosses() {
      return losses;
   }

   public void setLosses(int losses) {
      this.losses = losses;
   }

   public int getTies() {
      return ties;
   }

   public void setTies(int ties) {
      this.ties = ties;
   }

   /**
    * Sorts an array of {@link Player} using a insertion sort algorithm based on their score.
    *
    * @param players Array of {@link Player}
    */
   public static void insertionSortOnScore(Player[] players) {
      //TODO Add a step to sort equal score on their tournament performance rating.
      int length = players.length;
      for (int playerNumber = 1; playerNumber < length; playerNumber++) {
         Player key = players[playerNumber];
         int otherPlayerNumber = playerNumber - 1;
         while ((otherPlayerNumber >= 0) && (players[otherPlayerNumber].getScore() < key.getScore())) {
            players[otherPlayerNumber + 1] = players[otherPlayerNumber];
            otherPlayerNumber--;
         }
         players[otherPlayerNumber + 1] = key;
      }
   }
}