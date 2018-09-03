package chess.models;



/**
 * This class is responsible for representing a chess player.
 */
public class Player implements Comparable<Player> {

   private String prenom;

   private String nom;

   private String fullName;

   private double rating;

   private boolean isRatingPermanent = true;

   private double oldRating;

   private double score = 0;

   private int unratedGamesPlayed = 0;

   private int wins = 0;

   private int losses = 0;

   private int ties = 0;

   public Player() {

   }

   public Player(String prenom, String nom, double rating) {
      this.nom = nom;
      this.prenom = prenom;
      this.fullName = prenom + " " + nom;
      this.rating = rating;
      this.oldRating = rating;
   }

   public Player(String prenom, String nom, double rating, int unratedGamesPlayed) {
      this.nom = nom;
      this.prenom = prenom;
      this.fullName = prenom + " " + nom;
      this.rating = rating;
      this.oldRating = rating;
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

   public String getPrenom() {
      return prenom;
   }

   public void setPrenom(String prenom) {
      this.prenom = prenom;
   }

   public String getNom() {
      return nom;
   }

   public void setNom(String nom) {
      this.nom = nom;
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
}