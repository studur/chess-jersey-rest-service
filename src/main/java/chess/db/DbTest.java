package chess.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.lang.Exception;

/**
 *
 * @author Mathieu
 */
public class DbTest {

   private String url;
   private String user;
   private String password;
   private Connection connexion;
   private Statement statement;
   private ResultSet resultat;

   public DbTest(){
      url = "jdbc:mysql://localhost:3306/test";
      user = "root";
      password = "bob284132933";
      connexion = null;
      statement = null;
      resultat = null;
      connecterDB();
   }

   public Connection connecterDB(){
      try{
         Class.forName("com.mysql.jdbc.Driver");
         System.out.println("Driver téléchargé");
         connexion = DriverManager.getConnection(url, user, password);
         System.out.println("Connexion établie");
         return connexion;

      }catch(Exception e){
         e.printStackTrace();
         return null;
      }
   }

   public static void main(String[] args) {
      new DbTest();
   }


}