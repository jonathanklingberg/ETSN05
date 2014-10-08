package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *  This class is a singleton and contains operations that act over the 
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
 * @version 0.2
 * 
 */

public class WorkspaceInstance {
	private static WorkspaceInstance instance = null;
	private static Connection conn;
	
	protected WorkspaceInstance(Connection conn) {
		WorkspaceInstance.conn = conn;
		//Food for thought: Will this connection live on forever,
		//or will it be closed when the session of the user who created is
		//is closed? If so, there might be a need for a redesign, or setConnection
		//method
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
 	* Retrieves all project groups in the database
 	* 
 	* @return A list of all the project groupss associated with this database,
 	* or an empty list if no project groups exist.
 	*/
	public synchronized List<ProjectGroup> getProjectGroups() {
		return null;
	}

	/**
	 * Adds a project group to the database
	 * 
	 * @param projectGroup The project group to add to the database.
	 * @return True if the project group is successfully added to the database,
	 * otherwise false
	 */
	public synchronized boolean addProjectGroup(ProjectGroup projectGroup) {
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
	 * Retrieves a specific project group from the database
	 * 
	 * @param id The id of the project group to retrieve
	 * @return The project group that maps to id in the database, or 
	 * null if no such project group is found
	 */
	public synchronized ProjectGroup getProjectGroup(long id) {
		return null;
	}
	
//	/**
//	 * Method for generating the overall structure for the different
//	 * component classes. Will typically differ depending on the user who
//	 * is sent as input.
//	 * 
//	 * @param user The user who wants to print a project group.
//	 * @return Returns the components of the page in HTML representation.
//	 */
//	public synchronized String toHTML(User user) {
//		return null;
//		//If administrator, getProjects and getUsers should be called
//		//and then for each on each of those objects and call toHTML()
//		//on them. If project worker, call appropriate methods to
//		//generate this page instead, etc.
//		
//		//If the administrator would like to get the view of a project
//		//manager (which should be possible since the administrator also
//		//has the same rights as project manager), an 'ugly' solution
//		//is proposed here, but feel free to refine it if you find it too 
//		//ugly.
//		
//		//Simply change the role for the user (who is the administrator)
//		//to "ProjectManager" (before calling this method), 
//		//and then check what role the user has in this method. That is,
//		//always check the role to determine which page that should be generated
//		
//		//Problem if done this way is that the administrator does not have any
//		//role defined, however, then it should be null, so if the role is null
//		//we can be certain that the user is the administrator who wants 
//		//the administrator page. But as stated earlier, this is kind of
//		//ugly so there might be a better way to solve it!
//		
//		//Moreover the setRole(ProjectManager) for the administrator object should not
//		//have any effect on the database, thus a control needs to be done in
//		//setRole which checks if it is the administrator who the role is set for,
//		//if so, just set the attribute internally for the class, but do not
//		//update the database.	
//		
//	}


}