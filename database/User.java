package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author seb
 *
 * This class describes the user in the database and provides a number of methods
 * on this data
 */
public class User implements DatabaseInterface {
	
	/**
	 * 
	 * @param Connection conn is a connection to the database
	 */
	User(Connection conn) {
	}
	
	private Connection conn;
	private string name;
	private string password;
	private int userID;
	private int role;
	
	/**
	 * 
	 * @param u Depending on what role user u has the output is different
	 * @return The user in HTML
	 */
	public String toHtml(User u) {}
	
	/**
	 * 
	 * @return The name
	 */
	public String getName() {}
	
	/**
	 * 
	 * @return User ID
	 */
	public int getUserID() {}
	
	/**
	 * 
	 * @param pw Password to be compared to
	 * @return True if they match, false otherwise
	 */
	public boolean comparePassword(String pw) {}
	
	/**
	 * 
	 * @return The role 
	 */
	public String getRole() {}

}
