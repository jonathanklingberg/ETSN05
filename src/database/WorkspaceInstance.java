package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 
 * @author seb
 * This class contains operations that act over the whole database, and certain specific operations on projects and users
 */
public class WorkspaceInstance implements DatabaseInterface {

	/**
	 * 
	 * @param conn A connection to the database
	 */
	public WorkspaceInstance(Connection conn) {}
	


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
	 * @param user Should be a ProjectManager or Administrator, otherwise null will be returned.
	 * @return Returns the user in HTML representation.
	 */
	public String toHTML(User user) {
		return null;
	}

	/**
	 * @return True if the object manages to remove itself, otherwise false
	 */
	public boolean removeMe() {
		// TODO Auto-generated method stub
		return false;
	}

}