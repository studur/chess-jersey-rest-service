package chess.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import chess.models.Player;
import chess.models.Tournament;

public class TournamentReport {

   /**
    * Method used to print the Tournament report to the console.
    */
   public static void printTournamentReport(List<Player> players) {
      System.out.println(getTournamentReport(players));
   }

   /**
    * Method used to print the Tournament report to the console.
    */
   public static String getTournamentReport(List<Player> players) {
      StringBuilder report = new StringBuilder();
      report.append("*********************\n");
      report.append("* Tournament report *\n");
      report.append("*********************\n");
      for (Player aPlayersStanding : players) {
         report.append(aPlayersStanding.getFullName() + " : ");
         report.append(aPlayersStanding.getWins() + " wins ");
         report.append(aPlayersStanding.getLosses() + " losses ");
         report.append(aPlayersStanding.getTies() + " ties ");
         report.append("New rating is " + new DecimalFormat("##").format(aPlayersStanding.getRating()));
         if (!aPlayersStanding.isRatingPermanent()) {
            report.append("/" + aPlayersStanding.getUnratedGamesPlayed());
         }
         report.append("\n");
      }
      return report.toString();
   }

   /**
    * Method used to print the {@link Tournament} report to a semi-colon separated CSV file.
    *
    * @param fileName Name of the CSV file generated.
    * @throws IOException Thrown if IO problems with file generation.
    */
   public static void printTournamentReportToCsvFile(List<Player> players, String fileName) throws IOException {

      try (BufferedWriter bufferedWriter = new BufferedWriter(
            new FileWriter("./" + fileName + ".csv"))) {
         bufferedWriter.write("Nom;Ancienne cote;Gains;Nulles;Pertes;Nouvelle cote");
         bufferedWriter.newLine();
         for (Player aPlayersStanding : players) {
            bufferedWriter.write(aPlayersStanding.getFullName() + ";");
            bufferedWriter.write(new DecimalFormat("##").format(aPlayersStanding.getOldRating()) + ";");
            bufferedWriter.write(aPlayersStanding.getWins() + ";");
            bufferedWriter.write(aPlayersStanding.getTies() + ";");
            bufferedWriter.write(aPlayersStanding.getLosses() + ";");
            bufferedWriter.write(new DecimalFormat("##").format(aPlayersStanding.getRating()));
            bufferedWriter.newLine();
         }
      }
   }

}
