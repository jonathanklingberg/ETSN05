package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class WorkspaceInstance implements DatabaseInterface {

	public WorkspaceInstance(Connection conn) {}
	


	/**
 	* 
 	* @return The projects associated with this workspace
 	*/
	public List<Project> getProjects() {
		return null;}

	/**
	 * 
	 * @param project The project to add to the workspace.
	 * @return True if it succeeds adding the project, otherwise false.
	 */
	public boolean addProject(Project project) {
		return false;}

	/**
	 * 
	 * @param project The project to be removed from the workspace.
	 * @return True if it succeeds with removing the project, otherwise false.
	 */
	public boolean removeProject(Project project) {
		return false;}

	
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
		return null;}

	

	/**
	 * @param user Should be ProjectManager or Admin, otherwise null will be returned.
	 * @return Returns the user in HTML representation.
	 */
	public String toHTML(User user) {
	}



	/**
	 * @return True if the object manages to remove itself, otherwise false
	 */
	public boolean removeMe() {
		// TODO Auto-generated method stub
		return false;
	}

}