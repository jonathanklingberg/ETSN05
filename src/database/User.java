package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import data.Role;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 *	This class represents a User in the system, and thus
 *	contains methods which are applicable for users such
 *  as set a role of the user, or move the user to another group. This
 *  is done by making queries to the MySQL database via the connection
 *  attribute.
 *  
 *  The class also extends the AbstractCointainer which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database
 *  
 * @author SG
 * @version 0.3
 * 
 */

public class User extends AbstractCointainer implements HttpSessionBindingListener{
	private String name;
	private String password;
	private long groupID;
	private long userID;
	private String role;
	
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
	 */
	public User(Connection conn, String name, String password, 
			long userID, long groupID, String role) {
		this.conn = conn;
		this.name = name;
		this.password = password;
		this.userID = userID;
		this.groupID = groupID;
		this.role = role;
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
	public User(String name, String password, String role, long groupID) {
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
	 * Getter for password
	 * 
	 * @return The password if it exists, otherwise null
	 */
	public String getPassword() {
		return password;
	}
	
	
	/**
	 * Compares the given password with the one
	 * that is private for the user
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
				if(ps.executeUpdate() > 0){
					roleChanged = true;
				}
				killSession();
				ps.close();
			} catch (SQLException e) {
				handleSqlErrors(e);
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
		boolean successfullyMoved = false;
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE RoleInGroup SET " +
				"groupId = " + project.id +" WHERE userId = '" + userID + "'");
			if(ps.executeUpdate() > 0){
				successfullyMoved = true;
			}
			killSession();
			ps.close();
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
	 * @param requestingUserRole The role of the user in the system who wants to display this user.
	 * @return Returns the user in HTML representation.
	 */
	public String toHTML(Role requestingUserRole) {
		//TODO Implement this to be used instead of ServletBase->PrintUser() /J
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
		boolean successfullyRemoved = false;
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Users WHERE id = " + userID);
			ps.executeUpdate();
			ps = conn.prepareStatement("UPDATE RoleInGroup set isActiveInGroup = false WHERE userid = " + userID);
			if(ps.executeUpdate() > 0){
				successfullyRemoved = true;
			}
			killSession();
			ps.close();
		} catch (SQLException e) {
			successfullyRemoved = false;
			e.printStackTrace();
		}
		return successfullyRemoved;
	}
	
	/**
	 * Triggered as a user-object-instance is added as session-attribute.
	 * Kick currently logged in user and replace with new user-object into map.
	 */
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		if(usersSessions.containsKey(this.name)){
			usersSessions.get(this.name).invalidate(); // kick currently logged in user. /J
		}
		usersSessions.put(this.name, event.getSession()); // replace/add the new user to map. /J
		System.out.println("*********"+this.name+" WENT ONLINE***********");
	}

	/**
	 * Triggered as a user-object-instance is removed as session-attribute 
	 * and when current session is killed.
	 * Remove currently logged in user from sessions-map.
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		usersSessions.remove(this.name);
		System.out.println("*********"+this.name+" WENT OFFLINE***********");
	}
	
	/**
	 * Kills the session that belongs to this.username.
	 * We assume that a userName can only be used at one place at a time.
	 */
	public void killSession(){
		if(usersSessions.containsKey(this.name)){
			usersSessions.get(this.name).invalidate();		
			usersSessions.remove(this.name);
			System.out.println("*********"+this.name+" WENT OFFLINE***********");
		}
	}
	
	/**
	 * Prints username and sessionID of all currently logged in users.
	 * Used for testing purposes
	 */
	public void printActiveSessions(){
		Map<String, HttpSession> map = usersSessions;
		System.out.println("Total number of online users: " + map.size());
		for (Map.Entry<String, HttpSession> entry : map.entrySet()) { 
			System.out.println("Name = " + entry.getKey() + ", Session = " + entry.getValue().getId()); 
		}
		// Alternative method in case a user can be logged in at multiple machines simultaneously /J
//		Map<User, HttpSession> map = usersSessions;
//		System.out.println("Number of logged in users: " + map.size());
//		for (Map.Entry<User, HttpSession> entry : map.entrySet()) { 
//			System.out.println("Name = " + entry.getKey().getName() + ", Session = " + entry.getValue().getId()); 
//		}
	}
}
