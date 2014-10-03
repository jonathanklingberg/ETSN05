package database;

import java.sql.Connection;
import java.util.List;

/**
 *
 * This class describes the project in the database and provides a number of methods
 * on this data.
 * @author SG
 * @version 0.1 
 *
 */
public class Project implements DatabaseInterface {
	private int id;
	private String name;
	private Connection conn;
	
	
	/**
	 * 
	 * @param conn A connection to the database.
	 * @param id The id for the project.
	 * @param name The project name.
	 */
	public Project(Connection conn, int id, String name) {
		
	}
	
	/**
	 * 
	 * @return This method returns a list of users belonging to the project.
	 */
	public List<User> getUsers() {
		return null;
	}
	
	/**
	 * 
	 * @param userName The string of a user.
	 * @return The user who has the user name userName.
	 */
	public User getUser(String userName) {
		return null;
	}
	
	/**
	 * 
	 * @return This method returns a list of time reports belonging to the project.
	 */
	public List<TimeReport> getTimeReports() {
		return null;
	}
	
	/**
	 * 
	 * @param report The time report to be added to the project.
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean addTimeReport(TimeReport report) {
		return false;
	}
	
	/**
	 * 
	 * @param report The time report to be removed from the project.
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean removeTimeReport(TimeReport report) {
		return false;
	}
	
	
	/**
	 * 
	 * @param user The user to be added to the project.
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean addUser(User user) {
		return false;
	}
	
	/**
	 * 
	 * @param user The user to be removed from the project.
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean removeUser(User user) {
		return false;
	}
	
	/**
	 * 
	 * @return The name of the project
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * @param user The user who wants to view the project
	 * @return Returns the project in HTML representation
	 */
	public String toHTML(User user) {
		return null;
	}

	/**
	 * 
	 */
	public boolean removeMe() {
		// TODO Auto-generated method stub
		return false;
	}

}