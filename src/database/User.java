package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *	This class represents a User in the system, and thus
 *	contains methods which are applicable for users such
 *  as set a role of the user, or move the user to another group. This
 *  is done by making queries to the MySQL database via the connection
 *  attribute.
 *  
 *  The class also implements the DatabaseInterface which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database
 *  
 * @author SG
 * @version 0.1
 * 
 */
public class User implements DatabaseInterface {
	private Connection conn;
	private String name;
	private String password;
	private int userID;
	private int groupID;
	private int role;
	private String sessionID; 
	
	/**
	 * Constructor which should be used when the user
	 * already exists in the database since it will be possible
	 * to fetch all information needed in this case
	 * 
	 * @param conn A connection to the database.
	 * @param name  Username.
	 * @param password A password.
	 * @param userID The user's ID.
	 * @param groupID The user's group ID.
	 * @param role The user's role.
	 * @param isOnline Cookie-identifier.
	 */
	public User(Connection conn, String name, String password, 
			int userID, int groupID, int role, String isOnline) {}

	/**
	 * Constructor which should be used when the user is about
	 * to be created in the database
	 * 
	 * @param conn A connection to the database.
	 * @param name Username.
	 * @param password A password.
	 * @param role The user's role.
	 * @param groupID The user's  group ID.
	 */
	public User(Connection conn, String name, String password, 
			int role, int groupID) {}
	
	/**
	 * Getter for the username
	 * 
	 * @return The username of the user.
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * Setter for the username
	 * 
	 * @param name The new username
	 * @return True if it succeeds, false otherwise
	 */
	public boolean setName(String name) {
		return false;
	}
	
	/**
	 * Getter for the userID
	 * @return The user id of the user
	 */
	public int getUserId() {
		return userID;
	}
	
	/**
	 * Getter for the groupID
	 * @return The group id of the group the user is a part of
	 */
	public int getGroupId() {
		return groupID;
	}
	
	/**
	 * Getter for session ID
	 * @return The session ID if it exists, otherwise null
	 */
	public String getSessionId() {
		return null;
	}
	
	
	/**
	 * Compares the given password with the one
	 * which is private for the user
	 * 
	 * @param pw Password to be checked
	 * @return True if they match, false otherwise
	 */
	public boolean comparePassword(String pw) {
		return false;
	}
	
	/**
	 * Setter for the password
	 * 
	 * @param password The new password
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean setPassword(String password) {
		return false;
	}
	
	/**
	 * Getter for the role of the user
	 * 
	 * @return The user role 
	 */
	public String getRole() {
		return null;
	}
	
	/**
	 * Setter for the role of the user
	 * 
	 * @param role The role to change to.
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean setRole(String role) {
		return false;
	}
	
	/**
	 *  Moves a user to another group
	 *  
	 * 	@param project The project the user should be moved to
	 */
	public void moveUser(Project project) {
		//Should be sufficient to do an update table
		//instead of having to use "Add" and "Remove"
		//methods in the two different projects
	}
		
	
	/**
	 * Will produce an HTML representation of the user depending on the
	 * user asking for it
	 * 
	 * @param user A user in the system who wants to display this user.
	 * @return Returns the user in HTML representation.
	 */
	public String toHTML(User user) {
		return null;
	}

	/**
	 * Will remove the user from the database as well as from the
	 * project group the user is active in. However the user's
	 * time reports will be kept in the system
	 * 
	 * @return True if the object manages to remove itself, otherwise false
	 */
	public boolean removeMe() {
		//Don't forget to set 'activeInGroup' to false
		//when removing the user
		return false;
	}

}
