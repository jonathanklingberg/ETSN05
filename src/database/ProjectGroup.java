package database;

import java.sql.Connection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import data.Roles;

/**
 *	This class represents a Project Group in the system, and thus
 *	contains methods which are applicable for project groups such
 *  as listing users or time reports belonging to the group. This
 *  is done by making queries to the MySQL database via the connection
 *  attribute.
 *  
 *  The class also implements the DatabaseInterface which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database
 *  
 *  @author SG
 *  @version 0.2
 */

public class ProjectGroup extends DatabaseInterface {
	protected String name;
	protected long id;
	
	/**
	 * Constructor which should be used when the project group
	 * already exists in the database since it will be possible
	 * to fetch all information needed in this case
	 * 
	 * @param conn The connection to the database
	 * @param id The id of the project group
	 * @param name The name of the project group
	 */
	public ProjectGroup(Connection conn, long id, String name) {
		super();
		this.conn = conn;
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Constructor which should only be used when the project group is about
	 * to be created in the database.
	 * 
	 * @param name The name of the project group
	 */
	public ProjectGroup(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves all users currently active in the group
	 * 
	 * @return A list of User objects, all of whom are active in
	 * this group. Will return an empty list if no users are active
	 * in the group
	 */
	public List<User> getUsers() {
		//Make SQL statement to get Users from Database,
		//Probably will need to join on Users table
		//and "RoleInGroup", and select that the
		//"activeInGroup" is yes, and project id is the id given
		//in this class
		return null;
	}
	
	
	/**
	 * Retrieves all time reports in the project group
	 *
	 * @return A list of time reports in this group.
	 * Will return an empty list if no time reports
	 * are yet available
	 */
	public List<TimeReport> getTimeReports() {
		return new ArrayList<TimeReport>();
	}
	
	/**
	 * Retrieves a time report from the database based on
	 * an given id of the time report
	 * 
	 * @param reportId The id of the time report to be fetched
	 * @return The time report if it exists, otherwise null
	 */
	public TimeReport getTimeReport(long reportId) {
		return null;
	}
	
	public long getId() {
		return -1;
	}
	
	/**
	 * Adds a time report to the system
	 * 
	 * @param report The time report to add
	 * @return True if the time report was successfully added,
	 * otherwise false
	 */
	public boolean addTimeReport(TimeReport report) {
		return false;
	}
	
	/**
	 * Removes a time report from the project group
	 * 
	 * @param report The time report to be removed
	 * @return True if the time report was successfully removed,
	 * otherwise false
	 */
	public boolean removeTimeReport(TimeReport report) {
		//Don't forget to check that the time report is unsigned!
		return false;
	}
	
	/**
	 * Adds a user to the project
	 * 
	 * @param user The user to be added to the project
	 * @return True if the user was successfully added,
	 * otherwise false
	 */
	public boolean addUser(User user) {
		//Don't forget to check that the user is not
		//the administrator, since he isn't allowed to
		//be a part of a group
		
		return false;
	}
	
	/**
	 * Removes a user from the project
	 * 
	 * @param user The user to be removed from the project
	 * @return True if the user was successfully removed,
	 * otherwise false
	 */
	public boolean removeUser(User user) {
		return false;
	}
	
	/**
	 * Will produce an HTML representation of the project depending on the
	 * user asking for it
	 * 
	 * @param user A user in the system who wants to display this project.
	 * @return Returns the project in HTML representation.
	 */
	public String toHTML(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Will return the name of the project group.
	 * @return The name of the project group.
	 */
	public String getProjectName(){
		return name;
	}

	/**
	 * Will remove the project group from the database, which
	 * in turn will remove all users that are active in the group
	 * as well as all time reports associated with the group
	 * 
	 * @return True if the object successfully managed to remove itself,
	 * otherwise false
	 */
	public boolean removeMe() {
		//Instead of fetching all time reports and doing 'removeMe'
		//on them, as well as on all users it's probably easier to
		//just execute the correct SQL here directly instead of taking
		//the detour of creating all objects
		return false;
	}

}
