package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *  This class is singleton and contains operations that act over the 
 *  entire database. It is typically used by the 'Component' classes
 *  when initially needing information about users or project groups
 *  in the database. Then after having retrieved these objects,
 *  operations can be further carried out on them instead. The class
 *  communicates with a MySQL database via the 'Connection' attribute.
 *  
 *  The class also implements the DatabaseInterface which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database. However, the remove method will not be
 *  implemented by this class.
 *  
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
	   
	/**
	 * Typical singleton method in order to retrieve the
	 * instance of the object
	 * 
	 * @param conn A connection to the database
	 * @return The WorkspaceInstance which will be the same
	 * in the entire system.
	 */
	public static WorkspaceInstance getInstance(Connection conn) {  
		if(instance == null) {
	         instance = new WorkspaceInstance(conn);
	     }
	      return instance;
	}
	
	/**
 	* Retrieves all projects in the database
 	* 
 	* @return A list of all the projects associated with this database,
 	* or an empty list if no projects exist.
 	*/
	public synchronized List<Project> getProjects() {
		return null;
	}

	/**
	 * Adds a project to the database
	 * 
	 * @param project The project to add to the database.
	 * @return True if the project is successfully added to the database,
	 * otherwise false
	 */
	public synchronized boolean addProject(Project project) {
		return false;
	}

	/**
	 * Adds a user to the database
	 * 
	 * @param user The user to be added to the database
	 * @return True if the user is successfully added to the database,
	 * otherwise false
	 */
	public synchronized boolean addUser(User user) {
		//Since the user will contain the group id as well,
		//calls both to the "Users" table as well as the
		//"RoleInGroup" table could (should?) be made here, since
		//the user needs to be assigned to the group instantly.
		
		//Also note that according to the requirements in the base
		//system, the password needs to be 6 characters long, and
		//exactly 6 characters long. This might be a stupid requirement
		//we would like to change later on, just keep it in mind when
		//implementing for now though
		
		return false;
	}	
	
	/**
	 * Retrieves all users in the database
	 * 
	 * @return A list of all users in the database, or an empty list
	 * if no users exist in the system.
	 */
	public synchronized List<User> getUsers() {
		return null;
	}
	
	/** 
	 * Retrieves a specific user from the database
	 * 
	 * @param userName The username of the user that should be
	 * fetched from the database
	 * @return The user if it is found in the database, otherwise
	 * null
	 */
	public synchronized User getUser(String userName) {
		return null;
	}

	/**
	 * Retrieves a specific project from the database
	 * 
	 * @param id The id of the project to retrieve
	 * @return The project that maps to id in the database, or 
	 * null if no such project is found
	 */
	public synchronized Project getProject(int id) {
		return null;
	}
	
	/**
	 * @param user The user who wants to print a project.
	 * @return Returns the user in HTML representation.
	 */
	public synchronized String toHTML(User user) {
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