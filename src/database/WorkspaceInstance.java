package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * This class contains operations that act over the whole database, and certain specific operations on projects and users
 * @author SG
 * @version 0.1
 * 
 */
public class WorkspaceInstance implements DatabaseInterface {
	private static WorkspaceInstance instance = null;
	private static Connection conn;
	
	protected WorkspaceInstance(Connection conn) {
		WorkspaceInstance.conn = conn;
	}
	   
	public static WorkspaceInstance getInstance(Connection conn) {  
		if(instance == null) {
	         instance = new WorkspaceInstance(conn);
	     }
	      return instance;
	   }
	
	/**
 	* 
 	* @return The projects associated with this workspace
 	*/
	public List<Project> getProjects() {
		return null;
	}

	/**
	 * 
	 * @param project The project to add to the workspace.
	 * @return True if it succeeds adding the project, otherwise false.
	 */
	public boolean addProject(Project project) {
		return false;
	}

	/**
	 * 
	 * @param project The project to be removed from the workspace.
	 * @return True if it succeeds with removing the project, otherwise false.
	 */
	public boolean removeProject(Project project) {
		return false;
	}

	/**
	 * 
	 * @param user The user to be added to the database
	 */
	public void addUser(User user) {}	
	/**
	 * 
	 * @param user The user to be removed from the database
	 */
	public void removeUser(User user) {}
	
	/**
	 * 
	 * @return A list of users in the workspace
	 */
	public List<User> getUsers() {
		return null;
	}

	/**
	 * 
	 * @param id The id of the project to retrieve
	 * @return The Project that maps to id in the database
	 */
	public Project getProject(int id) {
		return null;
	}
	
	/**
	 * 
	 * @param user The user to be moved from one project to another
	 * @param project1 The project the user should be moved from
	 * @param project2 The project the user should be moved to
	 */
	public void moveUser(User user, Project project1, Project project2) {}
	
	/**
	 * 
	 * @param user The user who should get its role changed
	 * @param role The role to change to.
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean changeRole(User user, int role) {
		return false;
	}
	
	/**
	 * 
	 * @param user The user who should get its name changed
	 * @param name The new user name
	 * @return True if it succeeds, false otherwise
	 */
	public boolean changeUserName(User user, String name) {
		return false;
	}
	
	/**
	 * 
	 * @param user The user who should get its password changed
	 * @param password The new password
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean changePassword(User user, String password) {
		return false;
	}
	
	/**
	 * @param user The user who wants to print a project.
	 * @return Returns the user in HTML representation.
	 */
	public String toHTML(User user) {
		return null;
	}

	/**
	 * Should not be implemented!
	 * @return False.
	 */
	public boolean removeMe() {
		return false;
	}

}