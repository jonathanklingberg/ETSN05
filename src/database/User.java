package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * @version 0.2
 * 
 */
public class User extends DatabaseInterface {
	private String name;
	private String password;
	private long groupID;
	private long userID;
	private String role;
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
			long userID, long groupID, String role, String isOnline) {
		this.conn = conn;
		this.name = name;
		this.password = password;
		this.userID = userID;
		this.groupID = groupID;
		this.role = role;
		this.sessionID = isOnline;
	}

	/**
	 * Constructor which only should be used when the user is about
	 * to be created in the database
	 * 
	 * @param name The username of the user
	 * @param password The password for the user.
	 * @param role The user's role.
	 * @param groupID The user's  group ID.
	 */
	public User(String name, String password, 
			String role, long groupID) {
		this.name = name;
		this.password = password;
		this.role = role;
		this.groupID = groupID;
	}
	
	/**
	 * Getter for the username
	 * 
	 * @return The username of the user.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for the username
	 * 
	 * @param name The new username
	 * @return True if it succeeds, false otherwise
	 */
	public boolean setName(String name) {
		int length = name.length();
		boolean ok = true;
		if(length >= 5 && length <= 10){
			for(int i = 0; i < length; i++){
				int c = (int)name.charAt(i);
				if(!((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122))){
					ok = false;
				}
			}
			if(ok){
				this.name = name;
			}	
		}
		return ok;
	}
	
	/**
	 * Getter for the userID
	 * 
	 * @return The user id of the user
	 */
	public long getUserId() {
		return userID;
	}
	
	/**
	 * Getter for the groupID
	 * 
	 * @return The group id of the group the user is a part of
	 */
	public long getGroupId() {
		return groupID;
	}
	
	/**
	 * Getter for session ID
	 * 
	 * @return The session ID if it exists, otherwise null
	 */
	public String getSessionId() {
		return sessionID;
	}
	
	/**
	 * Getter for password
	 * 
	 * @return The password if it exists, otherwise null
	 */
	public String getPassword() {
		return password;
	}
	
	
	
	/**
	 * Compares the given password with the one
	 * which is private for the user
	 * 
	 * @param pw Password to be checked
	 * @return True if they match, false otherwise
	 */
	public boolean comparePassword(String pw) {
		return password.equals(pw);
	}
	
	/**
	 * Setter for the password
	 * 
	 * @param password The new password
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean setPassword(String password) {
		boolean ok = false;
		if(password.length() == 6){
			ok = true;
			for(int i = 0; i < password.length(); i++){
				int c = (int)name.charAt(i);
				if(!(c >= 97 && c <= 122)){
					ok = false;
				}
			}
			if(ok){
				this.password = password;
			}
		
		}
		return ok;
	}
	
	/**
	 * Getter for the role of the user
	 * 
	 * @return The user role 
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Setter for the role of the user
	 * 
	 * @param role The role to change to.
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean setRole(String role) {
		//Don't forget to kill the user's session since he should
		//be logged out after this operation has been done
		
		//Also check if the user is the administrator, if so
		//just set the attribute this.role = role but do not
		//make any changes to the database, since the administrator
		//account should not exist in the "RoleInGroup" table.
		
		//The reason to actually use setRole for the administrator is
		//that it will allow him to determine when he would like to gain
		//project management rights. This is as stated in WorkspaceInstance
		//->toHTML() an ugly solution, but it should work. But feel free to
		//refine it.
		
		boolean roleChanged = false;

		if(role == null){
			this.role = role;
			roleChanged = true;
		}else{
			try {
				PreparedStatement ps = conn.prepareStatement("UPDATE RoleInGroup SET role = '" + role + "' WHERE userId = '" + this.userID + "'");
				ps.executeUpdate();
				roleChanged = true;
				ps = conn.prepareStatement("UPDATE Users SET sessionId = NULL WHERE userId = '" + this.userID + "'");
				this.sessionID = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return roleChanged;
		}
		
		
		return false;
	}
	
	/**
	 *  Moves a user to another group
	 *  
	 * 	@param project The project the user should be moved to
	 *  @return True if the user is successfully moved to the
	 *  specified project, otherwise false
	 */
	public boolean moveUser(ProjectGroup project) {
		//Should be sufficient to do an update table
		//instead of having to use "Add" and "Remove"
		//methods in the two different projects
		
		//Don't forget to kill the user's session since he should
		//be logged out after this operation has been done
		
		//The user maintains his role from his previous project
		
		boolean successfullyMoved = false;
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE RoleInGroup SET " +
				"groupId = " + project.id +" WHERE userId = '" + userID + "'");
			ps.executeUpdate();
			sessionID = null;
			successfullyMoved = true;
		} catch (SQLException e) {
			successfullyMoved = false;
			e.printStackTrace();
		}
		return successfullyMoved;
	}
		
	
	/**
	 * Will produce an HTML representation of the user depending on the
	 * user asking for it
	 * 
	 * @param user A user in the system who wants to display this user.
	 * @return Returns the user in HTML representation.
	 */
	public String toHTML(User requestingUser) {
		
		//If the administrator asks password and everything 
		//should be displayed and if a project worker asks
		//not that much should be displayed etc..
		return null;
	}

	/**
	 * Will remove the user from the database (as well as from the
	 * project group the user is active in). However the user's
	 * time reports will be kept in the system
	 * 
	 * @return True if the object manages to remove itself, otherwise false
	 */
	public boolean removeMe() {
		//Don't forget to set 'activeInGroup' to false
		//when removing the user
		boolean successfullyRemoved = false;
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Users WHERE id = '" + userID + "'");
			ps.executeUpdate();
			ps = conn.prepareStatement("UPDATE RoleInGroup set isActiveInGroup = false WHERE userid = '" + userID + "'");
			ps.executeUpdate();
			sessionID = null;
			successfullyRemoved = true;
		} catch (SQLException e) {
			successfullyRemoved = false;
			e.printStackTrace();
		}
		return successfullyRemoved;
	
	}

}
