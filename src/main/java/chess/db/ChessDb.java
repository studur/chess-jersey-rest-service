package chess.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ChessDb {

   private Connection connect = null;
   private Statement statement = null;
   private PreparedStatement preparedStatement = null;
   private ResultSet resultSet = null;

   public void readDataBase() throws Exception {
      try {
         // This will load the MySQL driver, each DB has its own driver
         Class.forName("com.mysql.cj.jdbc.Driver");
         // Setup the connection with the DB
//         connect = DriverManager.getConnection("jdbc:mysql://mychemdb.dyndns.org/chess?" +
//               "user=studur&password=nip2cl2L");

         connect = DriverManager.getConnection("jdbc:mysql://mychemdb.dyndns.org/chess","studur","nip2cl2L");

         // Statements allow to issue SQL queries to the database
         statement = connect.createStatement();
         // Result set get the result of the SQL query
         resultSet = statement
               .executeQuery("select * from players");
         writeResultSet(resultSet);

//         // PreparedStatements can use variables and are more efficient
//         preparedStatement = connect
//               .prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
//         // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
//         // Parameters start with 1
//         preparedStatement.setString(1, "Test");
//         preparedStatement.setString(2, "TestEmail");
//         preparedStatement.setString(3, "TestWebpage");
//         preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
//         preparedStatement.setString(5, "TestSummary");
//         preparedStatement.setString(6, "TestComment");
//         preparedStatement.executeUpdate();
//
//         preparedStatement = connect
//               .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
//         resultSet = preparedStatement.executeQuery();
//         writeResultSet(resultSet);
//
//         // Remove again the insert comment
//         preparedStatement = connect
//               .prepareStatement("delete from feedback.comments where myuser= ? ; ");
//         preparedStatement.setString(1, "Test");
//         preparedStatement.executeUpdate();
//
//         resultSet = statement
//               .executeQuery("select * from feedback.comments");
//         writeMetaData(resultSet);

      } catch (Exception e) {
         throw e;
      } finally {
         close();
      }

   }
   private void writeMetaData(ResultSet resultSet) throws SQLException {
      //  Now get some metadata from the database
      // Result set get the result of the SQL query

      System.out.println("The columns in the table are: ");

      System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
      for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
         System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
      }
   }

   private void writeResultSet(ResultSet resultSet) throws SQLException {
      // ResultSet is initially before the first data set
      while (resultSet.next()) {
         // It is possible to get the columns via name
         // also possible to get the columns via the column number
         // which starts at 1
         // e.g. resultSet.getSTring(2);
         String firstName = resultSet.getString("prenom");
         String lastName = resultSet.getString("nom");
         String cote = resultSet.getString("cote");
         String permanente = resultSet.getString("permanente");
         String partiesnomcotees = resultSet.getString("partiesnoncotees");

         System.out.print("Prenom: " + firstName);
         System.out.print(", Nom: " + lastName);
         System.out.print(", Cote: " + cote);
         System.out.print(", "+(permanente.equals("1")?"Permanente":"Temporaire"));
         System.out.print(", "+(permanente.equals("0")?partiesnomcotees:""));
         System.out.println();
      }
   }

   // You need to close the resultSet
   private void close() {
      try {
         if (resultSet != null) {
            resultSet.close();
         }

         if (statement != null) {
            statement.close();
         }

         if (connect != null) {
            connect.close();
         }
      } catch (Exception e) {

      }
   }

   public static void main(String[] args) {
      ChessDb chessDb = new ChessDb();
      try {
         chessDb.readDataBase();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
