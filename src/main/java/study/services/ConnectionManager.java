package study.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectionManager {
	   private static final String DB_URL = "jdbc:h2:mem:univ";
	   private static final String USER = "username";
	   private static final String PASS = "password";

	   public Connection getConnection() {
	      Connection conn = null;
	      Statement stmt = null;
	      try {
	         Class.forName("org.h2.Driver");
	         return DriverManager.getConnection(DB_URL, USER, PASS);
	      } catch(Exception e){
	         throw new RuntimeException(e);   
	      }
	   }
	}