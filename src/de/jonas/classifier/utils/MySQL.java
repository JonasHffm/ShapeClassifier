package de.jonas.classifier.utils;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {
       
        private String HOST = "";
        private String DATABASE = "";
        private String USER = "";
        private String PASSWORD = "";
       
        private Connection con;
       
        public MySQL(String host, String database, String user, String password) {
            this.HOST = host;
            this.DATABASE = database;
            this.USER = user;
            this.PASSWORD = password;
            connect();
        }
        
        
        public void connect() {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?autoReconnect=true", USER, PASSWORD);
                System.out.println("[MySQL] Die Verbindung zur MySQL-Datenbank wurde hergestellt! - " + this.DATABASE);
            } catch (SQLException e) {
                    System.out.println("[MySQL] Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
            }
        }
        
        
        public void close() {
            try {
                if(con != null) {
                        con.close();
                        System.out.println("[MySQL] Die Verbindung zur MySQL wurde Erfolgreich beendet! - MySQLSystem");
                }
            } catch (SQLException e) {
                    System.out.println("[MySQL] Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
            }
        }
        
        
        public void update(String qry) {
            try {
                con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
       
        
        public ResultSet getResults(String qry) {
        	try {
				return con.createStatement().executeQuery(qry);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	return null;
        }

        
}