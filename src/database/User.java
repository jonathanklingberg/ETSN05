package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * This class describes the user in the database and provides a number of methods
 * on this data. 
 * @author SG
 * @version 0.1
 * 
 */
public class User implements DatabaseInterface {
	
	/**
	 * 
	 * @param conn A connection to the database.
	 * @param name  User name.
	 * @param password A password.
	 * @param userID The user's ID.
	 * @param role The user's role.
	 */
	public User(Connection conn, String name, String password, int userID, int role) {}

	/**
	 * 
	 * @param conn A connection to the database.
	 * @param name A user name.
	 */
	public User(Connection conn, String name) {}
	
	private Connection conn;
	private String name;
	private String password;
	private int userID;
	private int role;
	
	/**
	 * 
	 * @return User name.
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * 
	 * @return User ID
	 */
	public int getUserID() {
		return -1;
	}
	
	/**
	 * 
	 * @param pw Password to be compared to
	 * @return True if they match, false otherwise
	 */
	public boolean comparePassword(String pw) {
		return false;
	}
	
	/**
	 * 
	 * @return The user role 
	 */
	public String getRole() {
		return null;
	}
	
	/**
	 * 
	 * @param role The role the user's role should be set to
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean setRole(String role) {
		return false;
	}

	/**
	 * @param user Should be a ProjectManager or Administrator, otherwise null will be returned.
	 * @return Returns the user in HTML representation.
	 */
	public String toHTML(User user) {
		return null;
	}

	/**
	 * 
	 * @return True if the object manages to remove itself, otherwise false
	 */
	public boolean removeMe() {
		return false;
	}

}
